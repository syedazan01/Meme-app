package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    ProgressBar progressBar;
    String currentImageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.MemeImageView);
        progressBar = findViewById(R.id.progressBar);
        loadMeme();
    }

    private void loadMeme() {
        // Instantiate the RequestQueue.
        //load progress bar
        progressBar.setVisibility(View.VISIBLE);
        currentImageUrl = "https://meme-api.herokuapp.com/gimme";

        // Request a string response from the provided URL.
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, currentImageUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String url = response.getString("url");
                            Glide.with(MainActivity.this).load(url).listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e,
                                                            Object model, Target<Drawable> target,
                                                            boolean isFirstResource) {
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model,
                                                               Target<Drawable> target,
                                                               DataSource dataSource,
                                                               boolean isFirstResource) {
                                    //stop progress bar
                                    progressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(imageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Add the request to the RequestQueue.
       MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void shareMeme(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this cool meme i got form Reddit "+currentImageUrl);
        startActivity(intent.createChooser(intent, "Share this meme using ...."));

    }

    public void nextMeme(View view) {
        loadMeme();
    }
}