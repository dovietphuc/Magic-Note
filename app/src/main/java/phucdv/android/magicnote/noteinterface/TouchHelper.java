package phucdv.android.magicnote.noteinterface;

import androidx.recyclerview.widget.RecyclerView;

public interface TouchHelper {
    public void onSwipeLeft(RecyclerView.ViewHolder viewHolder);
    public void onSwipeRight(RecyclerView.ViewHolder viewHolder);
    public boolean onMove(RecyclerView recyclerView,
                       RecyclerView.ViewHolder viewHolder,
                       RecyclerView.ViewHolder target);
}
