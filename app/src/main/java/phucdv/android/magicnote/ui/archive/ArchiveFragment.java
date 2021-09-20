package phucdv.android.magicnote.ui.archive;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import phucdv.android.magicnote.R;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.ui.BaseListNoteFragment;
import phucdv.android.magicnote.util.NoteItemTouchCallback;

public class ArchiveFragment extends BaseListNoteFragment {

    private ArchiveViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mRecyclerView != null){
            new ItemTouchHelper(new NoteItemTouchCallback(ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT,
                    ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT, this))
                    .attachToRecyclerView(mRecyclerView);
        }
        mViewModel = new ViewModelProvider(this).get(ArchiveViewModel.class);
        mViewModel.getNotesInArchive().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mAdapter.setValues(notes);
                mShareComponents.getBottomAppBarTitle().setText(notes.size() + " " + getString(R.string.note));
            }
        });
        return view;
    }

    @Override
    public int getMenu() {
        return R.menu.archive_menu;
    }

    public void onSwipeLeft(RecyclerView.ViewHolder viewHolder){
        if(viewHolder.getLayoutPosition() >= mViewModel.getNotesInArchive().getValue().size()){
            return;
        }
        Note note = mViewModel.getNotesInArchive().getValue()
                .get(viewHolder.getLayoutPosition());
        note.setIs_archive(false);
        note.setIs_deleted(true);
        mViewModel.updateNote(note);
        Snackbar.make(getView(), "Moved to trash", Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        note.setIs_archive(true);
                        note.setIs_deleted(false);
                        mViewModel.updateNote(note);
                    }
                }).show();
    }
    public void onSwipeRight(RecyclerView.ViewHolder viewHolder){
        if(viewHolder.getLayoutPosition() >= mViewModel.getNotesInArchive().getValue().size()){
            return;
        }
        Note note = mViewModel.getNotesInArchive().getValue()
                .get(viewHolder.getLayoutPosition());
        note.setIs_archive(false);
        note.setIs_deleted(false);
        mViewModel.updateNote(note);
        Snackbar.make(getView(), "Moved to processing", Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        note.setIs_archive(true);
                        note.setIs_deleted(false);
                        mViewModel.updateNote(note);
                    }
                }).show();
    }
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target){
        return false;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder.getLayoutPosition() >= mViewModel.getNotesInArchive().getValue().size()){
            return;
        }
        new RecyclerViewSwipeDecorator.Builder(getContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftLabel(getString(R.string.recycle_bin))
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_white_24)
                .addSwipeLeftBackgroundColor(getResources().getColor(R.color.to_trash))
                .addSwipeRightLabel(getString(R.string.processing))
                .addSwipeRightActionIcon(R.drawable.ic_baseline_emoji_objects_white_24)
                .addSwipeRightBackgroundColor(getResources().getColor(R.color.to_processing))
                .create()
                .decorate();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}