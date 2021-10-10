package phucdv.android.magicnote.data.noteandlabel;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import phucdv.android.magicnote.data.label.Label;
import phucdv.android.magicnote.data.noteitem.Note;

@Dao
public interface NoteLabelDao {

    @Query("SELECT * FROM note INNER JOIN note_label ON note.id = note_label.note_id WHERE label_id = :labelId")
    public LiveData<List<Note>> getNotesByLabelId(long labelId);

    @Query("SELECT * FROM label INNER JOIN note_label ON label.id = note_label.label_id WHERE note_id = :noteId")
    public LiveData<List<Label>> getLabelsByNoteId(long noteId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public Long insert(NoteLabel noteLabel);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public Long[] insertAll(List<NoteLabel> noteLabels);

    @Delete(entity = NoteLabel.class)
    public void delete(NoteLabel noteLabel);

    @Query("DELETE FROM note_label WHERE note_id = :note_id")
    public void deleteForNote(long note_id);

    @Delete(entity = NoteLabel.class)
    public void deleteAll(List<NoteLabel> noteLabels);
}
