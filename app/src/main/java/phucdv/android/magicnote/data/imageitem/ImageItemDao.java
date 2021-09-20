package phucdv.android.magicnote.data.imageitem;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ImageItemDao {
    @Query("SELECT * FROM image_tem WHERE parent_id = :parentId")
    public LiveData<List<ImageItem>> getImageItemsByParentId(long parentId);

    @Query("SELECT * FROM image_tem WHERE id = :id")
    public LiveData<ImageItem> getImageItemById(long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public Long insert(ImageItem imageItem);

    @Query("DELETE FROM image_tem WHERE id = :id")
    public void deleteById(long id);

    @Query("DELETE FROM image_tem WHERE parent_id = :parentId")
    public void deleteByParentId(long parentId);

    @Update(entity = ImageItem.class)
    public void update(ImageItem imageItem);
}
