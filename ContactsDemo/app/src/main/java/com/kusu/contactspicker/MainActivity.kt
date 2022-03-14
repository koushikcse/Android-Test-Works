package com.kusu.contactspicker

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.google.android.material.snackbar.Snackbar
import com.kusu.contactspicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor>, TextWatcher,
    Listener {
    private lateinit var layout: View
    private lateinit var binding: ActivityMainBinding
    private val contactList = ArrayList<Contact>()
    private val originalContactList = ArrayList<Contact>()
    private var adapter: ContactAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root
        layout = binding.mainLayout
        LoaderManager.getInstance(this).initLoader(0, Bundle(), this)
        onClickRequestPermission(view)

        adapter = ContactAdapter(contactList, this)
        binding.list.adapter = adapter
        binding.search.addTextChangedListener(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    fun View.showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else {
            snackbar.show()
        }
    }

    fun onClickRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_granted),
                    Snackbar.LENGTH_LONG,
                    null
                ) {
                }
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_CONTACTS
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_LONG,
                    getString(R.string.ok)
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.READ_CONTACTS
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_CONTACTS
                )
            }
        }
    }

    @SuppressLint("InlinedApi")
    private val selectionArgs = arrayOf(
        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
    )


    @SuppressLint("InlinedApi")
    private val PROJECTION: Array<out String> = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.Data.MIMETYPE,
        ContactsContract.Contacts.PHOTO_URI
    )

    // The column index for the _ID column
    private val CONTACT_ID_INDEX: Int = 0

    // The column index for the CONTACT_KEY column
    private val CONTACT_KEY_INDEX: Int = 1

    // Defines the text expression
    @SuppressLint("InlinedApi")
    private val SELECTION: String =
        "(" + ContactsContract.Data.MIMETYPE + "=? OR " + ContactsContract.Data.MIMETYPE + "=?)"
    private val sortby = ContactsContract.Contacts.DISPLAY_NAME + " ASC"

    // Defines a variable for the search string
    private val searchString: String = ""

    // Defines the array to hold values that replace the ?

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(
            this,
            ContactsContract.Data.CONTENT_URI,  // URI
            PROJECTION,  // projection fields
            SELECTION,  // the selection criteria
            selectionArgs,  // the selection args
            sortby // the sort order
        )
    }

    @SuppressLint("Range")
    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null && data.moveToFirst()) {
            originalContactList.clear()
            val phoneList = ArrayList<String>()
            val emailList = ArrayList<String>()
            var name = ""
            var photo: String? = null
            while(data.moveToNext()){
                val displayName: String =
                    data.getString(data.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val communication: String =
                    data.getString(data.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                val id = data.getString(data.getColumnIndex(ContactsContract.Contacts._ID))
                val photouri =
                    data.getString(data.getColumnIndex(ContactsContract.Contacts.PHOTO_URI))

                Log.e("$$$$ ", "$id/ $displayName / $communication /$photouri")
                if (name == displayName) {
                    if (communication.contains('@')) {
                        if (!emailList.contains(communication))
                            emailList.add(communication)
                    } else {
                        if (!phoneList.contains(communication))
                            phoneList.add(communication)
                    }
                } else {
                    if (name == "") {
                        name = displayName
                        photo = photouri
                        if (communication.contains('@')) {
                            if (!phoneList.contains(communication))
                                emailList.add(communication)
                        } else {
                            if (!phoneList.contains(communication))
                                phoneList.add(communication)
                        }
                    } else {
                        originalContactList.add(
                            Contact(
                                name,
                                phoneList.joinToString(", "),
                                emailList.joinToString(", "),
                                photo

                            )
                        )
                        emailList.clear()
                        phoneList.clear()
                        name = displayName
                        photo = photouri
                        if (communication.contains('@')) {
                            if (!phoneList.contains(communication))
                                emailList.add(communication)
                        } else {
                            if (!phoneList.contains(communication))
                                phoneList.add(communication)
                        }
                    }
                }
            }
            if(emailList.isNotEmpty()||phoneList.isNotEmpty()){
                originalContactList.add(
                    Contact(
                        name,
                        phoneList.joinToString(", "),
                        emailList.joinToString(", "),
                        photo

                    )
                )
                emailList.clear()
                phoneList.clear()
            }
            contactList.clear()
            contactList.addAll(originalContactList)
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(p0: Editable?) {
        if (p0.isNullOrEmpty()) {
            contactList.clear()
            contactList.addAll(originalContactList)
        } else {
            contactList.clear()
            originalContactList.forEach {
                if (it.name.contains(p0.toString()))
                    contactList.add(it)
            }
        }
        adapter?.notifyDataSetChanged()
    }

    override fun onItemClick(pos: Int) {
        contactList[pos].selected = !contactList[pos].selected
        adapter?.notifyItemChanged(pos)
    }
}