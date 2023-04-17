package com.abcd.catchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


//    private var x= 0
//    val y = messageList.size
    val ITEM_RECIEVE = 1;
    val ITEM_SENT = 2;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == 1){
            // inflate recieve
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieve, parent, false)
            return RecieveViewHolder(view)
        }else{
            // inflate sent
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


        val currentMessage = messageList[position]


        if(holder.javaClass == SentViewHolder::class.java){

            //sent code
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
//            if (x<y){
//                holder.sendLayout.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,R.anim.anim_slide_left))
//                x++
//            }
        }else{

            // recieve code
            val viewHolder = holder as RecieveViewHolder
            holder.recieveMessage.text = currentMessage.message
//            if (x<y){
//                holder.recieveLayout.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context,R.anim.anim_slide_right))
//                x++
//            }
        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECIEVE
        }
    }

    override fun getItemCount(): Int {

       return messageList.size
    }



    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_mesg)
        val sendLayout =  itemView.findViewById<RelativeLayout>(R.id.sendLayout)
    }

    class RecieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val recieveMessage = itemView.findViewById<TextView>(R.id.txt_recieve_mesg)
        val recieveLayout = itemView.findViewById<RelativeLayout>(R.id.recieveLayout)

    }
}
