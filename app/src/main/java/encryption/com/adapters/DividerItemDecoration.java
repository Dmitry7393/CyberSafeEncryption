package encryption.com.adapters;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpaceSize ;
    public DividerItemDecoration(int spaceSize) {
        this.mSpaceSize = spaceSize;
    }
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state)
    {
        outRect.right = mSpaceSize;
    }
}