package phucdv.android.magicnote.ui.archive;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import phucdv.android.magicnote.data.BaseItemRepository;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteRepository;

public class ArchiveViewModel extends AndroidViewModel {
    private NoteRepository mNoteRepository;
    private LiveData<List<Note>> mArchiveNotes;
    private BaseItemRepository mBaseItemRepository;

    public ArchiveViewModel(Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mArchiveNotes = mNoteRepository.getNotesInArchive();
        mBaseItemRepository = new BaseItemRepository(application);
    }

    public LiveData<List<Note>> getNotesInArchive() { return mArchiveNotes; }

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
    }
}