package phucdv.android.magicnote.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import java.util.ArrayList;
import java.util.List;

import phucdv.android.magicnote.R;
import phucdv.android.magicnote.data.BaseItem;
import phucdv.android.magicnote.data.checkboxitem.CheckboxItem;
import phucdv.android.magicnote.data.textitem.TextItem;
import phucdv.android.magicnote.noteinterface.ShareComponents;
import phucdv.android.magicnote.util.Constants;

public class EditNoteItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_TEXT = 0;
    private final int TYPE_CHECKBOX = 1;
    private final int TYPE_BLANK = 2;

    private SortedList<BaseItem> mItemSortedList;
    private long mTextCount = 0;
    private long mCheckboxtextCount = 0;

    public EditNoteItemRecyclerViewAdapter() {
        mItemSortedList = new SortedList<BaseItem>(BaseItem.class, new SortedList.Callback<BaseItem>() {
            @Override
            public int compare(BaseItem o1, BaseItem o2) {
                return (int) (o1.getOrder_in_parent() - o2.getOrder_in_parent());
            }

            @Override
            public void onChanged(int position, int count) {

            }

            @Override
            public boolean areContentsTheSame(BaseItem oldItem, BaseItem newItem) {
                return false;
            }

            @Override
            public boolean areItemsTheSame(BaseItem item1, BaseItem item2) {
                return false;
            }

            @Override
            public void onInserted(int position, int count) {

            }

            @Override
            public void onRemoved(int position, int count) {

            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {

            }
        });
    }

    public void setValues(List<BaseItem> values) {
//        mItemSortedList.clear();
        if(values != null){
            mItemSortedList.addAll(values);
            if(mItemSortedList.size() == 0){
                mItemSortedList.add(new TextItem(Constants.UNKNOW_PARENT_ID, 0, ""));
            }
        }
        notifyDataSetChanged();
    }

    public void addItem(BaseItem item){
        mItemSortedList.add(item);
        notifyItemInserted(mItemSortedList.size() - 1);
    }

    public void removeItem(int position){

    }

    public SortedList<BaseItem> getAdapterList(){
        return mItemSortedList;
    }

    public long getAllTextCount(){
        return mTextCount + mCheckboxtextCount;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() - 1) return TYPE_BLANK;

        BaseItem item = mItemSortedList.get(position);
        if (item instanceof TextItem) {
            return TYPE_TEXT;
        } else if (item instanceof CheckboxItem) {
            return TYPE_CHECKBOX;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_CHECKBOX:
                return new CheckboxItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.checkbox_item, parent, false));
            case TYPE_TEXT:
                return new TextItemViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.text_item, parent, false));
            default:
                return new RecyclerView.ViewHolder(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fake_item_margin, parent, false)){};
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_CHECKBOX:
                ((CheckboxItemViewHolder) holder)
                        .bind(((CheckboxItem) mItemSortedList.get(position)).getContent(),
                                ((CheckboxItem) mItemSortedList.get(position)).isIs_checked());
                break;
            case TYPE_TEXT: ((TextItemViewHolder) holder)
                    .bind(((TextItem) mItemSortedList.get(position)).getContent());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mItemSortedList.size() + 1;
    }

    public class TextItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final EditText mEditTextView;
//        public final View mMarginBottomView;

        public TextItemViewHolder(View view) {
            super(view);
            mView = view;
            mEditTextView = view.findViewById(R.id.textInputText);
            mEditTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mTextCount = s.toString().trim().length();
                    ((TextItem)mItemSortedList.get(getLayoutPosition())).setContent(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
//            mMarginBottomView = view.findViewById(R.id.margin_bottom_view);

        }

        public void bind(String text) {
            mEditTextView.setText(text);
//            if(getLayoutPosition() == mItemSortedList.size() - 1){
//                mMarginBottomView.setVisibility(View.VISIBLE);
//            } else {
//                mMarginBottomView.setVisibility(View.GONE);
//            }
        }
    }

    public class CheckboxItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CheckBox mCheckBoxView;
        public final EditText mEditTextView;
//        public final View mMarginBottomView;

        public CheckboxItemViewHolder(View view) {
            super(view);
            mView = view;
            mCheckBoxView = view.findViewById(R.id.checkBox);
            mCheckBoxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ((CheckboxItem)mItemSortedList.get(getLayoutPosition())).setIs_checked(isChecked);
                }
            });
            mEditTextView = view.findViewById(R.id.textInputText);
            mEditTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mCheckboxtextCount = s.toString().trim().length();
                    ((CheckboxItem)mItemSortedList.get(getLayoutPosition())).setContent(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
//            mMarginBottomView = view.findViewById(R.id.margin_bottom_view);

        }

        public void bind(String text, boolean isChecked) {
            mCheckBoxView.setChecked(isChecked);
            mEditTextView.setText(text);
//            if(getLayoutPosition() == mItemSortedList.size() - 1){
//                mMarginBottomView.setVisibility(View.VISIBLE);
//            } else {
//                mMarginBottomView.setVisibility(View.GONE);
//            }
        }
    }
}