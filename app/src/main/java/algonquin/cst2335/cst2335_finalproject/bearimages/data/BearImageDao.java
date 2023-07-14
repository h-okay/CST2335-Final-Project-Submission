package algonquin.cst2335.cst2335_finalproject.bearimages.data;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BearImageDao {

    @Insert
    long insertImage(BearImage bi);

    @Query("SELECT * FROM BearImage")
    List<BearImage> getAllImages();

    @Delete
    void deleteImage(BearImage bi);
}
