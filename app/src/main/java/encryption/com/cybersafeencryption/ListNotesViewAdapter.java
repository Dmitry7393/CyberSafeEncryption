package encryption.com.cybersafeencryption;

import android.app.DialogFragment;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ListNotesViewAdapter extends ArrayAdapter<Note> {
    private List<Note> mListData = new ArrayList<>();

    public ListNotesViewAdapter(Context context, int resource, List<Note> items) {
        super(context, resource, items);
        mListData = items;
    }
    public List<Note> getUpdatedListNote() {
        return mListData;
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
        CheckBox cb = (CheckBox) itemView.findViewById(R.id.checkbox);
        // присваиваем чекбоксу обработчик
        cb.setOnCheckedChangeListener(myCheckChangList);
        // пишем позицию
        cb.setTag(position);

        holder.setData(position);
        return itemView;
    }
    // обработчик для чекбоксов
    OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            buttonView.setSelected(true);
            int position = (Integer) buttonView.getTag();
            mListData.get(position).setCheckBox();
        }
    };
    private class ViewHolder {
        private TextView vTitleNote;
        private TextView vNote;
        private TextView vDate;

        public ViewHolder(View itemView) {
            vTitleNote = (TextView) itemView.findViewById(R.id.txt_view_title_note);
            vNote = (TextView) itemView.findViewById(R.id.txt_view_note);
            vDate = (TextView) itemView.findViewById(R.id.txt_view_date);
        }

        public void setData(int position) {
            vTitleNote.setText(mListData.get(position).getTitleNote());
            vNote.setText(mListData.get(position).getNote());
            vDate.setText(mListData.get(position).getDate());
        }
    }
}