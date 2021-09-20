package phucdv.android.magicnote.ui.processing;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import java.util.List;

import phucdv.android.magicnote.data.BaseItemRepository;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteRepository;

public class ProcessingViewModel extends AndroidViewModel {
    private NoteRepository mNoteRepository;
    private LiveData<List<Note>> mProcessingNotes;
    private BaseItemRepository mBaseItemRepository;

    public ProcessingViewModel(Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mProcessingNotes = mNoteRepository.getNotesInProcessing();
        mBaseItemRepository = new BaseItemRepository(application);
    }

    public LiveData<List<Note>> getProcessingNotes() { return mProcessingNotes; }

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