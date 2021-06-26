package com.omegamark.remember.ui.members

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.omegamark.remember.R
import com.omegamark.remember.adapters.GetRelationSpinnerAdapter
import com.omegamark.remember.utility.*
import com.omegamark.remember.viewmodels.members.CreateMemberViewModel
import com.omegamark.remember.viewmodels.members.CreateMemberViewModelFactory
import com.omegamark.remember.viewmodels.members.UploadImageViewModel
import com.omegamark.remember.viewmodels.members.UploadImageViewModelFactory
import com.omegamark.remember.viewmodels.relation.GetRelationViewModel
import com.omegamark.remember.viewmodels.relation.GetRelationViewModelFactory
import com.remember.api.models.registration.User
import com.remember.api.models.relation.Relation
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.edit_profile_dialog.*
import kotlinx.android.synthetic.main.fragment_create_member.*
import kotlinx.android.synthetic.main.fragment_create_member.etBorn
import kotlinx.android.synthetic.main.fragment_create_member.view.*
import kotlinx.android.synthetic.main.member_confirmation_dialog.*
import kotlinx.coroutines.launch
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class CreateMemberFragment : Fragment() {

    private lateinit var viewForLayout :View

    lateinit var spinnerAdapter: GetRelationSpinnerAdapter
    lateinit var relationList: ArrayList<Relation>
    private lateinit var apiRepository: APIRepository
    private lateinit var getRelationViewModelFactory: GetRelationViewModelFactory
    private lateinit var getRelationViewModel: GetRelationViewModel
    private lateinit var uploadImageViewModelFactory: UploadImageViewModelFactory
    private lateinit var uploadImageViewModel : UploadImageViewModel
    private lateinit var createMemberViewModelFactory: CreateMemberViewModelFactory
    private lateinit var createMemberViewModel: CreateMemberViewModel
    private lateinit var sharedPref: SharedPref
    private lateinit var user : User
    lateinit var cal: Calendar
    lateinit var dateSetListenerBorn: DatePickerDialog.OnDateSetListener
    lateinit var dateSetListenerDied: DatePickerDialog.OnDateSetListener
    lateinit var bornLatitude:String
    lateinit var diedLatitude:String
    lateinit var restingLatitude:String
    lateinit var bornLongitude:String
    lateinit var diedLongitude:String
    lateinit var restingLongitude:String
    var selectedOption: Int=0
     var imageFile :File?=null
    private val requestPermission= registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        result-> if( (result[android.Manifest.permission.READ_EXTERNAL_STORAGE] == true) && ((result[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == true)))
    {

            pickGalleryImage(launchGal)
        }
        else {Toast.makeText(requireContext(),"Storage access permission denied",Toast.LENGTH_LONG).show()}
    }
    private val launchGal = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data!!


            try {
                val contentResolver = requireContext().contentResolver
                val inputStream :InputStream? = contentResolver.openInputStream(uri)
                var selected_img : Bitmap = BitmapFactory.decodeStream(inputStream);
                lifecycleScope.launch { imageFile= bitmapToFile(selected_img,"profilepic.jpeg",requireContext()) }

                Log.d("shiva", "selected image" + selected_img.width + ":" + selected_img.height)
                Log.d("shiva", "File_name" + imageFile?.absoluteFile.toString())
                viewForLayout.user_image.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        selected_img,
                        250,
                        250,
                        false
                    )
                )
            } catch (e: FileNotFoundException) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "An error occured!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(requireContext(), "You didn't pick an image!", Toast.LENGTH_LONG).show();
        }

        }
    private val getAddress= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if (result.resultCode == Activity.RESULT_OK) {

            var intent = result.data
            var address =intent?.getStringExtra("address")
            var latitude =intent?.getStringExtra("latitude")
            var longitude =intent?.getStringExtra("longitude")
            Log.d("shiva",address)
            Log.d("shiva",latitude)
            Log.d("shiva",longitude)
            Log.d("shiva",intent?.getIntExtra("type",0).toString())
            when(intent?.getIntExtra("type",0)){
                1->{
                    viewForLayout.etBirthPlace.setText(address)
                    bornLatitude= latitude.toString()
                    bornLongitude=longitude.toString()
                }
                2->{
                    viewForLayout.etDeathPlace.setText(address)
                    diedLatitude= latitude.toString()
                    diedLongitude=longitude.toString()
                }
                3->{
                    viewForLayout.etRestPlace.setText(address)
                    restingLatitude= latitude.toString()
                    restingLongitude=longitude.toString()
                }
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForLayout= inflater.inflate(R.layout.fragment_create_member, container, false)
        sharedPref= SharedPref(requireContext())
        user = sharedPref.get<User>("user")!!
        apiRepository= APIRepository()
        getRelationViewModelFactory = GetRelationViewModelFactory(apiRepository)
        getRelationViewModel= ViewModelProvider(this, getRelationViewModelFactory).get(GetRelationViewModel::class.java)
        uploadImageViewModelFactory= UploadImageViewModelFactory(apiRepository)
        uploadImageViewModel=ViewModelProvider(this,uploadImageViewModelFactory).get(UploadImageViewModel::class.java)
        createMemberViewModelFactory =CreateMemberViewModelFactory(apiRepository)
        createMemberViewModel=ViewModelProvider(this,createMemberViewModelFactory).get(CreateMemberViewModel::class.java)
        relationList= ArrayList()
        spinnerAdapter = GetRelationSpinnerAdapter(requireContext(), relationList)
        viewForLayout.spinner.adapter = spinnerAdapter
        getRelation(requireContext(),user.id,getRelationViewModel)
        observeRelationResponse(this)
        observeUploadImageResponse(this)
        observeCreateMember(this)
        initialiseCalenderPickeBorn()
        initialiseCalenderPickerDied()
        viewForLayout.user_image.setOnClickListener{

            requestMultiplePermission(requestPermission,arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
        viewForLayout.etBorn.setOnClickListener{
            val dpd: DatePickerDialog = DatePickerDialog(
                requireContext(), dateSetListenerBorn,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            cal.add(Calendar.DAY_OF_MONTH, 1);
            val dp = dpd.datePicker
            dp.maxDate = cal.timeInMillis
            dpd.show()
        }
        viewForLayout.etDied.setOnClickListener {
            val dpd: DatePickerDialog = DatePickerDialog(
                requireContext(), dateSetListenerDied,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            cal.add(Calendar.DAY_OF_MONTH, 1);
            val dp = dpd.datePicker
           // dp.minDate = cal.timeInMillis
            dpd.show()
        }
        viewForLayout.etBirthPlace.setOnClickListener{showMapActivity(1)}
        viewForLayout.etDeathPlace.setOnClickListener{showMapActivity(2)}
        viewForLayout.etRestPlace.setOnClickListener{showMapActivity(3)}
        viewForLayout.btn_createProfile.setOnClickListener {createMemberApiCall()}
        return viewForLayout
    }

    fun createMemberApiCall() {
        when (validate()) {
            0 -> {
                showConfirmDialog()
            }
            1 -> {
                btn_createProfile.snack("Please enter name")
            }

            2 -> {
                btn_createProfile.snack("Please enter related as")
            }
            3 -> {
                btn_createProfile.snack("please select birth date")
            }
            4 -> {
                btn_createProfile.snack("Please select death date")
            }
            5 -> {
                btn_createProfile.snack("Please enter birth place")
            }
            6 -> {
                btn_createProfile.snack("Please death place")
            }
            7 -> {
                btn_createProfile.snack("Please resting place")
            }
            8 -> {
                btn_createProfile.snack("Please enter biography")
            }
            9 -> {
                btn_createProfile.snack("Please select profile image")
            }
        }
    }

    companion object {
        fun create(): CreateMemberFragment {
            return CreateMemberFragment()
        }
    }


    fun observeRelationResponse(owner: LifecycleOwner) {
        getRelationViewModel.relationResponse.observe(owner, Observer { response ->
            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            if (response.body()!!.response.size > 0) {
                                spinnerAdapter.setList(requireContext(), response.body()!!.response)
                                spinnerAdapter.notifyDataSetChanged()
                            } else {
                                showDialogConfirmation(
                                    requireContext(),
                                    "user alert",
                                    "unable to fetch relation",
                                    false
                                ) { s: String, dialogInterface: DialogInterface -> if (s.equals("positive", true)) activity?.finish() }
                            }
                        }
                        false -> {
                            showDialogConfirmation(
                                requireContext(), "user alert", response.message(), false
                            ) { s: String, dialogInterface: DialogInterface -> if (s.equals("positive", true)) activity?.finish() }
                        }
                    }
                }
                false -> {


                    showDialogConfirmation(
                        requireContext(), "Network error", "Unable to fetch data", false
                    ) { s: String, dialogInterface: DialogInterface -> if (s.equals("positive", true)) activity?.finish() }
                }
            }
        })

    }

    fun observeUploadImageResponse(owner :LifecycleOwner){

        uploadImageViewModel.uploadImageResponse.observe(owner, Observer {response->
            Log.d("Shiva","response"+response)
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                          Log.d("Shiva","response"+response.body())
                            createMember(response.body()!!.response)
                        }
                        false -> {

                          hideProgreeDialog()
                            Toast.makeText(requireContext(),"Unable to create profile",Toast.LENGTH_LONG).show()
                        }
                    }
                }
                false -> {

                    hideProgreeDialog()
                    Toast.makeText(requireContext(),"Unable to create profile",Toast.LENGTH_LONG).show()

                }
            }
        })
    }

    fun observeCreateMember(owner:LifecycleOwner){

        createMemberViewModel.getResponse.observe(owner, Observer {response->
            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            activity?.finish()
                        }
                        false -> {


                            Toast.makeText(requireContext(),"Unable to create profile",Toast.LENGTH_LONG).show()
                        }
                    }
                }
                false -> {

                    hideProgreeDialog()
                    Toast.makeText(requireContext(),"Unable to create profile",Toast.LENGTH_LONG).show()

                }
            }

        })
    }

    fun createMember(img_url:String){
        var relation: Relation = viewForLayout.spinner.selectedItem as Relation
        var params =HashMap<String,String>()
        params["user_id"] =user.id
        params["name"]=etName.text.toString()
        params["born_place"]=etBirthPlace.text.toString()
        params["born_lat"]=bornLatitude
        params["born_lon"]=bornLongitude
        params["death_place"]=etDeathPlace.text.toString()
        params["death_lat"]=diedLatitude
        params["death_lon"]=diedLongitude
        params["born_date"]= etBorn.text.toString()
        params["death_date"]=etDied.text.toString()
        params["relation"]= relation.id.toString()
        params["relation_name"]=etRelationSub.text.toString()
        params["resting_in"]= etRestPlace.text.toString()
        params["resting_in_lat"]= restingLatitude
        params["resting_in_lon"]= restingLongitude
        selectedOption = viewForLayout.groupradio!!.checkedRadioButtonId
        if(selectedOption>-1) {
            Log.d("Shiva", "selected ID" + selectedOption)
            var radioButton: RadioButton = viewForLayout.findViewById(selectedOption)
            if (radioButton.text.toString().equals("private", true))
            { params["private_member"]= "1" }
            else if (radioButton.text.toString().equals("public", true))
            { params["private_member"]= "0"  }
        }

        params["img_url"] = img_url
        params["bio"]= etBio.text.toString()
        createMemberViewModel.getCreateMemberResponse(params)
    }

    fun validate ():Int
    {
        if (etName.text.length == 0) {
            return 1
        } else if (etRelationSub.text.length == 0) {
            return 2
        } else if (etBorn.text.length == 0) {
            return 3
        }
        else if (etDied.text.length == 0) {
            return 4
        }
        else if (etBirthPlace.text.length == 0) {
            return 5
        }
        else if (etDeathPlace.text.length == 0) {
            return 6
        }
        else if (etRestPlace.text.length == 0) {
            return 7
        }else if (etBio.text.length == 0) {
            return 8
        }
        else if(imageFile==null)
        {
            return 9
        }
        return 0
    }


    fun initialiseCalenderPickeBorn() {
        cal = Calendar.getInstance()
        dateSetListenerBorn =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "MM/dd/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                viewForLayout.etBorn.setText(sdf.format(cal.time).toString())
            }


    }
    fun initialiseCalenderPickerDied() {
        cal = Calendar.getInstance()
        dateSetListenerDied =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "MM/dd/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                viewForLayout.etDied.setText(sdf.format(cal.time).toString())
            }


    }

    fun showMapActivity(type:Int)
    {
        var intent = Intent(activity,MapActivity::class.java)
        Log.d("shiva",type.toString())
        intent.putExtra("type",type)
        getAddress.launch(intent)
    }

    fun showConfirmDialog()
    {
        var confirm  = Dialog(requireContext())
        confirm.setContentView(R.layout.member_confirmation_dialog)
        val window: Window? = confirm.getWindow()

        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );
        window?.setBackgroundDrawableResource(android.R.color.transparent);
        var relation: Relation = viewForLayout.spinner.selectedItem as Relation
        confirm.tv_name_con.setText(etName.text)
        confirm.tv_relation_con.setText(relation.name)
        confirm.tv_relation_as_con.setText(etRelationSub.text)
        confirm.tv_born_con.setText(etBorn.text)
        confirm.tv_died_con.setText(etDied.text)
        confirm.tv_birthplace_con.setText(etBirthPlace.text)
        confirm.tv_deathplace_con.setText(etDeathPlace.text)
        confirm.tv_restingplace_con.setText(etRestPlace.text)
        confirm.tv_bio_con.setText(etBio.text)
        selectedOption = viewForLayout.groupradio!!.checkedRadioButtonId
        if(selectedOption>-1) {
            Log.d("Shiva", "selected ID" + selectedOption)
            var radioButton: RadioButton = viewForLayout.findViewById(selectedOption)
            confirm.tv_type_con.setText(radioButton.text.toString())
        }
        confirm.btn_confirm.setOnClickListener {
            confirm.dismiss()
            showProgressDialog(requireContext(),"Creating member..")
            uploadImage(imageFile,uploadImageViewModel)
        }
        confirm.show()
    }
}