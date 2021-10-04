package phucdv.android.magicnote.data.label;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import phucdv.android.magicnote.data.NoteRoomDatabase;

public class LabelRepository {
    private LabelDao mLabelDao;

    public LabelRepository(Application application){
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mLabelDao = db.labelDao();
    }

    public LiveData<List<Label>> getAllLabels(){
        return mLabelDao.getAllLabels();
    }

    public LiveData<Label> getLabelById(long id){
        return mLabelDao.getLabelById(id);
    }

    public LiveData<Label> getLabelByName(String name){
        return mLabelDao.getLabelByName(name);
    }

    public void insert(Label label){

    }

    public void insertAll(List<Label> labels){

    }

    public void delete(Label label){

    }
}
