package phucdv.android.magicnote.data;

import androidx.room.ColumnInfo;

public abstract class BaseItem {
    @ColumnInfo(name = "order_in_parent")
    protected long order_in_parent;

    public long getOrder_in_parent() {
        return order_in_parent;
    }

    public void setOrder_in_parent(long order_in_parent) {
        this.order_in_parent = order_in_parent;
    }
}
