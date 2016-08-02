package encryption.com.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import encryption.com.Database.DatabaseHelper;
import encryption.com.cybersafeencryption.R;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    public ArrayList<Integer> mListID;
    private SQLiteDatabase database;

    public MyRecyclerViewAdapter(ArrayList<Integer> listID, Context mContext) {
        this.mListID = listID;
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    private Bitmap getBitmapByID(int id) {
        Cursor cursor = database.rawQuery("SELECT image_data FROM table_image WHERE id=?", new String[]{id + ""});
        byte[] image = null;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            image = cursor.getBlob(cursor.getColumnIndex("image_data"));
        }
        cursor.close();
        return convertBytesToBitmap(image);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.imageViewIcon.setImageBitmap(getBitmapByID(mListID.get(position)));
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
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }
}