package phucdv.android.magicnote.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import phucdv.android.magicnote.R;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.noteinterface.OnItemLongClickListener;
import phucdv.android.magicnote.noteinterface.ShareComponents;
import phucdv.android.magicnote.ui.colorpicker.ColorPickerDialog;
import phucdv.android.magicnote.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Note}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NoteItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    public final int ACTION_FILTER_CHECKBOX = 0;
    public final int ACTION_FILTER_IMAGE = 1;
    public final int ACTION_FILTER_COLOR = 2;
    public final int ACTION_FILTER_LABEL = 3;
    public final int ACTION_FILTER_TEXT = 4;
    public final int COLOR_NONE = ColorPickerDialog.COLOR_NONE;

    private final int TYPE_DEFAULT = 0;
    private final int TYPE_BLANK = 1;

    public final int MODE_NORMAL = 0;
    public final int MODE_SELECT = 1;
    public final int MODE_SELECT_ALL = 2;

    private int mMode = MODE_NORMAL;

    private OnItemLongClickListener mOnItemLongClickListener;

    private List<Note> mValues;
    private List<Note> mValuesFilted;
    private boolean[] mSelectedPos;
    private Note[] mNoteArr;

    private boolean mFilterCheckbox = false;
    private boolean mFilterImage = false;
    private int mFilterColor = COLOR_NONE;
    private int mFilterLabel = -1;
    private String mFilterText = "";


    public NoteItemRecyclerViewAdapter() {
        mValues = new ArrayList<>();
        mValuesFilted = mValues;
    }

    public void setValues(List<Note> values){
        mValues = values;
        mValuesFilted = mValues;
        mNoteArr = new Note[values.size()];
        values.toArray(mNoteArr);
        mSelectedPos = new boolean[mValuesFilted.size()];
        notifyDataSetChanged();
    }

    public void setMode(int mode){
        mMode = mode;
    }

    public boolean isSelecting(){
        return mMode != MODE_NORMAL;
    }

    public void startSelect(){
        setMode(MODE_SELECT);
        Arrays.fill(mSelectedPos, false);
        notifyDataSetChanged();
    }

    public void startSelectAll(){
        setMode(MODE_SELECT_ALL);
        Arrays.fill(mSelectedPos, true);
        notifyDataSetChanged();
    }

    public void endSelect(){
        setMode(MODE_NORMAL);
        notifyDataSetChanged();
    }

    public List<Note> getSelectedList(){
        List<Note> notes = new ArrayList<>();
        for(int i = 0; i < mSelectedPos.length; i++){
            if(mSelectedPos[i]){
                notes.add(mNoteArr[i]);
            }
        }
        return notes;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public Note getItemAt(int index){
        return mValuesFilted.get(index);
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
            Note note = mValuesFilted.get(position);
            Calendar calendar = note.getTime_last_update();
            String time = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1)
                    + "/" + calendar.get(Calendar.YEAR) + " " + calendar.get(Calendar.HOUR_OF_DAY)
                    + ":" + calendar.get(Calendar.MINUTE);
            String content = "";
            int indexOfEndLine = note.getFull_text().indexOf("\n");
            if(indexOfEndLine != -1){
                content = note.getFull_text().substring(indexOfEndLine + 1);
            }
            ((ViewHolder)holder).bind(note.getTitle(), time, note.getColor(), note.isHas_checkbox(),
                    note.isHas_image(), note.isIs_pinned(), content);
        }
    }

    @Override
    public int getItemCount() {
        return mValuesFilted.size() + 1;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                mFilterText = charSequence.toString().toLowerCase();

                if(mFilterCheckbox || mFilterImage || mFilterColor != COLOR_NONE || mFilterLabel != -1 || !mFilterText.isEmpty()) {
                    List<Note> filteredList = new ArrayList<>();
                    for (Note note : mValuesFilted) {
                        if (mFilterCheckbox && note.isHas_checkbox()
                                || mFilterImage && note.isHas_image()
                                || mFilterColor != COLOR_NONE && mFilterColor == note.getColor()
                                || !mFilterText.isEmpty() && note.getFull_text().toLowerCase().contains(mFilterText)) {
                            filteredList.add(note);
                        }
                    }
                    mValuesFilted = filteredList;
                } else {
                    mValuesFilted = mValues;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mValuesFilted;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mValuesFilted = (List<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void filter(int action, String text, int value, boolean isOn){
        switch (action){
            case ACTION_FILTER_CHECKBOX:
                mFilterCheckbox = isOn;
                break;
            case ACTION_FILTER_IMAGE:
                mFilterImage = isOn;
                break;
            case ACTION_FILTER_COLOR:
                mFilterColor = value;
                break;
            case ACTION_FILTER_LABEL:
                mFilterLabel = value;
                break;
            case ACTION_FILTER_TEXT:
                mFilterText = text;
                break;
        }
        if(mFilterCheckbox || mFilterImage || mFilterColor != COLOR_NONE || mFilterLabel != -1 || !mFilterText.isEmpty()) {
            List<Note> filteredList = new ArrayList<>();
            for (Note note : mValuesFilted) {
                if (mFilterCheckbox && note.isHas_checkbox()
                    || mFilterImage && note.isHas_image()
                    || mFilterColor != COLOR_NONE && mFilterColor == note.getColor()
                    || !mFilterText.isEmpty() && note.getFull_text().toLowerCase().contains(mFilterText)) {
                    filteredList.add(note);
                }
            }
            mValuesFilted = filteredList;
        } else {
            mValuesFilted = mValues;
        }
        notifyDataSetChanged();
    }

    public void clearFilter(){
        mFilterColor = COLOR_NONE;
        mFilterLabel = -1;
        mFilterImage = false;
        mFilterCheckbox = false;
        mValuesFilted = mValues;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mTime;
        public final ImageView mHasCheckbox;
        public final ImageView mHasImage;
        public final ImageView mHasPinned;
        public final CheckBox mCheckBox;
        public final TextView mContent;

        public ViewHolder(View view) {
            super(view);
            mCheckBox = view.findViewById(R.id.select_note_checkbox);

            mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mSelectedPos[getLayoutPosition()] = b;
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mMode == MODE_NORMAL) {
                        if (view.getContext() instanceof ShareComponents) {
                            Bundle bundle = new Bundle();
                            bundle.putLong(Constants.ARG_PARENT_ID, mValuesFilted.get(getLayoutPosition()).getId());
                            ((ShareComponents) view.getContext()).navigate(R.id.action_global_editNoteFragment, bundle);
                        }
                    } else {
                        mCheckBox.setChecked(!mCheckBox.isChecked());
                    }
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(mMode == MODE_NORMAL) {
                        if (mOnItemLongClickListener != null)
                            return mOnItemLongClickListener.onItemLongClick(ViewHolder.this, getLayoutPosition());
                    } else {
                        mCheckBox.setChecked(!mCheckBox.isChecked());
                        return true;
                    }
                    return false;
                }
            });
            mView = view;
            mTitleView = view.findViewById(R.id.title);
            mTime = view.findViewById(R.id.time);
            mHasCheckbox = view.findViewById(R.id.hasCheckbox);
            mHasImage = view.findViewById(R.id.hasImage);
            mHasPinned = view.findViewById(R.id.hasPin);
            mContent = view.findViewById(R.id.content);
        }

        public long getNoteId(int position){
            return mValuesFilted.get(position).getId();
        }

        public void bind(String title, String time, int color, boolean hasCheckbox
                , boolean hasImage, boolean hasPin, String content){
            if(title.isEmpty()){
                title = mView.getContext().getString(R.string.none_text);
            }
            mTitleView.setText(title);
            mTime.setText(mView.getContext().getString(R.string.last_modify, time));
            mView.setBackgroundTintList(ColorStateList.valueOf(color));
            mHasCheckbox.setVisibility(hasCheckbox ? View.VISIBLE : View.GONE);
            mHasImage.setVisibility(hasImage ? View.VISIBLE : View.GONE);
            mHasPinned.setVisibility(hasPin ? View.VISIBLE : View.GONE);
            mCheckBox.setVisibility(mMode == MODE_NORMAL ? View.GONE : View.VISIBLE);
            mCheckBox.setChecked(mSelectedPos[getLayoutPosition()]);
            mContent.setText("");
            mContent.setText(content);
        }
    }
}