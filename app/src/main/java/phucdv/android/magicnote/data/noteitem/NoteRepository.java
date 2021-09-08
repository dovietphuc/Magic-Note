package phucdv.android.magicnote.data.noteitem;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import phucdv.android.magicnote.data.NoteRoomDatabase;
import phucdv.android.magicnote.data.checkboxitem.CheckboxItemDao;
import phucdv.android.magicnote.data.textitem.TextItemDao;
import phucdv.android.magicnote.noteinterface.AsyncResponse;
import phucdv.android.magicnote.util.AsyncTaskUtil;

public class NoteRepository {
    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;
    private LiveData<List<Note>> mProcessingNotes;
    private LiveData<List<Note>> mArchiveNotes;
    private LiveData<List<Note>> mTrashNotes;
    private MutableLiveData<Long> mLastNodeInsertedID;
    private LiveData<Note> mNote;

    public NoteRepository(Application application){
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getNotes();
        mProcessingNotes = mNoteDao.getNotesInProcessing();
        mArchiveNotes = mNoteDao.getNotesInArchive();
        mTrashNotes = mNoteDao.getNotesInTrash();
        mLastNodeInsertedID = new MutableLiveData<>();
    }

    public void initNote(long id){
        mNote = mNoteDao.getNotesById(id);
    }

    public LiveData<Note> getNote(){
        return mNote;
    }

    public LiveData<Long> getLastNodeInsertedID(){
        return mLastNodeInsertedID;
    }

    public LiveData<List<Note>> getAllNotes(){
        return mAllNotes;
    }

    public LiveData<List<Note>> getNotesInProcessing(){
        return mProcessingNotes;
    }

    public LiveData<List<Note>> getNotesInArchive(){
        return mArchiveNotes;
    }

    public LiveData<List<Note>> getNotesInTrash(){
        return mTrashNotes;
    }

    public void insert(Note note){
        new AsyncTaskUtil.insertNoteAsyncTask(mNoteDao, new AsyncResponse() {
            @Override
            public void processFinish(Object output) {
                mLastNodeInsertedID.setValue((Long) output);
            }
        }).execute(note);
    }

    public void insert(Note note, AsyncResponse response){
        new AsyncTaskUtil.insertNoteAsyncTask(mNoteDao, response).execute(note);
    }

    public void deleteNote(long id){
        new AsyncTaskUtil.deleteNoteAsyncTask(mNoteDao).execute(id);
    }

    public void updateNote(Note note){
        new AsyncTaskUtil.updateNoteAsyncTask(mNoteDao).execute(note);
    }

    public void updateListNote(List<Note> notes){
        new AsyncTaskUtil.updateListNoteAsyncTask(mNoteDao).execute(notes);
    }
}