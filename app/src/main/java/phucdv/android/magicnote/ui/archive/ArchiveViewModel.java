package phucdv.android.magicnote.ui.archive;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteRepository;

public class ArchiveViewModel extends AndroidViewModel {
    private NoteRepository mRepository;
    private LiveData<List<Note>> mArchiveNotes;

    public ArchiveViewModel(Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mArchiveNotes = mRepository.getNotesInArchive();
    }

    public LiveData<List<Note>> getNotesInArchive() { return mArchiveNotes; }

}