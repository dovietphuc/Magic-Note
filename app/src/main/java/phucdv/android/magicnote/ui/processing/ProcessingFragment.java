package phucdv.android.magicnote.ui.processing;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import phucdv.android.magicnote.R;
import phucdv.android.magicnote.data.BaseItemRepository;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.ui.BaseListNoteFragment;
import phucdv.android.magicnote.util.NoteItemTouchCallback;

public class ProcessingFragment extends BaseListNoteFragment {

    private ProcessingViewModel mProcessingViewModel;
    private ItemTouchHelper mItemTouchHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (mRecyclerView != null){
            mItemTouchHelper = new ItemTouchHelper(new NoteItemTouchCallback(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT, this));
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
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

    @Override
    public int getMenu() {
        return R.menu.processing_menu;
    }

    public void onSwipeLeft(RecyclerView.ViewHolder viewHolder){
        if(viewHolder.getLayoutPosition() >= mProcessingViewModel.getProcessingNotes().getValue().size()){
            return;
        }
        Note note = mProcessingViewModel.getProcessingNotes().getValue()
                .get(viewHolder.getLayoutPosition());
        mProcessingViewModel.moveToArchive(note);
        Snackbar.make(getView(), getString(R.string.move_to_archive), Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProcessingViewModel.moveToProcessing(note);
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

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder.getLayoutPosition() >= mProcessingViewModel.getProcessingNotes().getValue().size()){
            return;
        }
        new RecyclerViewSwipeDecorator.Builder(getContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addBackgroundColor(getResources().getColor(R.color.to_archive))
                .addActionIcon(R.drawable.ic_baseline_archive_white_24)
                .addSwipeLeftLabel(getString(R.string.archive))
                .create()
                .decorate();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_select_note:
                mItemTouchHelper.attachToRecyclerView(null);
                mAdapter.startSelect();
                mShareComponents.getToolbar().getMenu().clear();
                return true;
            case R.id.action_select_all:
                mItemTouchHelper.attachToRecyclerView(null);
                mAdapter.startSelectAll();
                mShareComponents.getToolbar().getMenu().clear();
                return true;
        }
        return false;
    }

    @Override
    public String[] getPopupMenuItem(Note note) {
        return new String[]{
                note.isIs_pinned() ? getString(R.string.unpin) : getString(R.string.pin)
                , getString(R.string.share)
                , getString(R.string.move_to_archive)
                , getString(R.string.recycle_bin)
                , getString(R.string.completely_delete)};
    }

    @Override
    public void onPopupItemSelect(DialogInterface dialog, int which, Note note) {
        switch (which){
            case 0:
                mProcessingViewModel.pinOrUnpin(note);
                break;
            case 1:
                break;
            case 2:
                mProcessingViewModel.moveToArchive(note);
                break;
            case 3:
                mProcessingViewModel.moveToTrash(note);
                break;
            case 4:
                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext())
                        .setTitle(R.string.warning)
                        .setMessage(R.string.warning_delete)
                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mProcessingViewModel.deleteNote(note.getId());
                                Snackbar.make(getView(), getString(R.string.delete), Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }
}