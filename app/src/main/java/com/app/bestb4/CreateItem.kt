package com.app.bestb4

import android.content.Intent
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.app.bestb4.data.ListItem
import com.app.bestb4.data.events.ItemEvent
import com.app.bestb4.data.events.PhotoEvent
import com.app.bestb4.data.realmObjects.RealmListItem
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmQuery
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_create_item.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

class CreateItem : AppCompatActivity() {

    private lateinit var image: ImageView
    private lateinit var animationView: LottieAnimationView
    private lateinit var date: Date
    private lateinit var bitmap: Bitmap
    val realm by lazy { Realm.getDefaultInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)

        image = findViewById(R.id.create_item_image_preview)
        image.visibility = View.GONE
        animationView = findViewById(R.id.create_item_loading_animation)
        animationView.visibility = View.VISIBLE

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("bestb4.realm")
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)

        confirm_item_btn.setOnClickListener {
            // Opret nyt item
            addToList() //TODO: TOM FUNKTION, UDARBEJD NOGET. REALM?
        }
        cancel_item_btn.setOnClickListener {
            finish()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onPhotoEvent(photoEvent: PhotoEvent){
        date = photoEvent.date
        bitmap = photoEvent.bitmap
        image.setImageBitmap(bitmap)
        animationView.visibility = View.GONE
        image.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        // TODO: Fjern billede så gammelt billede ikke vises når man åbner CreateItem igen
        EventBus.getDefault().unregister(this)
    }

    private fun addToList(){
        // TODO: Fejlhåndtering, forkert input i edit text
        val name: String = item_name_edit_text.text.toString()
        val expiration = item_expiration_edit_text.text.toString().toInt()
        val thumbnail = createThumbnail(bitmap)
        var listItem = ListItem(date.time, name, expiration, bitmap, thumbnail, date)
        insertItemToRealm(listItem)
        val event: ItemEvent = ItemEvent(listItem)
        EventBus.getDefault().postSticky(event)

        val intent = Intent(this@CreateItem, ListActivity::class.java)
        startActivity(intent)
    }

//    name: String, expiration: Int, bitmap: Bitmap, thumbnail: Bitmap
    private fun insertItemToRealm(listItem: ListItem){
        val bitmapByteArray = convertBitmapToByteArray(listItem.bitmap)
        val thumbnailByteArray = convertBitmapToByteArray(listItem.thumbnail)
        realm.executeTransaction {
            val item: RealmListItem = realm.createObject(RealmListItem::class.java, listItem.id)
            item.name = listItem.name
            item.expiration = listItem.expiration
            item.date = listItem.date
            item.bitmapByteArray = bitmapByteArray
            item.thumbnailByteArray = thumbnailByteArray
        }
    }

    private fun getItemsFromRealm(){
        val items: RealmQuery<RealmListItem>? = realm.where(RealmListItem::class.java)
        items?.findAll()?.forEach{
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()

        }
    }

    private fun createThumbnail(bitmap: Bitmap) : Bitmap {
        val thumbnailSize = 320
        return ThumbnailUtils.extractThumbnail(bitmap, thumbnailSize, thumbnailSize)
    }

//    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//    imageData = baos.toByteArray();

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
}