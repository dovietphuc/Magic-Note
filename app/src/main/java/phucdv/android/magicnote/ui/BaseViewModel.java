package phucdv.android.magicnote.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import phucdv.android.magicnote.data.BaseItemRepository;
import phucdv.android.magicnote.data.noteitem.Note;
import phucdv.android.magicnote.data.noteitem.NoteRepository;

public abstract class BaseViewModel extends AndroidViewModel {

    protected NoteRepository mNoteRepository;
    protected BaseItemRepository mBaseItemRepository;
    protected LiveData<List<Note>> mNotes;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
        mBaseItemRepository = new BaseItemRepository(application);
        initNotes();
    }

    public LiveData<List<Note>> getListNotes(){
        return mNotes;
    }

    public abstract void initNotes();

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

    public void moveToArchive(Note note){
        mNoteRepository.moveToArchive(note);
    }

    public void moveToTrash(Note note){
        mNoteRepository.moveToTrash(note);
    }

    public void moveToProcessing(Note note){
        mNoteRepository.moveToProcessing(note);
    }

    public void pinOrUnpin(Note note){
        mNoteRepository.pinOrUnpin(note);
    }
}
