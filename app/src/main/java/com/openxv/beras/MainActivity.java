package com.openxv.beras;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public ImageView selectedImage;
    public RecyclerView imageGrid;
    public ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedImage = (ImageView) findViewById(R.id.selected_image);

        imageGrid = (RecyclerView) findViewById(R.id.image_grid);
        imageGrid.setLayoutManager(new GridLayoutManager(this, 4));

        String[] data = {"a","b"};
        adapter = new ImageAdapter(this, data);
        adapter.setClickListener(new ImageAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("TAG", "You clicked number " + adapter.getItem(position) + ", which is at cell position " + position);
            }
        });
        imageGrid.setAdapter(adapter);

    }
}
