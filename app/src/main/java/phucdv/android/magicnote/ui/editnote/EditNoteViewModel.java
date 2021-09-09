package phucdv.android.magicnote.ui.editnote;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.SortedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import phucdv.android.magicnote.adapter.EditNoteItemRecyclerViewAdapter;
import phucdv.android.magicnote.data.BaseItem;
import phucdv.android.magicnote.data.BaseItemRepository;
import phucdv.android.magicnote.data.checkboxitem.CheckboxItem;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteRepository;
import phucdv.android.magicnote.data.textitem.TextItem;
import phucdv.android.magicnote.noteinterface.AsyncResponse;
import phucdv.android.magicnote.util.AsyncTaskUtil;
import phucdv.android.magicnote.util.Constants;

public class EditNoteViewModel extends AndroidViewModel {
    private NoteRepository mNoteRepository;
    private BaseItemRepository mBaseItemRepository;
    private MutableLiveData<Long> mParentId;
    private LiveData<Note> mNote;

    public EditNoteViewModel(Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mParentId = new MutableLiveData<Long>();
    }

    public void initBaseItemRepository(){
        mBaseItemRepository = new BaseItemRepository(getApplication(), mParentId.getValue());
        mNoteRepository.initNote(mParentId.getValue());
        mNote = mNoteRepository.getNote();
    }

    public void initBaseItemRepository(long parentId){
        mBaseItemRepository = new BaseItemRepository(getApplication(), parentId);
        mNoteRepository.initNote(parentId);
        mNote = mNoteRepository.getNote();
    }

    public LiveData<Note> getNote(){
        return mNote;
    }

    public MutableLiveData<Long> getParentId() {
        return mParentId;
    }

    public LiveData<List<TextItem>> getListTextItems() {
        return mBaseItemRepository.getListTextItems();
    }

    public LiveData<List<CheckboxItem>> getListCheckboxItems() {
        return mBaseItemRepository.getListCheckboxItems();
    }

    public void insertNote(Note note) { mNoteRepository.insert(note); }

    public void insertNote(Note note, AsyncResponse response) { mNoteRepository.insert(note, response); }

    public void insertTextItem(TextItem textItem){
        mBaseItemRepository.insertTextItem(textItem);
    }

    public void insertAllTextItems(List<TextItem> textItems){
        mBaseItemRepository.insertAllTextItems(textItems);
    }

    public void insertCheckboxItem(CheckboxItem checkboxItem){
        mBaseItemRepository.insertCheckboxItem(checkboxItem);
    }

    public void insertAllCheckboxItems(List<CheckboxItem> checkboxItems){
        mBaseItemRepository.insertAllCheckboxItems(checkboxItems);
    }

    public void deleteNote(long id){
        mNoteRepository.deleteNote(id);
        mBaseItemRepository.deleteTextByParentId(id);
        mBaseItemRepository.deleteCheckboxByParentId(id);
    }

    public void updateTextItem(TextItem item){
        mBaseItemRepository.updateTextItem(item);
    }

    public void updateListTextItem(List<TextItem> items){
        mBaseItemRepository.updateListTextItem(items);
    }

    public void updateCheckboxItem(CheckboxItem item){
        mBaseItemRepository.updateCheckboxItem(item);
    }

    public void updateListCheckboxItem(List<CheckboxItem> items) {
        mBaseItemRepository.updateListCheckboxItem(items);
    }

    public void updateNote(Note item){
        mNoteRepository.updateNote(item);
    }

    public void updateListNote(List<Note> items){
        mNoteRepository.updateListNote(items);
    }

    public void deleteTextItemById(long id){
        mBaseItemRepository.deleteTextById(id);
    }

    public void deleteCheckboxItemById(long id){
        mBaseItemRepository.deleteCheckboxById(id);
    }

    public void onSave(EditNoteItemRecyclerViewAdapter adapter, Note note){
        if(adapter.getAllTextCount() != 0 || note != null) {
            HashMap<BaseItem, Integer> hashMap = adapter.getHashMap();
            Note toInsertNote = (note == null) ? new Note("", 0, 0, false,
                    false, 0, false, 0) : note;
            SortedList<BaseItem> listBase = adapter.getAdapterList();
            if(adapter.getAllTextCount() == 0){
                toInsertNote.setTitle("");
            } else {
                for (int i = 0; i < listBase.size(); i++) {
                    BaseItem item = listBase.get(i);
                    if (item instanceof TextItem) {
                        if (!((TextItem) item).getContent().trim().isEmpty()) {
                            toInsertNote.setTitle(((TextItem) item).getContent());
                            break;
                        }
                    } else if (item instanceof CheckboxItem) {
                        if (!((CheckboxItem) item).getContent().trim().isEmpty()) {
                            toInsertNote.setTitle(((CheckboxItem) item).getContent());
                            break;
                        }
                    }
                }
            }
            if (note == null) {
                insertNote(toInsertNote, new AsyncResponse() {
                    @Override
                    public void processFinish(Object output) {
                        initBaseItemRepository((Long) output);
                        for (int i = 0; i < listBase.size(); i++) {
                            BaseItem item = listBase.get(i);
                            if (item instanceof TextItem) {
                                ((TextItem) item).setParent_id((Long) output);
                                insertTextItem((TextItem) item);
                            } else if (item instanceof CheckboxItem) {
                                ((CheckboxItem) item).setParent_id((Long) output);
                                insertCheckboxItem((CheckboxItem) item);
                            }
                        }
                    }
                });
            } else {
                updateNote(toInsertNote);
                initBaseItemRepository(note.getId());
                for (BaseItem item : hashMap.keySet()) {
                    int state = hashMap.get(item);
                    if (state == EditNoteItemRecyclerViewAdapter.STATE_NONE) {
                        continue;
                    } else if (state == EditNoteItemRecyclerViewAdapter.STATE_ADD) {
                        if (item instanceof TextItem) {
                            ((TextItem) item).setParent_id(note.getId());
                            insertTextItem((TextItem) item);
                        } else if (item instanceof CheckboxItem) {
                            ((CheckboxItem) item).setParent_id(note.getId());
                            insertCheckboxItem((CheckboxItem) item);
                        }
                    } else if (state == EditNoteItemRecyclerViewAdapter.STATE_MODIFY) {
                        if (item instanceof TextItem) {
                            ((TextItem) item).setParent_id(note.getId());
                            updateTextItem((TextItem) item);
                        } else if (item instanceof CheckboxItem) {
                            ((CheckboxItem) item).setParent_id(note.getId());
                            updateCheckboxItem((CheckboxItem) item);
                        }
                    } else if (state == EditNoteItemRecyclerViewAdapter.STATE_DELETE) {
                        if (item instanceof TextItem) {
                            ((TextItem) item).setParent_id(note.getId());
                            deleteTextItemById(item.getId());
                        } else if (item instanceof CheckboxItem) {
                            ((CheckboxItem) item).setParent_id(note.getId());
                            deleteCheckboxItemById(item.getId());
                        }
                    }
                }
            }
        }
    }
}