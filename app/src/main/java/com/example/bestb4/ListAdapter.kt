package com.example.bestb4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bestb4.data.Events.ClickEvent
import com.example.bestb4.data.ListItem
import kotlinx.android.synthetic.main.list_item.view.*
import org.greenrobot.eventbus.EventBus

// Guide brugt til implementering: https://www.youtube.com/watch?v=afl_i6uvvU0
class ListAdapter(private val exampleList: List<ListItem>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,
        parent, false)

        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = exampleList[position]

        // Skal ændres for at indsætte billeder fra kamera?
        holder.imageView.setImageResource(currentItem.imageResource)

        holder.textView1.text = currentItem.text1
        holder.textView2.text = currentItem.text2
    }

    override fun getItemCount() = exampleList.size

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val imageView: ImageView = itemView.list_item_image_view
        val textView1: TextView = itemView.list_item_text_view_1
        val textView2: TextView = itemView.list_item_text_view_2

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                val clickEvent: ClickEvent = ClickEvent(position)
                EventBus.getDefault().post(clickEvent)
            }
        }
    }

}