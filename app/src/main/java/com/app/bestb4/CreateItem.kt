package com.app.bestb4

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.app.bestb4.data.ListItem
import com.app.bestb4.data.events.ItemEvent
import com.app.bestb4.data.events.PhotoEvent
import com.app.bestb4.room.AppDatabase
import com.app.bestb4.room.DatabaseBuilder
import kotlinx.android.synthetic.main.activity_create_item.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream
import java.util.*


class CreateItem : AppCompatActivity() {

    private lateinit var bitmap: Bitmap
    private lateinit var image: ImageView
    private lateinit var animationView: LottieAnimationView
    private lateinit var date: Date
    private lateinit var db: AppDatabase
    private lateinit var imageUri: Uri
    private lateinit var filePath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)

        db = DatabaseBuilder.get(this)

        image = findViewById(R.id.create_item_image_preview)
        image.visibility = View.GONE
        animationView = findViewById(R.id.create_item_loading_animation)
        animationView.visibility = View.VISIBLE


        create_item_confirm_btn.setOnClickListener {
            // Opret nyt item
            if (item_name_edit_text.text.isNullOrEmpty()){
                Toast.makeText(this, "Indtast navn på vare.", Toast.LENGTH_SHORT).show()
            }
            else if (item_expiration_edit_text.text.isNullOrEmpty()){
                Toast.makeText(this, "Indtast antal dage holdbar efter åbning.", Toast.LENGTH_SHORT).show()
            } else {
                addToList()
                finish()
            }
        }
        create_item_cancel_btn.setOnClickListener {
            finish()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onPhotoEvent(photoEvent: PhotoEvent){
        date = photoEvent.date
        imageUri = photoEvent.imageUri
        filePath = photoEvent.filePath
        bitmap = convertUriToBitmap(imageUri)
        image.setImageBitmap(bitmap)
        animationView.visibility = View.GONE
        image.visibility = View.VISIBLE
        EventBus.getDefault().removeStickyEvent(photoEvent)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun addToList(){
        image.visibility = View.GONE
        animationView.visibility = View.VISIBLE
        val name: String = item_name_edit_text.text.toString()
        val expiration = item_expiration_edit_text.text.toString().toInt()
        val thumbnail = convertBitmapToByteArray(createThumbnail(bitmap))
        var listItem = ListItem(date.time, name, expiration, imageUri, date, expiration, filePath, thumbnail)

        GlobalScope.launch {
            db.listItemDao().insert(listItem)
        }

        val event: ItemEvent = ItemEvent(listItem)
        EventBus.getDefault().postSticky(event)

        val intent = Intent(this@CreateItem, MainFragmentActivity::class.java)
        startActivity(intent)
    }

    private fun createThumbnail(bitmap: Bitmap) : Bitmap {
        val thumbnailSize = 320
        return ThumbnailUtils.extractThumbnail(bitmap, thumbnailSize, thumbnailSize)
    }

    private fun convertBitmapToByteArray(bitmap: Bitmap) : ByteArray {
        lateinit var byteArray: ByteArray
        try {
            var baos : ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            byteArray = baos.toByteArray()
        } catch (e: Exception){
            e.printStackTrace()
        }
        return byteArray
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