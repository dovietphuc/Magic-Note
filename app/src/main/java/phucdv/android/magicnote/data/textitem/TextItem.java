package phucdv.android.magicnote.data.textitem;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import phucdv.android.magicnote.data.BaseItem;

@Entity(tableName = "text_item")
public class TextItem extends BaseItem {
    private String content;

    public TextItem(long parent_id, long order_in_parent, String content) {
        super(order_in_parent, parent_id);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
