package phucdv.android.magicnote.data;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;

public abstract class BaseItem {
    @PrimaryKey
    protected long id;
    @ColumnInfo(name = "order_in_parent")
    protected long order_in_parent;
    @ColumnInfo(name = "parent_id")
    private long parent_id;
    @ColumnInfo(name = "uid")
    private String uid;
    private long time_stamp_update;

    public BaseItem(long order_in_parent, long parent_id, String uid){
        this.id = Calendar.getInstance().getTimeInMillis();
        this.order_in_parent = order_in_parent;
        this.parent_id = parent_id;
        this.uid = uid;
        this.time_stamp_update = Calendar.getInstance().getTimeInMillis();
    }

    @Ignore
    public BaseItem(){
        this.id = Calendar.getInstance().getTimeInMillis();
        this.time_stamp_update = Calendar.getInstance().getTimeInMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrder_in_parent() {
        return order_in_parent;
    }

    public void setOrder_in_parent(long order_in_parent) {
        this.order_in_parent = order_in_parent;
    }

    public long getParent_id() {
        return parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTime_stamp_update() {
        return time_stamp_update;
    }

    public void setTime_stamp_update(long time_stamp_update) {
        this.time_stamp_update = time_stamp_update;
    }
}
