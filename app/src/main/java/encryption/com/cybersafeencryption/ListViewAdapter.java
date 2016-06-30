package encryption.com.cybersafeencryption;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<ContactInfo> {
    private List<ContactInfo> mListData = new ArrayList<>();

    public ListViewAdapter(Context context, int resource, List<ContactInfo> items) {
        super(context, resource, items);
        mListData = items;
    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        EditText editText = (EditText) view.findViewById(R.id.editTextFiles);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        ViewHolder holder;
        if (convertView == null) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item, parent, false);
            holder = new ViewHolder(itemView);
            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        holder.setData(position);
        return itemView;
    }

    private class ViewHolder {

        private EditText t;

        public ViewHolder(View itemView) {
            t = (EditText) itemView.findViewById(R.id.editTextFiles);
        }

        public void setData(int position) {
            t.setText(mListData.get(position).getNameEnterprise());
        }
    }
}