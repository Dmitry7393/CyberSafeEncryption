package encryption.com.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;


import encryption.com.adapters.GridViewDrawingsAdapter;
import encryption.com.cybersafeencryption.R;

public class PageViewDrawingsFragment extends Fragment {

    private GridView mGridView;
    private GridViewDrawingsAdapter mGridViewDrawingsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.fragment_view_drawings, container, false);
        mGridView = (GridView) rootView.findViewById(R.id.gridViewDrawings);
        return rootView;
    }
    final String LOG_TAG = "TTTT2";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void onStart() {
        super.onStart();
        mGridViewDrawingsAdapter = new GridViewDrawingsAdapter(getContext());
        if(mGridViewDrawingsAdapter != null)
            mGridView.setAdapter(mGridViewDrawingsAdapter);
        Log.d(LOG_TAG, "88 onStart");
    }
}