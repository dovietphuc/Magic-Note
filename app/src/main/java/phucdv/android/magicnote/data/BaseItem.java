package phucdv.android.magicnote.data;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public abstract class BaseItem {
    @PrimaryKey(autoGenerate = true)
    protected long id;
    @ColumnInfo(name = "order_in_parent")
    protected long order_in_parent;
    @ColumnInfo(name = "parent_id")
    private long parent_id;

    public BaseItem(long order_in_parent, long parent_id){
        this.order_in_parent = order_in_parent;
        this.parent_id = parent_id;
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
}
