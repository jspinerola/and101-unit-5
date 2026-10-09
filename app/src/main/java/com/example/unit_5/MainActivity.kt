package com.example.unit_5

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var photoImageView: ImageView

    private lateinit var authorTextView: TextView
    private lateinit var urlTextView: TextView
    private lateinit var fetchButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Initialize Views
        photoImageView = findViewById(R.id.petImage)
        authorTextView = findViewById(R.id.authorTextView)
        urlTextView = findViewById(R.id.urlTextView)
        fetchButton = findViewById(R.id.fetchButton)

        // 2. Initial API call
        fetchPicture()

        // 3. Attach click listener to request a new entry
        fetchButton.setOnClickListener {
            fetchPicture()
        }
    }

    private fun fetchPicture(){
        val client = AsyncHttpClient()

        val randomPage = Random.nextInt(1, 100)
        val endpoint = "https://picsum.photos/v2/list?page=$randomPage&limit=1"

        client.get(endpoint, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                try
                {
                    // Picsum /v2/list returns a JSON array: [{ "id": "...", "author": "...", "url": "...", "download_url": "..." }]
                    val firstItem = json.jsonArray.getJSONObject(0)
                    // 1. Image URL (download_url)
                    val imageUrl = firstItem.getString("download_url")

                    // 2. Author field
                    val author = firstItem.getString("author")

                    // 3. Web page URL field
                    val photoUrl = firstItem.getString("url")

                    // Update UI components
                    authorTextView.text = "Author: $author"
                    urlTextView.text = "URL: $photoUrl"

                    // Load image into ImageView using Glide
                    Glide.with(this@MainActivity)
                        .load(imageUrl)
                        .fitCenter()
                        .into(photoImageView)

                } catch (e: Exception) {
                    Log.e("PicsumAPI", "Error parsing JSON: ${e.message}")
                    }
                }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String?, throwable: Throwable?) {
                Log.e("PicsumAPI", "Request failed with code $statusCode: $errorResponse")
            }
            }
        )

    }
}