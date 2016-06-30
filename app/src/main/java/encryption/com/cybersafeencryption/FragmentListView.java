package encryption.com.cybersafeencryption;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class FragmentListView extends Fragment {
    List<Note> mListNotes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_listview, container, false);

       // mListNotes = new ArrayList<>();
      //  ListView tabListView = (ListView) rootView.findViewById(R.id.list_view);

       // ListNotesViewAdapter customAdapter = new ListNotesViewAdapter(getActivity().getApplicationContext(), R.layout.item_notes, mListNotes);

       // tabListView.setAdapter(customAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}