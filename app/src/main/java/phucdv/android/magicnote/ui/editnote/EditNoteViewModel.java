package phucdv.android.magicnote.ui.editnote;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private LiveData<Long> mLastNodeInsertedID;
    private LiveData<Long> mLastTextInsertedID;
    private LiveData<Long[]> mLastTextInsertedIDs;
    private LiveData<Long> mLastCheckboxInsertedID;
    private LiveData<Long[]> mLastCheckboxInsertedIDs;
//    private LiveData<Note> mNote;

    public EditNoteViewModel(Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mLastNodeInsertedID = mNoteRepository.getLastNodeInsertedID();
        mParentId = new MutableLiveData<Long>();
    }

    public LiveData<Long> getLastNodeInsertedID() {
        return mLastNodeInsertedID;
    }

    public LiveData<Long> getLastTextInsertedID() {
        return mLastTextInsertedID;
    }

    public LiveData<Long[]> getLastTextInsertedIDs() {
        return mLastTextInsertedIDs;
    }

    public LiveData<Long> getLastCheckboxInsertedID() {
        return mLastCheckboxInsertedID;
    }

    public LiveData<Long[]> getLastCheckboxInsertedIDs() {
        return mLastCheckboxInsertedIDs;
    }

    public void initBaseItemRepository(){
        mBaseItemRepository = new BaseItemRepository(getApplication(), mParentId.getValue());
//        mNoteRepository.initNote(mParentId.getValue());
//        mNote = mNoteRepository.getNote();
        mLastTextInsertedID = mBaseItemRepository.getLastTextInsertedID();
        mLastTextInsertedIDs = mBaseItemRepository.getLastTextInsertedIDs();
        mLastCheckboxInsertedID = mBaseItemRepository.getLastCheckboxInsertedID();
        mLastCheckboxInsertedIDs = mBaseItemRepository.getLastCheckboxInsertedIDs();
    }

//    public LiveData<Note> getNote(){
//        return mNote;
//    }

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
}