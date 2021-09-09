package phucdv.android.magicnote.ui.editnote;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import phucdv.android.magicnote.R;
import phucdv.android.magicnote.adapter.EditNoteItemRecyclerViewAdapter;
import phucdv.android.magicnote.data.BaseItem;
import phucdv.android.magicnote.data.checkboxitem.CheckboxItem;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.textitem.TextItem;
import phucdv.android.magicnote.noteinterface.AsyncResponse;
import phucdv.android.magicnote.noteinterface.ShareComponents;
import phucdv.android.magicnote.util.Constants;
import phucdv.android.magicnote.util.KeyBoardController;

public class EditNoteFragment extends Fragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    protected RecyclerView mRecyclerView;
    protected EditNoteItemRecyclerViewAdapter mAdapter;
    protected EditNoteViewModel mViewModel;
    protected ShareComponents mShareComponents;
    protected Note mNote;
    protected Observer<List<TextItem>> mListTextItemObserver = new Observer<List<TextItem>>() {
        @Override
        public void onChanged(List<TextItem> textItems) {
            mAdapter.setValues(textItems, mViewModel.getListCheckboxItems().getValue());
        }
    };

    protected Observer<List<CheckboxItem>> mListCheckboxItemObserver = new Observer<List<CheckboxItem>>() {
        @Override
        public void onChanged(List<CheckboxItem> checkboxItems) {
            mAdapter.setValues(mViewModel.getListTextItems().getValue(), checkboxItems);
        }
    };

    protected Observer<Long> mParentIdObserver = new Observer<Long>() {
        @Override
        public void onChanged(Long aLong) {
            mViewModel.initBaseItemRepository();
            mViewModel.getNote().observe(getViewLifecycleOwner(), new Observer<Note>() {
                @Override
                public void onChanged(Note note) {
                    mNote = note;
                }
            });
            mViewModel.getListTextItems().observe(getViewLifecycleOwner(), mListTextItemObserver);
            mViewModel.getListCheckboxItems().observe(getViewLifecycleOwner(), mListCheckboxItemObserver);
        }
    };
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_note_fragment, container, false);

        if(view instanceof RecyclerView){
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            mAdapter = new EditNoteItemRecyclerViewAdapter(getContext());
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setOnTouchListener(new View.OnTouchListener() {
                int downPosX = 0;
                int downPosY = 0;
                int slop = 50;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        if(Math.abs(downPosX - event.getX()) < slop && Math.abs(downPosY - event.getY()) < slop){
                            if(!(mAdapter.getAdapterList().get(mAdapter.getAdapterList().size()-1) instanceof TextItem)){
                                mAdapter.addTextItem("");
                            } else {
                                mAdapter.focusOnPosition(mAdapter.getAdapterList().size() - 1);
                            }
                            downPosX = 0;
                            downPosY = 0;
                            return true;
                        }
                        downPosX = 0;
                        downPosY = 0;
                    } else if(event.getAction() == MotionEvent.ACTION_DOWN){
                        downPosX = (int) event.getX();
                        downPosY = (int) event.getY();
                    }
                    return false;
                }
            });
        }

        mViewModel = new ViewModelProvider(this).get(EditNoteViewModel.class);

        mViewModel.getParentId().observe(getViewLifecycleOwner(), mParentIdObserver);

        mShareComponents = (ShareComponents) getContext();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getArguments() != null){
            long parentId = getArguments().getLong(Constants.ARG_PARENT_ID, Constants.UNKNOW_PARENT_ID);
            if(parentId == Constants.UNKNOW_PARENT_ID){
                mAdapter.setValues(new ArrayList<>(), new ArrayList<>());
            } else {
                mViewModel.getParentId().setValue(parentId);
            }
        }

        if(getContext() == null || getActivity() == null) return;
        if(getContext() instanceof ShareComponents) {
            mShareComponents.getFloatingActionButton().setOnClickListener(this);
            mShareComponents.getBottomAppBar().replaceMenu(R.menu.edit_bottom_menu);
            mShareComponents.getBottomAppBar().setOnMenuItemClickListener(this);
            mShareComponents.getBottomAppBarTitle().setVisibility(View.GONE);
            mShareComponents.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mShareComponents.setFabDrawable(R.drawable.avd_done_to_add);
        }
    }

    @Override
    public void onDestroy() {
        mViewModel.onSave(mAdapter, mNote);
        if(getContext() instanceof ShareComponents) {
            mShareComponents.setFabDrawable(R.drawable.avd_add_to_done);
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if(getActivity() == null) return;
                if(!KeyBoardController.hideKeyboard(getActivity())){
                    getActivity().onBackPressed();
                } else{
                    mShareComponents.getBottomAppBar().performShow();
                }
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_undo:
                break;
            case R.id.action_redo:
                break;
            case R.id.action_add_check_item:
                mAdapter.addCheckItem("", false);
                break;
        }
        return false;
    }
}