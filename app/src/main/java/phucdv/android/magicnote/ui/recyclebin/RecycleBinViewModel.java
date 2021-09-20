package phucdv.android.magicnote.ui.recyclebin;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import phucdv.android.magicnote.data.BaseItemRepository;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteRepository;

public class RecycleBinViewModel extends AndroidViewModel {
    private NoteRepository mNoteRepository;
    private BaseItemRepository mBaseItemRepository;
    private LiveData<List<Note>> mTrashNotes;

    public RecycleBinViewModel(Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mTrashNotes = mNoteRepository.getNotesInTrash();
        mBaseItemRepository = new BaseItemRepository(application);
    }

    public LiveData<List<Note>> getNotesInTrash() { return mTrashNotes; }

    public void updateNote(Note note){
        mNoteRepository.updateNote(note);
    }

    public void updateListNote(List<Note> notes){
        mNoteRepository.updateListNote(notes);
    }

    public void deleteNote(long id){
        mNoteRepository.deleteNote(id);
        mBaseItemRepository.deleteTextByParentId(id);
        mBaseItemRepository.deleteCheckboxByParentId(id);
        mBaseItemRepository.deleteImageItemByParentId(id);
    }
}