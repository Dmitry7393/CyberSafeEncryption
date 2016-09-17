package encryption.com.adapters;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import encryption.com.Database.DatabaseHelper;
import encryption.com.cybersafeencryption.R;

public class GridViewDrawingsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> mListID = new ArrayList<>();
    private SQLiteDatabase database;

    public GridViewDrawingsAdapter(Context c) {
        mContext = c;
        DatabaseHelper dbHelper = new DatabaseHelper(mContext);
        database = dbHelper.getWritableDatabase();

        getImagesId();
    }

    public int getCount() {
        return mListID.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.item_grid_view, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.image.setImageBitmap(getBitmapByID(mListID.get(position)));
        return row;
    }

    static class ViewHolder {
        ImageView image;
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

    public Bitmap convertBytesToBitmap(byte[] image) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        return BitmapFactory.decodeByteArray(image, 0, image.length, options);
    }

    public void getImagesId() {
        Cursor cursor = database.query("table_image", null, null, null, null, null, null);
        mListID.clear();
        if (cursor.moveToFirst()) {
            do {
                mListID.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}