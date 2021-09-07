package phucdv.android.magicnote.data;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import phucdv.android.magicnote.data.checkboxitem.CheckboxItem;
import phucdv.android.magicnote.data.checkboxitem.CheckboxItemDao;
import phucdv.android.magicnote.data.textitem.TextItem;
import phucdv.android.magicnote.data.textitem.TextItemDao;
import phucdv.android.magicnote.noteinterface.AsyncResponse;
import phucdv.android.magicnote.util.AsyncTaskUtil;

public class BaseItemRepository {
    private TextItemDao mTextItemDao;
    private CheckboxItemDao mCheckboxItemDao;
    private LiveData<List<TextItem>> mListTextItem;
    private LiveData<List<CheckboxItem>> mListCheckboxItem;
    private MutableLiveData<Long> mLastTextInsertedID;
    private MutableLiveData<Long[]> mLastTextInsertedIDs;
    private MutableLiveData<Long> mLastCheckboxInsertedID;
    private MutableLiveData<Long[]> mLastCheckboxInsertedIDs;

    public BaseItemRepository(Application application){
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mTextItemDao = db.textItemDao();
        mCheckboxItemDao = db.checkboxItemDao();
    }

    public BaseItemRepository(Application application, long parent_id){
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mTextItemDao = db.textItemDao();
        mCheckboxItemDao = db.checkboxItemDao();
        mListTextItem = mTextItemDao.getTextItemForParentId(parent_id);
        mListCheckboxItem = mCheckboxItemDao.getCheckboxItemForParentId(parent_id);

        mLastTextInsertedID = new MutableLiveData<>();
        mLastTextInsertedIDs = new MutableLiveData<>();
        mLastCheckboxInsertedID = new MutableLiveData<>();
        mLastCheckboxInsertedIDs = new MutableLiveData<>();
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

    public LiveData<List<TextItem>> getListTextItems(){
        return mListTextItem;
    }

    public LiveData<List<CheckboxItem>> getListCheckboxItems(){
        return mListCheckboxItem;
    }

    public void insertTextItem(TextItem textItem){
        new AsyncTaskUtil.insertTextItemAsyncTask(mTextItemDao, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                mLastTextInsertedID.setValue((Long) output);
            }
        }).execute(textItem);
    }

    public void insertAllTextItems(List<TextItem> textItems){
        new AsyncTaskUtil.insertAllTextItemsAsyncTask(mTextItemDao, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                mLastTextInsertedIDs.setValue((Long[]) output);
            }
        }).execute(textItems);
    }

    public void insertCheckboxItem(CheckboxItem checkboxItem){
        new AsyncTaskUtil.insertChecboxItemAsyncTask(mCheckboxItemDao, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                mLastCheckboxInsertedID.setValue((Long) output);
            }
        }).execute(checkboxItem);
    }

    public void insertAllCheckboxItems(List<CheckboxItem> checkboxItems){
        new AsyncTaskUtil.insertAllCheckboxItemsAsyncTask(mCheckboxItemDao, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                mLastCheckboxInsertedIDs.setValue((Long[]) output);
            }
        }).execute(checkboxItems);
    }

    public void deleteTextByParentId(long parentId){
        new AsyncTaskUtil.deleteTextItemByParentIdAsyncTask(mTextItemDao).execute(parentId);
    }

    public void deleteTextById(long id){
        new AsyncTaskUtil.deleteTextItemByIdAsyncTask(mTextItemDao).execute(id);
    }

    public void deleteCheckboxByParentId(long parentId){
        new AsyncTaskUtil.deleteCheckboxItemByParentIdAsyncTask(mCheckboxItemDao).execute(parentId);
    }

    public void deleteCheckboxById(long id){
        new AsyncTaskUtil.deleteCheckboxItemByIdAsyncTask(mCheckboxItemDao).execute(id);
    }

    public void updateTextItem(TextItem item){
        new AsyncTaskUtil.updateTextItemAsyncTask(mTextItemDao).execute(item);
    }

    public void updateListTextItem(List<TextItem> items){
        new AsyncTaskUtil.updateListTextItemAsyncTask(mTextItemDao).execute(items);
    }

    public void updateCheckboxItem(CheckboxItem item){
        new AsyncTaskUtil.updateCheckboxItemAsyncTask(mCheckboxItemDao).execute(item);
    }

    public void updateListCheckboxItem(List<CheckboxItem> items){
        new AsyncTaskUtil.updateListCheckboxItemAsyncTask(mCheckboxItemDao).execute(items);
    }
}
