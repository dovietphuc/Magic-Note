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
    }

    public void deleteNote(Note note){
        mNoteRepository.deleteNote(note);
    }

    public void deleteListNote(List<Note> notes){
        mNoteRepository.deleteListNote(notes);
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

    public void moveToArchive(List<Note> notes){
        mNoteRepository.moveToArchive(notes);
    }

    public void moveToTrash(List<Note> notes){
        mNoteRepository.moveToTrash(notes);
    }

    public void moveToProcessing(List<Note> notes){
        mNoteRepository.moveToProcessing(notes);
    }

    public void pinOrUnpin(Note note){
        mNoteRepository.pinOrUnpin(note);
    }

    public void pin(Note note){
        mNoteRepository.pin(note);
    }

    public void unpin(Note note){
        mNoteRepository.unpin(note);
    }

    public void updateColor(int color, Note note){
        mNoteRepository.updateColor(color, note);
    }

    public void pin(List<Note> notes){
        mNoteRepository.pin(notes);
    }

    public void unpin(List<Note> notes){
        mNoteRepository.unpin(notes);
    }

    public void updateColor(int color, List<Note> notes){
        mNoteRepository.updateColor(color, notes);
    }
}
