package com.fospe.remember.ui.members

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.fospe.remember.R
import kotlinx.android.synthetic.main.fragment_create_member.view.*
import java.io.*


class CreateMemberFragment : Fragment() {

    private lateinit var viewForLayout :View
    private lateinit var mGalleryFile :File
    /*private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data!!
                Log.d("Shiva", "URI" + uri)
                val file = ImagePicker.getFile(result.data)!!
                val filepath = getFilePath(result.data)
                Log.d("Shiva", "File path" + filepath)
                mGalleryFile = file
               // viewForLayout.user_image.setImageBitmap(BitmapFactory.decodeFile(filepath));
                val contentResolver = requireContext().contentResolver
                val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION

                uri?.let { contentResolver.takePersistableUriPermission(it, takeFlags) }
               viewForLayout.user_image.setImageURI(uri)
            }
        }*/

    private val launchCrop=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->

        Log.d("Shiva", "Cropped uri" + result.data)

    }

    private val launchGal = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data!!


            try {
                val contentResolver = requireContext().contentResolver
                val inputStream :InputStream? = contentResolver.openInputStream(uri)
                var selected_img : Bitmap = BitmapFactory.decodeStream(inputStream);
                Log.d("shiva", "selected image" + selected_img.width + ":" + selected_img.height)
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewForLayout= inflater.inflate(R.layout.fragment_create_member, container, false)

        viewForLayout.user_image.setOnClickListener{

            pickGalleryImage()
        }
        return viewForLayout
    }

    companion object {
        fun create(): CreateMemberFragment {
            return CreateMemberFragment()
        }
    }
    fun pickGalleryImage() {

        /*val activity: Activity = this.activity as CreateMemberActivity
        galleryLauncher.launch(
            ImagePicker.with(activity)
                .cropSquare()
                .galleryOnly()
                .createIntent()
        )
*/
        var intent = Intent()

        intent = Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setType("image/*");
        launchGal.launch(intent)


    }


}