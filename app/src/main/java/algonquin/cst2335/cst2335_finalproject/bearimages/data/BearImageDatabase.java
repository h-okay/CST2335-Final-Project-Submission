package algonquin.cst2335.cst2335_finalproject.bearimages.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BearImage.class}, version = 1)
public abstract class BearImageDatabase extends RoomDatabase {

    public abstract BearImageDao bearImageDao();
}
