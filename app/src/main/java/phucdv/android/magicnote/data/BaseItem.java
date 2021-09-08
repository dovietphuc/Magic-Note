package phucdv.android.magicnote.data;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

public abstract class BaseItem {
    @PrimaryKey(autoGenerate = true)
    protected long id;
    @ColumnInfo(name = "order_in_parent")
    protected long order_in_parent;

    public BaseItem(long order_in_parent){
        this.order_in_parent = order_in_parent;
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
}
