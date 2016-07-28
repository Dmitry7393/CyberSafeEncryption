package encryption.com.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import encryption.com.cybersafeencryption.R;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>  {
    private ArrayList<Bitmap> mListImages;
    private Context mContext;

    public MyRecyclerViewAdapter(ArrayList<Bitmap> mListImages, Context mContext) {
        this.mContext = mContext;
        this.mListImages = mListImages;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.imageViewIcon.setImageBitmap(mListImages.get(position));
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.recyclerviewimages);
        }
    }

    @Override
    public int getItemCount() {
        return mListImages.size();
    }
}