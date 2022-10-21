package com.example.contacts

import android.content.ContentProviderOperation
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.contacts.databinding.ActivityAddContactBinding

class AddContactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddContactBinding
    private lateinit var storeContactArrayList: ArrayList<StoreData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddContactBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val names = binding.nameInputET.text
        val numbers = binding.numberInputET.text
        val email = binding.emailInputET.text

        binding.addContactBTN.setOnClickListener {
            super.onBackPressed()
            addContact(names.toString(), numbers.toString(), email.toString())
            Log.d(
                "AddContactActivity",
                "${binding.nameInputET.text} --- ${binding.numberInputET.text}--- ${binding.emailInputET.text}"
            )


            Toast.makeText(this, "$names --- $numbers -- $email", Toast.LENGTH_SHORT).show()
            finish()
        }



    }

    fun addContact(name: String, number: String,email: String) {
//        val storeData = StoreData( name, number, email)
        val cpo = ArrayList<ContentProviderOperation>()
        val rawId = cpo.size

        cpo.add(
            ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI
            )
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build()
        )
        //  for name
        if (name != null) {
            cpo.add(
                ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI
                )
                    .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID, rawId
                    )
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        name
                    ).build()
            )
        }
        //  for number
        if (number != null) {

            cpo.add(
                ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI
                )
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawId)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        number
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    )
                    .build()
            )
        }
        if (email != null) {

            cpo.add(
                ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI
                )
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                        email
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
                    )
                    .withYieldAllowed(true)
                    .build()
            )
        }


        //  Asking the Contact provider to create a new contact
        try {
            contentResolver.applyBatch(ContactsContract.AUTHORITY, cpo)
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()

//            super.onBackPressed()
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;



        } catch (e:java.lang.Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Exception: ${e.message}")
        }






    }

    companion object {
        val TAG = AddContactActivity::class.java.simpleName
    }
}