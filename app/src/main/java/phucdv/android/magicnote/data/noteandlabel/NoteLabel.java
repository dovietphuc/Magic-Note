package phucdv.android.magicnote.data.noteandlabel;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_label",
        indices = {@Index(value = {"note_id", "label_id"}, unique = true)})
public class NoteLabel {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private long note_id;
    private long label_id;

    public NoteLabel(long note_id, long label_id) {
        this.note_id = note_id;
        this.label_id = label_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNote_id() {
        return note_id;
    }

    public void setNote_id(long note_id) {
        this.note_id = note_id;
    }

    public long getLabel_id() {
        return label_id;
    }

    public void setLabel_id(long label_id) {
        this.label_id = label_id;
    }
}
