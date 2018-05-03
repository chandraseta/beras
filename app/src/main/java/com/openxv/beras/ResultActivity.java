package com.openxv.beras;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ResultActivity extends AppCompatActivity {
    private String selectedPath;
    private ImageView selectedImage;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Intent intent = getIntent();
        selectedPath = intent.getStringExtra("image");

        statusText = (TextView) findViewById(R.id.result_text);
        selectedImage = (ImageView) findViewById(R.id.selected_image);
        Glide.with(this).load(selectedPath).into(selectedImage);

        statusText.setText(new File(selectedPath).getName());
        //classifyImage();
    }

    public void classifyImage() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            statusText.setText("Mengklasifikasikan");

            ImageSender imageSender = new ImageSender(statusText);
            imageSender.execute(selectedPath);
        } else {
            statusText.setText("Tidak Ada Koneksi");
        }

    }
}
