package com.example.contacts

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contacts.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var storeContactArrayList: ArrayList<StoreData>
    private var storeContactListD: ArrayList<StoreData>? = null

//    private var recyclerView: RecyclerView? = null
    private lateinit var contactRVAdapter: ContactRVAdapter         // changed from null to lateinit
    private var progressBar: ProgressBar? = null

    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
    private var isConnectedReadGranted = false
    private var isConnectedWriteGranted = false
    private var isConnectedCallGranted = false

    private var columns = listOf<String>(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Email.ADDRESS
    ).toTypedArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        storeContactArrayList = ArrayList<StoreData>()          //  Do not remove initializing lateinit property
        permissionsLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            isConnectedReadGranted =
                permissions[android.Manifest.permission.READ_CONTACTS] ?: isConnectedReadGranted
            isConnectedWriteGranted =
                permissions[android.Manifest.permission.WRITE_CONTACTS] ?: isConnectedWriteGranted
            isConnectedCallGranted =
                permissions[android.Manifest.permission.CALL_PHONE] ?: isConnectedCallGranted
        }
        prepareRecyclerView()
        requestPermissions()

        getContact()


        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivity(intent)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onQueryTextChange(newText: String?): Boolean {

                if (newText!!.isNotEmpty()) {
                    val storeContactListD: ArrayList<StoreData> = ArrayList<StoreData>()
//                    storeContactListD.clear()
                    val search = newText.lowercase(Locale.getDefault())
                    storeContactArrayList.forEach {
                        if (it.contactName!!.lowercase(Locale.getDefault()).contains(search)) {

                            storeContactListD.add(it)

                            Log.d(TAG, it.contactName!!.lowercase(Locale.getDefault()))
//
                        }
                    }
//                    storeContactArrayList.clear()
//                    binding.recyclerview.adapter = contactRVAdapter
                    Log.d(TAG,"111")

                    binding.recyclerview.adapter?.notifyDataSetChanged()
//                    contactRVAdapter.notifyDataSetChanged()
                } else {
                    Log.d(TAG,"222")
                    storeContactListD?.clear()
                    storeContactListD?.addAll(storeContactArrayList)
                    contactRVAdapter.notifyDataSetChanged()
                }
                return false
            }

        })
    }

    private fun prepareRecyclerView() {

        contactRVAdapter = ContactRVAdapter(this,storeContactArrayList)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = contactRVAdapter
    }

    @SuppressLint("Range")
    private fun getContact() {
        var contactId: String?
        var displayName: String?
        var contactNumber: String?
        var contactEmail: String?


        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,     // The content URI of the Contact
            columns,                                             //The columns to return for each row
            null,                                           //  Selection criteria
            null,                                        //  Selection criteria
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME     // sortOrder
        )

        if (cursor?.count!! > 0) {
            Log.d(TAG, cursor.count.toString())
            while (cursor.moveToNext()) {
//                var contact = ContactsData()

                if (cursor.moveToNext()) {
                    contactId =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                    displayName =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    contactNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    contactEmail =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))

//                    storeContactArrayList.add(StoreData(contactId, displayName, contactNumber))

                    val emails = applicationContext.contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        arrayOf(ContactsContract.CommonDataKinds.Email.DATA1),
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "+ contactId ,
                        null,
                        null
                    )
                    if (emails?.count!! > 0) {
                        while (emails.moveToNext()) {
                            if (emails.moveToNext()) {
                                val email =
                                    emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS))
                                storeContactArrayList.add(StoreData(contactId, displayName, contactNumber, email))
                                Log.d(TAG, "11223344")
                            }
                        }
                    } else {
                        storeContactArrayList.add(StoreData(contactId, displayName, contactNumber))
                        Log.d(TAG, "5566778899")
                    }

//                    val aaa = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
//                    Log.d(TAG, "${contactId} - ${displayName} - $hasPhoneNumber")

//                    storeContactArrayList.add(StoreData(contactId, displayName, contactNumber, contactEmail))
                    Log.d(TAG, "$contactId $displayName  $contactNumber $emails")
//                    Log.d(TAG, aaa)


                }
            }
        }
        cursor.close()

//        prepareRecyclerView()

//        contactRVAdapter = ContactRVAdapter(this,storeContactArrayList)
//        binding.recyclerview.layoutManager = LinearLayoutManager(this)
//        binding.recyclerview.adapter = contactRVAdapter
        progressBar?.visibility = View.GONE

    }




    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
//        storeContactArrayList.clear()
//        getContact()
        contactRVAdapter?.notifyDataSetChanged()
        Log.d(TAG, "onResume")
    }



    private fun requestPermissions() {
        isConnectedReadGranted = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        isConnectedWriteGranted = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        isConnectedCallGranted = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList()
        if (!isConnectedReadGranted) {
            permissionRequest.add(android.Manifest.permission.READ_CONTACTS)
        }
        if (!isConnectedWriteGranted) {
            permissionRequest.add(android.Manifest.permission.WRITE_CONTACTS)
        }
        if (!isConnectedCallGranted) {
            permissionRequest.add(android.Manifest.permission.CALL_PHONE)
        }
        if (permissionRequest.isNotEmpty()) {
            permissionsLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}