package encryption.com.cybersafeencryption;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListNotesViewAdapter extends ArrayAdapter<Note> {
    private List<Note> mListData = new ArrayList<>();

    public ListNotesViewAdapter(Context context, int resource, List<Note> items) {
        super(context, resource, items);
        mListData = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder;
        if (convertView == null) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notes, parent, false);
            holder = new ViewHolder(itemView);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        holder.setData(position);
        return itemView;
    }

    private class ViewHolder {
        private TextView vTitleNote;
        private TextView vNote;
        private TextView vDate;

        public ViewHolder(View itemView) {
            vTitleNote = (TextView) itemView.findViewById(R.id.txt_view_title_note);
            vNote = (TextView) itemView.findViewById(R.id.txt_view__note);
            vDate = (TextView) itemView.findViewById(R.id.txt_view_date);
        }

        public void setData(int position) {
            vTitleNote.setText(mListData.get(position).getTitleNote());
            vNote.setText(mListData.get(position).getNote());
            vDate.setText(mListData.get(position).getDate());
        }
    }
}