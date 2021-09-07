package phucdv.android.magicnote.data.noteitem;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import phucdv.android.magicnote.data.BaseItem;

@Entity(tableName = "note")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private long time_create;
    private long time_last_update;
    private boolean is_archive;
    private boolean is_deleted;
    private long order_in_parent;
    private boolean is_pinned;
    private long color;

    public Note(String title, long time_create, long time_last_update, boolean is_archive, boolean is_deleted, long order_in_parent, boolean is_pinned, long color) {
        this.title = title;
        this.time_create = time_create;
        this.time_last_update = time_last_update;
        this.is_archive = is_archive;
        this.is_deleted = is_deleted;
        this.order_in_parent = order_in_parent;
        this.is_pinned = is_pinned;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime_create() {
        return time_create;
    }

    public void setTime_create(long time_create) {
        this.time_create = time_create;
    }

    public long getTime_last_update() {
        return time_last_update;
    }

    public void setTime_last_update(long time_last_update) {
        this.time_last_update = time_last_update;
    }

    public boolean isIs_archive() {
        return is_archive;
    }

    public void setIs_archive(boolean is_archive) {
        this.is_archive = is_archive;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public long getOrder_in_parent() {
        return order_in_parent;
    }

    public void setOrder_in_parent(long order_in_parent) {
        this.order_in_parent = order_in_parent;
    }

    public boolean isIs_pinned() {
        return is_pinned;
    }

    public void setIs_pinned(boolean is_pinned) {
        this.is_pinned = is_pinned;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }
}
