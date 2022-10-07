package com.example.contacts

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactRVAdapter(
    private var context: Context,
    private var contactData: ArrayList<StoreData>
    ): RecyclerView.Adapter<ContactRVAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val conName: TextView = itemView.findViewById(R.id.contactNameTV)
        val conNumber: TextView = itemView.findViewById(R.id.contactNumberTV)


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.recyclerview_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem: StoreData = contactData[position]
        holder.conName.text = currentItem.contactName
        holder.conNumber.text = currentItem.contactNumber


        holder.itemView.setOnClickListener{
            val intent = Intent(context, ContactsDetailActivity::class.java)
            intent.putExtra("contactId", currentItem.contactId)
            intent.putExtra("contactName", currentItem.contactName)
            intent.putExtra("contactNumber", currentItem.contactNumber)
//            intent.putExtra("contactEmail", currentItem.contactEmail)

//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {

        return contactData.size
    }




}