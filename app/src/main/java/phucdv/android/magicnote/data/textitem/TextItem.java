package phucdv.android.magicnote.data.textitem;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import phucdv.android.magicnote.data.BaseItem;

@Entity(tableName = "text_item")
public class TextItem extends BaseItem {
    private long parent_id;
    private String content;

    public TextItem(long parent_id, long order_in_parent, String content) {
        super(order_in_parent);
        this.parent_id = parent_id;
        this.content = content;
    }

    public long getParent_id() {
        return parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
