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
import java.util.HashMap;
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

    public static final int STATE_ADD = 0;
    public static final int STATE_MODIFY = 1;
    public static final int STATE_DELETE = 2;
    public static final int STATE_NONE = 3;

    private SortedList<BaseItem> mItemSortedList;
    private HashMap<BaseItem, Integer> mHashItem;
    private long mTextCount = 0;
    private long mCheckboxtextCount = 0;
    private SortedList<BaseItem> mNewSortedList = new SortedList<BaseItem>(BaseItem.class, new SortedList.Callback<BaseItem>() {
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

    public EditNoteItemRecyclerViewAdapter() {
        mHashItem = new HashMap<>();
        mItemSortedList = mNewSortedList;
    }

    public void setValues(List<TextItem> textItems, List<CheckboxItem> checkboxItems) {
        mItemSortedList.clear();
        if(textItems == null) {
            textItems = new ArrayList<>();
        }
        if (checkboxItems == null) {
            checkboxItems = new ArrayList<>();
        }
        for (BaseItem item : textItems) {
            mHashItem.put(item, STATE_NONE);
        }
        for (BaseItem item : checkboxItems) {
            mHashItem.put(item, STATE_NONE);
        }
        mItemSortedList.addAll((List)textItems);
        mItemSortedList.addAll((List)checkboxItems);
        if (mItemSortedList.size() == 0) {
            BaseItem baseItem = new TextItem(Constants.UNKNOW_PARENT_ID, 0, "");
            mItemSortedList.add(baseItem);
            mHashItem.put(baseItem, STATE_ADD);
        }
        notifyDataSetChanged();
    }

    public void addItem(BaseItem item){
        mItemSortedList.add(item);
        mHashItem.put(item, STATE_ADD);
        notifyItemInserted(mItemSortedList.size() - 1);
    }

    public void removeItem(int position){
        BaseItem baseItem = mItemSortedList.get(position);
        mHashItem.put(baseItem, STATE_DELETE);
        mItemSortedList.remove(baseItem);
        notifyItemRemoved(position);
    }

    public HashMap<BaseItem, Integer> getHashMap(){
        return mHashItem;
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
                    BaseItem item = mItemSortedList.get(getLayoutPosition());
                    ((TextItem)item).setContent(s.toString());
                    if(mHashItem.get(item) != STATE_ADD){
                        mHashItem.put(item, STATE_MODIFY);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        public void bind(String text) {
            mEditTextView.setText(text);
        }
    }

    public class CheckboxItemViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CheckBox mCheckBoxView;
        public final EditText mEditTextView;

        public CheckboxItemViewHolder(View view) {
            super(view);
            mView = view;
            mCheckBoxView = view.findViewById(R.id.checkBox);
            mCheckBoxView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    BaseItem item = mItemSortedList.get(getLayoutPosition());
                    ((CheckboxItem)item).setIs_checked(isChecked);
                    if(mHashItem.get(item) != STATE_ADD){
                        mHashItem.put(item, STATE_MODIFY);
                    }
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
                    BaseItem item = mItemSortedList.get(getLayoutPosition());
                    ((CheckboxItem)item).setContent(s.toString());
                    if(mHashItem.get(item) != STATE_ADD){
                        mHashItem.put(item, STATE_MODIFY);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        public void bind(String text, boolean isChecked) {
            mCheckBoxView.setChecked(isChecked);
            mEditTextView.setText(text);
        }
    }
}