package phucdv.android.magicnote.ui.recyclebin;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteRepository;

public class RecycleBinViewModel extends AndroidViewModel {
    private NoteRepository mRepository;
    private LiveData<List<Note>> mTrashNotes;

    public RecycleBinViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mTrashNotes = mRepository.getNotesInTrash();
    }

    public LiveData<List<Note>> getNotesInTrash() { return mTrashNotes; }
}