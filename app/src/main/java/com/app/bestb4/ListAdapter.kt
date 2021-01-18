package com.app.bestb4

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.bestb4.data.ListItem
import com.app.bestb4.data.events.ClickEvent
import kotlinx.android.synthetic.main.list_item.view.*
import org.greenrobot.eventbus.EventBus
import java.io.ByteArrayOutputStream
import java.lang.Exception

// Guide brugt til implementering: https://www.youtube.com/watch?v=afl_i6uvvU0
class ListAdapter(private val exampleList: List<ListItem>) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
            parent, false
        )

        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = exampleList[position]
        holder.textView1.text = currentItem.name
        val context = holder.background.context


        val bitmap = decodeByteArray(currentItem.thumbnailByteArray)
        holder.imageView.setImageBitmap(bitmap)

        when {
            currentItem.daysLeft < -1 -> {
                holder.textView2.text =
                    "Expired by${(currentItem.daysLeft) * (-1)} daYS."
//                    holder.background.setBackgroundColor(Color.parseColor("#CF3700"))
                holder.background.setBackgroundResource(
                    context.resources.getIdentifier(
                        "gradient_expired",
                        "drawable",
                        context?.packageName
                    )
                )
            }
            currentItem.daysLeft == -1 -> {
                holder.textView2.text =
                    "Expired by ${(currentItem.daysLeft) * (-1)} day."
//                    holder.background.setBackgroundColor(Color.parseColor("#CF6E00"))
                holder.background.setBackgroundResource(
                    context.resources.getIdentifier(
                        "gradient_expired",
                        "drawable",
                        context?.packageName
                    )
                )
            }
            currentItem.daysLeft == 0 -> {
                holder.textView2.text = "Expires Today."
//                    holder.background.setBackgroundColor(Color.parseColor("#DBD000"))
                holder.background.setBackgroundResource(
                    context.resources.getIdentifier(
                        "gradient_zero",
                        "drawable",
                        context?.packageName
                    )
                )
            }
            currentItem.daysLeft == 1 -> {
                holder.textView2.text = "Expires in ${currentItem.daysLeft} day."
                holder.background.setBackgroundResource(
                    context.resources.getIdentifier(
                        "gradient_close",
                        "drawable",
                        context?.packageName
                    )
                )
            }
            else -> {
                holder.textView2.text = "Expires in ${currentItem.daysLeft} days."
//                    holder.background.setBackgroundColor(Color.parseColor("#8BDB00"))
                holder.background.setBackgroundResource(
                    context.resources.getIdentifier(
                        "gradient_fresh",
                        "drawable",
                        context?.packageName
                    )
                )
            }
        }
    }

    override fun getItemCount() = exampleList.size


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.list_item_image_view
        val textView1: TextView = itemView.list_item_text_view_1
        val textView2: TextView = itemView.list_item_text_view_2
        val background: RelativeLayout = itemView.item_layout

        init {
            imageView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickEvent: ClickEvent = ClickEvent(position)
                EventBus.getDefault().post(clickEvent)
            }
        }
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun convertUriToBitmap(imageUri: Uri, context: Context): Bitmap? {

        try {
            val bitmap = compressBitmap(MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri), 50)
            return rotateImage(bitmap, 90F)
        } catch (e: Exception) {
            Log.e("ListAdapter", "Error bitmap")
        }
        return null
    }

    private fun compressBitmap(bitmap:Bitmap, quality:Int):Bitmap{
        val stream = ByteArrayOutputStream()

//            **** reference source developer.android.com ***
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)

        val byteArray = stream.toByteArray()

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun decodeByteArray(byteArray: ByteArray):Bitmap{
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}