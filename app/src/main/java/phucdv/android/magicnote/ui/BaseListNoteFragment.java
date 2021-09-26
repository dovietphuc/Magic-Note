package phucdv.android.magicnote.ui;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.reflect.Field;
import java.util.List;

import phucdv.android.magicnote.R;
import phucdv.android.magicnote.adapter.NoteItemRecyclerViewAdapter;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.noteinterface.OnItemLongClickListener;
import phucdv.android.magicnote.noteinterface.ShareComponents;
import phucdv.android.magicnote.noteinterface.TouchHelper;
import phucdv.android.magicnote.util.Constants;
import phucdv.android.magicnote.util.KeyBoardController;

public abstract class BaseListNoteFragment extends Fragment implements View.OnClickListener, TouchHelper, Toolbar.OnMenuItemClickListener, OnItemLongClickListener {

    protected RecyclerView mRecyclerView;
    protected NoteItemRecyclerViewAdapter mAdapter;
    protected ShareComponents mShareComponents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list_note_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new NoteItemRecyclerViewAdapter();
        mAdapter.setOnItemLongClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

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
        // Associate searchable configuration with the SearchView
        SearchManager manager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.action_search).getActionView();
        search.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

        // listening to search query text change
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
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

    public abstract String[] getPopupMenuItem(Note note);

    public abstract void onPopupItemSelect(DialogInterface dialog, int which, Note note);

    @Override
    public boolean onItemLongClick(RecyclerView.ViewHolder holder, int pos) {
        Note note = mAdapter.getItemAt(pos);
        String[] option = getPopupMenuItem(note);
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(getContext())
                .setTitle(R.string.what_you_want_to_do)
                .setItems(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPopupItemSelect(dialog, which, note);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return true;
    }

    public void notifyListChange(List<Note> notes){
        mShareComponents.getBottomAppBarTitle().setText(
                notes.size() + " " + ((notes.size() > 1)
                        ? getString(R.string.notes) : getString(R.string.note)));
    }
}