package encryption.com.cybersafeencryption;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;



public class MyDialogFragment extends DialogFragment implements
        OnItemClickListener  {

    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    List<Note> mListNotes;
    ListView mylist;
    TextView txtViewNote;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_fragment, null, false);



        mylist = (ListView) view.findViewById(R.id.listDFragment);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListNotes = new ArrayList<>();
        mListNotes = ((EncryptTextActivity)getActivity()).getlistOfNotes();

        ListNotesViewAdapter customAdapter = new ListNotesViewAdapter(getActivity().getApplicationContext(), R.layout.item_notes, mListNotes);
        mylist.setAdapter(customAdapter);
        mylist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        txtViewNote = (TextView) view.findViewById(R.id.txt_view__note);
        EditNameDialogListener activity = (EditNameDialogListener) getActivity();
        activity.onFinishEditDialog(txtViewNote.getText().toString());
        dismiss();
        Toast.makeText(getActivity(), mListNotes.get(position).getTitleNote(), Toast.LENGTH_SHORT)
                .show();
    }

}
