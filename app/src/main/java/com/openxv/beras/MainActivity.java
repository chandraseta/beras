package com.openxv.beras;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    public ImageView selectedImage;
    public RecyclerView imageGrid;
    public ImageAdapter adapter;
    public View selectedView = null;
    public String selectedImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);

        // Set Title Bar
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        Button sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImagePath != null){
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("image", selectedImagePath);
                    startActivity(intent);
                }
            }
        });

        selectedImage = (ImageView) findViewById(R.id.selected_image);

        // Load Image Grid
        imageGrid = (RecyclerView) findViewById(R.id.image_grid);
        imageGrid.setLayoutManager(new GridLayoutManager(this, 4));
        String[] data = getAllImages();
        adapter = new ImageAdapter(this, data);
        adapter.setClickListener(new ImageAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(selectedView != null){
                    selectedView.setBackgroundColor(getResources().getColor(R.color.colorUnselected));
                }
                view.setBackgroundColor(getResources().getColor(R.color.colorSelected));
                selectedView = view;
                selectedImagePath = adapter.getItem(position);
                Glide.with(getApplicationContext()).load(selectedImagePath).into(selectedImage);
            }
        });
        imageGrid.setAdapter(adapter);
    }

    private String[] getAllImages() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 777);
        }

        String[] data = new String[0];
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(absolutePathOfImage);
        }

        Collections.reverse(listOfAllImages);
        data = listOfAllImages.toArray(new String[0]);

        return data;
    }
}
