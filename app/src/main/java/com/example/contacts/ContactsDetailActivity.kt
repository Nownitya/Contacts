package com.example.contacts

import android.content.ContentProviderOperation
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.contacts.databinding.ActivityContactsDetailBinding

class ContactsDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsDetailBinding
    private lateinit var contactId: String
    private val contentValues =  ContentValues()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val name = intent.getStringExtra("contactName")
        contactId = intent.getStringExtra("contactId").toString()
        val number = intent.getStringExtra("contactNumber")
        val email = intent.getStringExtra("contactEmail")
//        val color = intent.getIntExtra("color",titleColor)
//        val initialName = name?.get(0)?.toString()
//        Toast.makeText(this@ContactsDetailActivity, contactId, Toast.LENGTH_SHORT).show()
//        Toast.makeText(this@ContactsDetailActivity, name, Toast.LENGTH_SHORT).show()
//        Toast.makeText(this@ContactsDetailActivity, number, Toast.LENGTH_SHORT).show()

        binding.nameDetail.setText(name)
//        binding.initialIV.text = initialName
//        binding.initialIV.setBackgroundColor(color)
        binding.phNumDetail.setText(number)
//        binding.updateBT.setBackgroundColor(color)
        binding.emailDetail.setText(email)



//        Log.d(TAG, "$contactId - $name - $number - $initialName")
        Log.d(TAG, "$contactId - $name - $number - $email ")

        binding.updateBT.setOnClickListener {
            updateContact(contactId)
//            val intent = Intent(this@ContactsDetailActivity, MainActivity::class.java)
//            startActivity(intent)
            super.onBackPressed()
//            onBackPressed()
            val intent = Intent()
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            this@ContactsDetailActivity.finishAffinity()
            this@ContactsDetailActivity.finish()


        }


    }

    //    override fun onBackPressed() {
//        super.onBackPressed()
//        Intent().flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
//    }
    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete -> {
//                Toast.makeText(this, "${R.string.delete} - $contactId", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "${(R.string.delete)} - $contactId")


                deleteContact(contactId)

//                val cursor = contentResolver.delete(
//                    ContactsContract.RawContacts.CONTENT_URI,
//                    ContactsContract.Contacts.DISPLAY_NAME + " =?",
//                    arrayOf(contactId)
//                )
            }
        }


        return super.onOptionsItemSelected(item)
    }


    private fun updateContact(id: String) {
        val cpos = ArrayList<ContentProviderOperation>()

        if (binding.nameDetail != null) {
            cpos.add(
                ContentProviderOperation
                    .newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=? AND " +
                                ContactsContract.Data.MIMETYPE + "='" +
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE + "'",
                        arrayOf(
                            id
                        )
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        binding.nameDetail.text.toString()
                    )
                    .build()
            )
        }
        if (binding.phNumDetail != null) {
            cpos.add(
                ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                    .withSelection(
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
                                + ContactsContract.CommonDataKinds.Phone.MIMETYPE + " = ? ",

                        arrayOf(
                            id,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                        )
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        binding.phNumDetail.text.toString()
                    )
//                .withValue(
//                ContactsContract.CommonDataKinds.Phone.TYPE,
//                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build()
            )
        }

        try {
            var result =  contentResolver.applyBatch(ContactsContract.AUTHORITY, cpos)

            Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
            Log.d(AddContactActivity.TAG, "Updated!")
            Log.d(AddContactActivity.TAG, "Result -- ${binding.nameDetail.text.toString()}")
            Log.d(AddContactActivity.TAG, "Result -- ${binding.phNumDetail.text.toString()}")
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
        }
        catch (e:java.lang.Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Exception: ${e.message} -1", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Exception: ${e.message} -1")

        }
//        catch (e: OperationApplicationException) {
//            e.printStackTrace()
//            Toast.makeText(this, "Exception: ${e.message} -2", Toast.LENGTH_SHORT).show()
//            Log.d(TAG, "Exception: ${e.message} -2")
//        }
//        catch (e: RemoteException) {
//            e.printStackTrace()
//            Toast.makeText(this, "Exception: ${e.message} -3", Toast.LENGTH_SHORT).show()
//            Log.d(TAG, "Exception: ${e.message} -3")
//        }


    }


//    {
//        arrayOf(id,
//            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ? AND " +
//                    ContactsContract.Data.MIMETYPE + "='"
//                    + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "' AND "
//                    + ContactsContract.CommonDataKinds.Phone.TYPE + "='"
//                    + ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE + "'",)
//    }


    private fun deleteContact(id: String) {
        var deleteRows = contentResolver.delete(
            ContactsContract.RawContacts.CONTENT_URI,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
            arrayOf(id)
        )

//        val intent = Intent(this@ContactsDetailActivity, MainActivity::class.java)
        val intent = Intent()
        startActivity(intent)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        super.onBackPressed()
//        this@ContactsDetailActivity.finish()


    }


//    fun deleteContact(id: Int) {
//        val deleteRows: Int = contentResolver.delete(
//            ContactsContract.Contacts.CONTENT_URI,
//            ContactsContract.RawContacts.CONTACT_ID + " =?",
//            arrayOf(id.toString())
//        )
//        Log.d(TAG, "$deleteRows.")
//    }

    companion object {
        val TAG: String = ContactsDetailActivity::class.java.simpleName
    }
}