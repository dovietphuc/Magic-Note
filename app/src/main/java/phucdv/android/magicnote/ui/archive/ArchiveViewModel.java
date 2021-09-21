package phucdv.android.magicnote.ui.archive;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import java.util.List;

import phucdv.android.magicnote.data.BaseItemRepository;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteRepository;
import phucdv.android.magicnote.ui.BaseViewModel;

public class ArchiveViewModel extends BaseViewModel {

    public ArchiveViewModel(Application application) {
        super(application);
    }

    @Override
    public void initNotes() {
        mNotes = mNoteRepository.getNotesInArchive();
    }

    public LiveData<List<Note>> getNotesInArchive() { return getListNotes(); }
}