package phucdv.android.magicnote.ui.recyclebin;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import phucdv.android.magicnote.data.BaseItemRepository;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteRepository;
import phucdv.android.magicnote.ui.BaseViewModel;

public class RecycleBinViewModel extends BaseViewModel {

    public RecycleBinViewModel(Application application) {
        super(application);
    }

    @Override
    public void initNotes() {
        mNotes = mNoteRepository.getNotesInTrash();
    }

    public LiveData<List<Note>> getNotesInTrash() { return getListNotes(); }
}