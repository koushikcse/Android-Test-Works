package com.arc.fileencryptiondecryption

import android.Manifest
import android.app.Activity
import android.app.LauncherActivity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File
import com.developer.filepicker.model.DialogConfigs
import kotlinx.android.synthetic.main.activity_main.*
import com.developer.filepicker.model.DialogProperties
import com.developer.filepicker.view.FilePickerDialog

class MainActivity : AppCompatActivity() {

    private val STORAGE_CODE = 1
    private lateinit var filename: String
    private lateinit var selectedFile: File
    private var selectedMultiFile: ArrayList<File> = ArrayList()

    lateinit var dialog: FilePickerDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val properties = DialogProperties()
        properties.selection_mode = DialogConfigs.SINGLE_MODE
        properties.selection_type = DialogConfigs.FILE_SELECT
        properties.root = File(DialogConfigs.DEFAULT_DIR)
        properties.error_dir = File(DialogConfigs.DEFAULT_DIR)
        properties.offset = File(DialogConfigs.DEFAULT_DIR)
        properties.extensions = null
        dialog = FilePickerDialog(this@MainActivity, properties)
        dialog.setTitle("Select a File")
        dialog.setDialogSelectionListener {
            //files is the array of the paths of files selected by the Application User.
//            selectedMultiFile.clear()
//            val size = it.size
//            for (path in it) {
//                val file = File(path)
//                val item = LauncherActivity.ListItem()
//                selectedMultiFile.add(file)
//            }

            selectedFile = File(it[0])
            tv_file_name.text = selectedFile.name
        }

        btSelect.setOnClickListener {
            checkPermission()
        }
        btnEncrypt.setOnClickListener {
            CryptoUtils.encrypt(
                "jXn2r5u8x/A?D(G-", selectedFile,
                createFile(tv_file_name.text.toString())
            )
//
//            for(file in selectedMultiFile){
//                CryptoUtils.encrypt(
//                    "jXn2r5u8x/A?D(G-", file,
//                    createEnFile(file.name)
//                )
//
//            }
        }
        btnDecrypt.setOnClickListener {
            CryptoUtils.decrypt(
                "jXn2r5u8x/A?D(G-",
                selectedFile,
                createFile(tv_file_name.text.toString())
            )
//            for(file in selectedMultiFile){
//                CryptoUtils.encrypt(
//                    "jXn2r5u8x/A?D(G-", file,
//                    createFile(file.name)
//                )
//
//            }
        }

        btnEncryptEn.setOnClickListener {
            CryptoUtils.encrypt(
                "jXn2r5u8x/A?D(G-", selectedFile,
                createEnFile(tv_file_name.text.toString())
            )
        }
        btnDecryptEn.setOnClickListener {
            CryptoUtils.decrypt(
                "jXn2r5u8x/A?D(G-",
                selectedFile,
                createFileRemoveEn(tv_file_name.text.toString())
            )
        }

    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_CODE
                )
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            //we have permission
            dialog.show()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            STORAGE_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    selectFileForEncryption()
                    dialog.show()

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "No permission", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    fun createFile(filename: String): File? {
        val mediaStorageDir =
            File(
                Environment.getExternalStorageDirectory(),
                "FileEncryptionDecryption"
            )

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("App", "failed to create directory")
                return null
            }
        }

        var file: File? = null
        if (mediaStorageDir.exists())
            file = File(mediaStorageDir, filename)
        return file
    }

    fun createEnFile(filename: String): File? {
        val mediaStorageDir =
            File(
                Environment.getExternalStorageDirectory(),
                "FileEncryptionDecryption"
            )

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("App", "failed to create directory")
                return null
            }
        }

        var file: File? = null
        if (mediaStorageDir.exists())
            file = File(mediaStorageDir, filename+".en")
        return file
    }

    fun createFileRemoveEn(filename: String): File? {
        var name = filename
        name = name.substring(0, name.length - 3)
        val mediaStorageDir =
            File(
                Environment.getExternalStorageDirectory(),
                "FileEncryptionDecryption"
            )

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("App", "failed to create directory")
                return null
            }
        }

        var file: File? = null
        if (mediaStorageDir.exists())
            file = File(mediaStorageDir, name)
        return file
    }

}
