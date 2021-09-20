package phucdv.android.magicnote.data.imageitem;

import androidx.room.Entity;

import phucdv.android.magicnote.data.BaseItem;

@Entity(tableName = "image_tem")
public class ImageItem extends BaseItem {
    private String path;
    public ImageItem(long order_in_parent, long parent_id, String path) {
        super(order_in_parent, parent_id);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
