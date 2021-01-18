package com.app.bestb4

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.app.bestb4.data.ListItem
import com.app.bestb4.data.events.EditItemEvent
import com.app.bestb4.data.events.ItemEvent
import com.app.bestb4.data.events.PhotoEvent
import com.app.bestb4.data.events.UpdateItemEvent
import com.app.bestb4.room.AppDatabase
import com.app.bestb4.room.DatabaseBuilder
import kotlinx.android.synthetic.main.activity_create_item.*
import kotlinx.android.synthetic.main.activity_edit_item_activity.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class EditItemActivity : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var animationView: LottieAnimationView
    private lateinit var db: AppDatabase
    private lateinit var nameEditText: EditText
    private lateinit var expirationEditText: EditText
    private lateinit var listItem: ListItem
    private var positionInList: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item_activity)

        db = DatabaseBuilder.get(this)

        nameEditText = findViewById(R.id.edit_item_name_edit_text)
        expirationEditText = findViewById(R.id.edit_item_expiration_edit_text)

        image = findViewById(R.id.edit_item_image_preview)
        image.visibility = View.GONE
        animationView = findViewById(R.id.edit_item_loading_animation)
        animationView.visibility = View.VISIBLE

        edit_item_confirm_btn.setOnClickListener {
            // Opret nyt item
            if (edit_item_name_edit_text.text.isNullOrEmpty()){
                Toast.makeText(this, "Name of item cannot be blank.", Toast.LENGTH_SHORT).show()
            }
            else if (edit_item_expiration_edit_text.text.isNullOrEmpty()){
                Toast.makeText(this, "Days left until expiration cannot be left blank.", Toast.LENGTH_SHORT).show()
            } else {
                updateList()
                finish()
            }
        }
        edit_item_cancel_btn.setOnClickListener {
            finish()
        }

    }

    private fun updateList(){
        image.visibility = View.GONE
        animationView.visibility = View.VISIBLE
        var listItemToUpdate = listItem
        val name: String = nameEditText.text.toString()
        val expiration = expirationEditText.text.toString().toInt()
        var updatedListItem = ListItem(
            listItemToUpdate.date.time,
            name,
            expiration,
            listItemToUpdate.uri,
            listItemToUpdate.date,
            expiration,
            listItemToUpdate.filePath,
            listItemToUpdate.thumbnailByteArray)

        GlobalScope.launch {
            db.listItemDao().update(updatedListItem)
        }

        val event: UpdateItemEvent = UpdateItemEvent(updatedListItem, positionInList)
        EventBus.getDefault().postSticky(event)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEditItemEvent(editEvent: EditItemEvent){
        listItem = editEvent.item
        positionInList = editEvent.positionInList
        image.setImageBitmap(convertUriToBitmap(listItem.uri))
        animationView.visibility = View.GONE
        image.visibility = View.VISIBLE
        nameEditText.setText(listItem.name)
        expirationEditText.setText(listItem.expiration.toString())

        EventBus.getDefault().removeStickyEvent(editEvent)
    }


    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }


    private fun convertUriToBitmap(imageUri: Uri): Bitmap {

        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            return rotateImage(bitmap, 90F)
        } catch (e: java.lang.Exception) {
            Log.e("ListAdapter", "Error bitmap")
        }
        return BitmapFactory.decodeResource(resources, R.drawable.diskette)
    }
}