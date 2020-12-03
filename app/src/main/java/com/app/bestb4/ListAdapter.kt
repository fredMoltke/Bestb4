package com.app.bestb4

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.bestb4.data.events.ClickEvent
import com.app.bestb4.data.ListItem
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
        holder.imageView.setImageBitmap(currentItem.thumbnail)


        holder.textView1.text = currentItem.name
        when {
                currentItem.daysLeft < -1 -> {
                    holder.textView2.text = "Udløbet! Holdbarhed overskredet med ${(currentItem.daysLeft)*(-1)} dage."
                    holder.background.setBackgroundColor(Color.parseColor("#CF3700"))
                }
                currentItem.daysLeft == -1 -> {
                    holder.textView2.text = "Udløbet! Holdbarhed overskredet med ${(currentItem.daysLeft)*(-1)} dag."
                    holder.background.setBackgroundColor(Color.parseColor("#CF6E00"))
                }
                currentItem.daysLeft == 0 -> {
                    holder.textView2.text = "Udløber i dag."
                    holder.background.setBackgroundColor(Color.parseColor("#DBD000"))
                }
                currentItem.daysLeft == 1 -> {
                    holder.textView2.text = "Holdbar i ${currentItem.daysLeft} dag."
                    holder.background.setBackgroundColor(Color.parseColor("#C5DB00"))
                }
                else -> {
                    holder.textView2.text = "Holdbar i ${currentItem.daysLeft} dage."
                    holder.background.setBackgroundColor(Color.parseColor("#8BDB00"))
            }
        }
    }

    override fun getItemCount() = exampleList.size


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener{
        val imageView: ImageView = itemView.list_item_image_view
        val textView1: TextView = itemView.list_item_text_view_1
        val textView2: TextView = itemView.list_item_text_view_2
        val background: RelativeLayout = itemView.item_layout

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