package phucdv.android.magicnote.data.textitem;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TextItemDao {
    @Query("SELECT * FROM text_item")
    public LiveData<List<TextItem>> getAll();

    @Query("SELECT * FROM text_item WHERE id = :id")
    public LiveData<List<TextItem>> getTextItemForId(long id);

    @Query("SELECT * FROM text_item WHERE parent_id = :parentId")
    public LiveData<List<TextItem>> getTextItemForParentId(long parentId);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public Long insert(TextItem textItem);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public Long[] insertAll(List<TextItem> textItems);

    @Query("DELETE FROM text_item WHERE id = :id")
    public void deleteById(long id);

    @Query("DELETE FROM text_item WHERE parent_id = :parentId")
    public void deleteByParentId(long parentId);

    @Update(entity = TextItem.class)
    public void update(TextItem textItem);

    @Update(entity = TextItem.class)
    public void updateAll(List<TextItem> textItems);
}
