/**
 * BearImageDao.java
 * <p>
 * The BearImageDao interface defines the Data Access Object (DAO) for the BearImage entity in the BearImages application.
 * It provides methods for inserting a bear image into the database, querying all bear images from the database, and deleting
 * a bear image from the database.
 *
 * @author Hakan Okay
 * @version 1.0
 * @since JDK 20
 */

package algonquin.cst2335.cst2335_finalproject.bearimages.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * The BearImageDao interface defines the Data Access Object (DAO) for the BearImage entity in the BearImages application.
 * It provides methods for inserting a bear image into the database, querying all bear images from the database, and deleting
 * a bear image from the database.
 */
@Dao
public interface BearImageDao {

    /**
     * Inserts a bear image into the local database.
     *
     * @param bi The BearImage object to be inserted.
     * @return The ID of the newly inserted bear image in the database.
     */
    @Insert
    long insertImage(BearImage bi);

    /**
     * Retrieves all bear images stored in the local database.
     *
     * @return A list of BearImage objects representing all the bear images in the database.
     */
    @Query("SELECT * FROM BearImage")
    List<BearImage> getAllImages();

    /**
     * Deletes a bear image from the local database.
     *
     * @param bi The BearImage object to be deleted.
     */
    @Delete
    void deleteImage(BearImage bi);
}
