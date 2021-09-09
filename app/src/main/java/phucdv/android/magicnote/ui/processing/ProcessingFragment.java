package phucdv.android.magicnote.ui.processing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import phucdv.android.magicnote.R;
import phucdv.android.magicnote.data.BaseItemRepository;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.ui.BaseListNoteFragment;
import phucdv.android.magicnote.util.NoteItemTouchCallback;

public class ProcessingFragment extends BaseListNoteFragment {

    private ProcessingViewModel mProcessingViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mRecyclerView != null){
            new ItemTouchHelper(new NoteItemTouchCallback(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT, this))
                    .attachToRecyclerView(mRecyclerView);
        }
        mProcessingViewModel = new ViewModelProvider(this).get(ProcessingViewModel.class);

        mProcessingViewModel.getProcessingNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mAdapter.setValues(notes);
                mShareComponents.getBottomAppBarTitle().setText(notes.size() + " " + getString(R.string.note));
            }
        });
        return view;
    }

    public void onSwipeLeft(RecyclerView.ViewHolder viewHolder){
        if(viewHolder.getLayoutPosition() >= mProcessingViewModel.getProcessingNotes().getValue().size()){
            return;
        }
        Note note = mProcessingViewModel.getProcessingNotes().getValue()
                .get(viewHolder.getLayoutPosition());
        note.setIs_archive(true);
        note.setIs_deleted(false);
        mProcessingViewModel.updateNote(note);
        Snackbar.make(getView(), "Moved to archive", Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        note.setIs_archive(false);
                        note.setIs_deleted(false);
                        mProcessingViewModel.updateNote(note);
                    }
                }).show();
    }
    public void onSwipeRight(RecyclerView.ViewHolder viewHolder){

    }
    public boolean onMove(RecyclerView recyclerView,
                          RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target){
        return false;
    }
}