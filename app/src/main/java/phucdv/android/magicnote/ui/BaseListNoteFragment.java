package phucdv.android.magicnote.ui;

import android.content.Context;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import phucdv.android.magicnote.R;
import phucdv.android.magicnote.adapter.NoteItemRecyclerViewAdapter;
import phucdv.android.magicnote.noteinterface.ShareComponents;
import phucdv.android.magicnote.noteinterface.TouchHelper;
import phucdv.android.magicnote.util.Constants;
import phucdv.android.magicnote.util.KeyBoardController;
import phucdv.android.magicnote.util.NoteItemTouchCallback;

public abstract class BaseListNoteFragment extends Fragment implements View.OnClickListener, TouchHelper, Toolbar.OnMenuItemClickListener {

    protected RecyclerView mRecyclerView;
    protected NoteItemRecyclerViewAdapter mAdapter;
    protected ShareComponents mShareComponents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list_note_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new NoteItemRecyclerViewAdapter();
            mRecyclerView.setAdapter(mAdapter);
        }

        mShareComponents = (ShareComponents) getContext();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getContext() == null || getActivity() == null) return;
        if(getContext() instanceof ShareComponents){
            mShareComponents.getFloatingActionButton().setOnClickListener(this);
            KeyBoardController.hideKeyboard(getActivity());
            mShareComponents.getBottomAppBar().replaceMenu(R.menu.blank_menu);
            mShareComponents.getBottomAppBarTitle().setVisibility(View.VISIBLE);
            mShareComponents.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(getMenu(), menu);
        mShareComponents.getToolbar().setOnMenuItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if(getContext() == null) return;
                if(getContext() instanceof ShareComponents){
                    Bundle bundle = new Bundle();
                    bundle.putLong(Constants.ARG_PARENT_ID, Constants.UNKNOW_PARENT_ID);
                    mShareComponents.navigate(R.id.action_global_editNoteFragment, bundle);
                }
                break;
        }
    }

    public abstract int getMenu();
}