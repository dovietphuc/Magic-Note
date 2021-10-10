package phucdv.android.magicnote.data.label;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "label",
indices = {@Index(value = {"name"}, unique = true)})
public class Label {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;

    public Label(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
