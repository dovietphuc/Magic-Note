package phucdv.android.magicnote.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
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
    protected View mQuickFilter;
    protected boolean mClosing = false;
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

        mQuickFilter = view.findViewById(R.id.quick_filter);
        ImageButton filterCheckbox = mQuickFilter.findViewById(R.id.filter_checkbox);
        filterCheckbox.setOnClickListener(this);
        ImageButton filterImage = mQuickFilter.findViewById(R.id.filter_image);
        filterImage.setOnClickListener(this);
        ImageButton filterColor = mQuickFilter.findViewById(R.id.filter_color);
        filterColor.setOnClickListener(this);
        ImageButton filterLabel = mQuickFilter.findViewById(R.id.filter_label);
        filterLabel.setOnClickListener(this);

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
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuickFilter.setVisibility(View.VISIBLE);
                mQuickFilter.animate()
                        .translationY(0);
            }
        });
        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mClosing = true;
                mQuickFilter.animate()
                        .translationY(-mQuickFilter.getHeight())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if(mClosing) {
                                    mQuickFilter.setVisibility(View.GONE);
                                    mClosing = false;
                                }
                            }
                        });
                return false;
            }
        });
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
            case R.id.filter_checkbox:
                mAdapter.filter(mAdapter.ACTION_FILTER_CHECKBOX, 0, true);
                break;
            case R.id.filter_image:
                mAdapter.filter(mAdapter.ACTION_FILTER_IMAGE, 0, true);
                break;
            case R.id.filter_color:
                mAdapter.filter(mAdapter.ACTION_FILTER_COLOR, Color.RED, false);
                break;
            case R.id.filter_label:
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                return true;
        }
        return false;
    }
}