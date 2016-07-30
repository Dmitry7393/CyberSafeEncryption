package encryption.com.adapters;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import encryption.com.Database.DatabaseHelper;
import encryption.com.cybersafeencryption.R;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>  {
    public ArrayList<Integer> mListID;
    private Context mContext;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public MyRecyclerViewAdapter(ArrayList<Integer> mListImages, Context mContext) {
        this.mContext = mContext;
        this.mListID = mListImages;
        dbHelper = new DatabaseHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Cursor cursor = database.query("table_image", null, null, null, null, null, null);
        byte[] image;
        int i = 0;
        Bitmap btmp = null;
        if (cursor.moveToFirst()) {
            do {
                if(i == position) {
                    image = cursor.getBlob(cursor.getColumnIndex("image_data"));
                    btmp = convertBytesToBitmap(image);
                    break;
                }
                i++;
            } while (cursor.moveToNext());
        }
        holder.imageViewIcon.setImageBitmap(btmp);
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
        return mListID.size();
    }
    public Bitmap convertBytesToBitmap(byte[] image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
     //   options.inJustDecodeBounds = true;
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }
}