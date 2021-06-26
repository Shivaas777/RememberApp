package com.omegamark.remember.ui.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.omegamark.remember.R
import com.omegamark.remember.adapters.StateListAdapter
import com.omegamark.remember.ui.login.LoginActivity
import com.omegamark.remember.utility.*
import com.omegamark.remember.viewmodels.members.UploadImageViewModel
import com.omegamark.remember.viewmodels.members.UploadImageViewModelFactory
import com.omegamark.remember.viewmodels.profile.ProfileUpdateViewModel
import com.omegamark.remember.viewmodels.profile.ProfileUpdateViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.remember.api.models.registration.User
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.edit_address_dialog.*
import kotlinx.android.synthetic.main.edit_profile_dialog.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ProfileFragment : Fragment() {

private lateinit var viewForlayout :View
private lateinit var user :User
private lateinit var sharedPref: SharedPref
private lateinit var profile_update_dialog: Dialog
private lateinit var address_update_dialog: Dialog
private lateinit var states:ArrayList<String>
    private lateinit var apiRepository: APIRepository
    private lateinit var uploadImageViewModelFactory: UploadImageViewModelFactory
    private lateinit var uploadImageViewModel : UploadImageViewModel
    private lateinit var updateViewModel: ProfileUpdateViewModel
    private lateinit var updateViewModelFactory: ProfileUpdateViewModelFactory
lateinit var countryJson: String
var imageFile : File?=null
lateinit var cal: Calendar
    var selectedOption: Int=0
lateinit var dateSetListenerBorn: DatePickerDialog.OnDateSetListener
    private val requestPermission= registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ result-> if( (result[android.Manifest.permission.READ_EXTERNAL_STORAGE] == true) && ((result[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == true)))
    {

        pickGalleryImage(launchGal)
    }
    else {
        Toast.makeText(requireContext(), "Storage access permission denied", Toast.LENGTH_LONG).show()}
    }
    private val launchGal = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data!!


            try {
                val contentResolver = requireContext().contentResolver
                val inputStream : InputStream? = contentResolver.openInputStream(uri)
                var selected_img : Bitmap = BitmapFactory.decodeStream(inputStream);
                lifecycleScope.launch { imageFile= bitmapToFile(
                    selected_img,
                    "profilepic.jpeg",
                    requireContext()
                ) }

                Log.d("shiva", "selected image" + selected_img.width + ":" + selected_img.height)
                Log.d("shiva", "File_name" + imageFile?.absoluteFile.toString())
                profile_update_dialog.user_image.setImageBitmap(
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForlayout= inflater.inflate(R.layout.fragment_profile, container, false)
        apiRepository= APIRepository()
        uploadImageViewModelFactory= UploadImageViewModelFactory(apiRepository)
        uploadImageViewModel= ViewModelProvider(this, uploadImageViewModelFactory).get(
            UploadImageViewModel::class.java
        )
        updateViewModelFactory=ProfileUpdateViewModelFactory(apiRepository)
        updateViewModel=ViewModelProvider(this, updateViewModelFactory).get(ProfileUpdateViewModel::class.java)
        sharedPref= SharedPref(requireContext())

        observeUploadImageResponse(this)
        observeUpdateProfileResponse(this)
        viewForlayout.img_profile_edit.setOnClickListener { showProfileUpdateDialog() }
        viewForlayout.img_add_address.setOnClickListener { showAddressUpdateDialog() }
        viewForlayout.img_profile_edit_address.setOnClickListener { showAddressUpdateDialog() }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            countryJson=loadJSONFromAsset(requireContext())
            withContext(Dispatchers.Main) {
                states = getStateCityList(requireContext(), countryJson, true, "")
                Log.d("shiva", "statelist" + states.toString())
            }
        }
        viewForlayout.cardViewLogout.setOnClickListener {

            showDialogConfirmation(requireContext(),
            "LOG OUT?",
            "Are you sure you want to log out ?",
            true,{s,dialog-> logout()
            dialog.dismiss()})

        }
        return viewForlayout
    }

    private fun logout() {
         sharedPref.clear()
        var intent = Intent(activity,LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun setUI(user: User) {

        viewForlayout.tvUsername.text=user.name
        viewForlayout.tvEmail.text=user.email
        viewForlayout.tvMobileNo.text=user.mobile
        viewForlayout.tvDob.text=user.dob
        if (user.gender==1) { viewForlayout.tvGender.text="Male" }
        else if (user.gender==2) { viewForlayout.tvGender.text="Female" }
        else if (user.gender==3) { viewForlayout.tvGender.text="Others" }
        if(user.profile_image.length>0) {
            viewForlayout.imageView_profile.loadImage(
                com.remember.api.network.Url.url + user.profile_image,
                requireContext()
            )
        }
        if(user.street.length>0)
        {
            viewForlayout.cardViewAdd_address.visibility= View.GONE
            viewForlayout.cardViewEditAddress.visibility=View.VISIBLE
            viewForlayout.tvStreetAddress.text= user.street
            viewForlayout.tvAddressCity.text= user.city
            viewForlayout.tvAddressState.text= user.state
            viewForlayout.tvAddressCountry.text=user.country
            viewForlayout.tvAddressZipcode.text= user.zipcode

        }
        else {
            viewForlayout.cardViewAdd_address.visibility= View.VISIBLE
            viewForlayout.cardViewEditAddress.visibility=View.GONE
        }
    }

    private fun showProfileUpdateDialog(){
        initialiseCalenderPickeBorn()
        profile_update_dialog = Dialog(requireContext())
        profile_update_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        profile_update_dialog.setCancelable(true)
        profile_update_dialog.setContentView(R.layout.edit_profile_dialog)
        val window: Window? = profile_update_dialog.getWindow()
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );
        window?.setBackgroundDrawableResource(android.R.color.transparent);
        profile_update_dialog.etEmail_edit.setText(user.email)
        profile_update_dialog.etBorn.setText(user.dob)
        if(user.gender==1)
        {
            profile_update_dialog.radia_id1.isChecked=true
        }
        if(user.gender==2)
        {
            profile_update_dialog.radia_id2.isChecked=true
        }
        if(user.gender==3)
        {
            profile_update_dialog.radia_id3.isChecked=true
        }
        profile_update_dialog.user_image.setOnClickListener {

            requestMultiplePermission(
                requestPermission, arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
         }
        profile_update_dialog.etBorn.setOnClickListener{
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

    profile_update_dialog.btn_updateProfileUser.setOnClickListener {
             selectedOption = profile_update_dialog.groupradio!!.checkedRadioButtonId

        showProgressDialog(requireContext(),"updating user..")
        if(imageFile!=null)
        {
            Log.d("Shiva", "Upload started")

            uploadImage(imageFile, uploadImageViewModel)
        }
        else {

            updateUser(user)}
        }
       //
        profile_update_dialog.show()
        profile_update_dialog.setOnDismissListener{
            imageFile=null
        }
    }

    fun observeUploadImageResponse(owner: LifecycleOwner){

        uploadImageViewModel.uploadImageResponse.observe(owner, Observer { response ->
            Log.d("Shiva", "response" + response)
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            Log.d("Shiva", "response" + response.body())
                            user.profile_image = response.body()!!.response
                            updateUser(user)
                        }
                        false -> {

                            hideProgreeDialog()
                            Toast.makeText(
                                requireContext(),
                                "Unable to update profile",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
                false -> {

                    hideProgreeDialog()
                    Toast.makeText(requireContext(), "Unable to update profile", Toast.LENGTH_LONG)
                        .show()

                }
            }
        })
    }

    private fun updateUser(user: User) {

        if(profile_update_dialog.etEmail_edit.text.length>0){
            user.email=profile_update_dialog.etEmail_edit.text.toString()
        }
        if(profile_update_dialog.etBorn.text.length>0){
            user.dob=profile_update_dialog.etBorn.text.toString()
        }
        if(selectedOption>-1) {
            Log.d("Shiva", "selected ID" + selectedOption)
            var radioButton: RadioButton = profile_update_dialog.findViewById(selectedOption)
            if (radioButton.text.toString().equals("male", true))
            { user.gender = 1 }
            else if (radioButton.text.toString().equals("female", true))
            { user.gender = 2 }
           else if (radioButton.text.toString().equals("others", true))
            { user.gender = 3 }
        }
        val json = Gson().toJson(user)
        val request: HashMap<String, String> = Gson().fromJson(json, object : TypeToken<HashMap<String?, String?>?>() {}.type)
        updateViewModel.updateProfile(request)
    }



    private fun observeUpdateProfileResponse(owner: LifecycleOwner)
    {
        updateViewModel.getResponse.observe(owner, Observer {response->
            hideProgreeDialog()
            when (response.isSuccessful) {
                true -> {
                    when (response.body()?.isSuccess) {
                        true -> {
                            Toast.makeText(requireContext(),"Successfully updated", Toast.LENGTH_LONG).show()
                            sharedPref.put(response.body()?.response,"user")
                            setUser()
                           if(this::profile_update_dialog.isInitialized){
                               if(profile_update_dialog.isShowing)
                               profile_update_dialog.dismiss()
                           }else {
                               if(this::address_update_dialog.isInitialized){
                               if(address_update_dialog.isShowing)
                                   address_update_dialog.dismiss()
                           }
                           }


                        }
                        false -> {


                            Toast.makeText(requireContext(),"Unable to update", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                false -> {

                    hideProgreeDialog()
                    Toast.makeText(requireContext(),"Unable to update", Toast.LENGTH_LONG).show()

                }
            }
        })
    }

    private fun showAddressUpdateDialog(){
        lateinit var stateAdapter: StateListAdapter
        lateinit var cityAdapter: StateListAdapter
        lateinit var city :ArrayList<String>
        address_update_dialog = Dialog(requireContext())
        address_update_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        address_update_dialog.setCancelable(true)
        address_update_dialog.setContentView(R.layout.edit_address_dialog)
        city=ArrayList()
        cityAdapter= StateListAdapter(requireContext(), city)
        stateAdapter = StateListAdapter(requireContext(), states)

        address_update_dialog.etStreet_edit.setText(user.street)
        address_update_dialog.etPin_edit.setText(user.zipcode)
        address_update_dialog.spinner_state.adapter = stateAdapter
        if(user.state.length>0)
        {
           var index :Int= states.indexOf(user.state)
            address_update_dialog.spinner_state.setSelection(index)
        }
        else {
            address_update_dialog.spinner_state.setSelection(0)
        }
        address_update_dialog.spinner_city.adapter=cityAdapter

        address_update_dialog.spinner_state?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                city= getStateCityList(requireContext(), countryJson, false, selectedItem)
                cityAdapter.setList(requireContext(), city)
                cityAdapter.notifyDataSetChanged()
            }

        }
        Handler().postDelayed({
            if(user.city.length>0)
            {

                var index :Int= city.indexOf(user.city)
                Log.d("shiva","updated" +index)
                address_update_dialog.spinner_city.setSelection(index)
            }
            else {
                address_update_dialog.spinner_city.setSelection(0)
            }
        }, 1000)

        val window: Window? = address_update_dialog.getWindow()
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        );
        window?.setBackgroundDrawableResource(android.R.color.transparent);

        address_update_dialog.btn_updateAddress.setOnClickListener {

            showProgressDialog(requireContext(),"update address..")
            updateAddress(user)
        }

        address_update_dialog.show()
    }

    private fun updateAddress(user: User) {

        if(address_update_dialog.etStreet_edit.text.length>0){
            user.street=address_update_dialog.etStreet_edit.text.toString() }
        else {Toast.makeText(requireContext(),"Street cannot be empty",Toast.LENGTH_SHORT).show()}
            user.country=address_update_dialog.etCountry_edit.text.toString()
        if(address_update_dialog.etPin_edit.text.length>0){user.zipcode =address_update_dialog.etPin_edit.text.toString() }
        else {Toast.makeText(requireContext(),"Zipcode cannot be empty",Toast.LENGTH_SHORT).show()}
       user.state = address_update_dialog.spinner_state.selectedItem as String
        user.city = address_update_dialog.spinner_city.selectedItem as String
        val json = Gson().toJson(user)
        val request: HashMap<String, String> = Gson().fromJson(json, object : TypeToken<HashMap<String?, String?>?>() {}.type)
        updateViewModel.updateProfile(request)
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
                profile_update_dialog.etBorn.setText(sdf.format(cal.time).toString())
            }


    }

    override fun onResume() {
        super.onResume()
        Log.d("shiva","OnResume called")
        setUser()


    }

    private fun setUser()
    {
        user = sharedPref.get<User>("user")!!
        setUI(user)
    }

}