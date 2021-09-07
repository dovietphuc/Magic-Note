package phucdv.android.magicnote.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import phucdv.android.magicnote.R;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.noteinterface.ShareComponents;
import phucdv.android.magicnote.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Note}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NoteItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_DEFAULT = 0;
    private final int TYPE_BLANK = 1;

    private List<Note> mValues;

    public NoteItemRecyclerViewAdapter(List<Note> items) {
        mValues = items;
    }

    public NoteItemRecyclerViewAdapter() {
        mValues = new ArrayList<>();
    }

    public void setValues(List<Note> values){
        mValues = values;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == getItemCount() - 1 ? TYPE_BLANK : TYPE_DEFAULT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case TYPE_BLANK:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fake_item_margin, parent, false);
                return new RecyclerView.ViewHolder(view){};
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.note_item, parent, false);
                return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder){
            ((ViewHolder)holder).bind(mValues.get(position).getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
//        public final View mMarginBottomView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(view.getContext() instanceof ShareComponents){
                        Bundle bundle = new Bundle();
                        bundle.putLong(Constants.ARG_PARENT_ID, mValues.get(getLayoutPosition()).getId());
                        ((ShareComponents)view.getContext()).navigate(R.id.action_global_editNoteFragment, bundle);
                    }
                }
            });
            mView = view;
            mTitleView = view.findViewById(R.id.title);
//            mMarginBottomView = view.findViewById(R.id.margin_bottom_view);
        }

        public long getNoteId(int position){
            return mValues.get(position).getId();
        }

        public void bind(String title){
            mTitleView.setText(title);
//            if(getLayoutPosition() == mValues.size() - 1){
//                mMarginBottomView.setVisibility(View.VISIBLE);
//            } else {
//                mMarginBottomView.setVisibility(View.GONE);
//            }
        }
    }
}