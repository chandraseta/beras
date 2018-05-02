package com.openxv.beras;

import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

/**
 * Created by VincentH on 5/2/2018.
 */

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private String[] mData = new String[0];
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    /**
     * Konstruktur adapter untuk recyclerview
     * @param context
     */
    ImageAdapter(Context context, String[] data) {
        this.mInflater = LayoutInflater.from(context);
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String animal = mData[position];
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    String getItem(int id) {
        return mData[id];
    }

    /**
     * Kelas ViewHolder, clickable dengan onclicklistenernya
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_grid_pic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });
        }
    }

    /**
     * Memasang ClickListener untuk digunakan oleh kelas viewholder
     * @param itemClickListener
     */
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    /**
     * Interface ItemClickListener milik ImageAdapter
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}