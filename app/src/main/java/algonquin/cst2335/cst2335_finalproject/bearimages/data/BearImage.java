/**
 * BearImage.java
 * <p>
 * This class represents a bear image entity in the BearImages application.
 * Each BearImage object contains the image data (as a byte array), height, width, and creation date.
 * <p>
 * The class is annotated with @Entity to define it as a Room entity for local database storage.
 * It contains properties corresponding to the columns in the database table.
 *
 * @author Hakan Okay
 * @version 1.0
 * @since JDK 20
 */

package algonquin.cst2335.cst2335_finalproject.bearimages.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class represents a bear image entity in the BearImages application.
 */
@Entity
public class BearImage {

    /**
     * The auto-generated primary key of the bear image in the local database.
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    /**
     * The image data of the bear image, stored as a byte array.
     */
    @ColumnInfo(name = "img")
    protected byte[] image;

    /**
     * The height of the bear image in pixels.
     */
    @ColumnInfo(name = "height")
    protected int height;

    /**
     * The width of the bear image in pixels.
     */
    @ColumnInfo(name = "width")
    protected int width;

    /**
     * The creation date of the bear image in the format yyyy-MM-dd.
     */
    @ColumnInfo(name = "created")
    protected String createdDate;

    /**
     * Default constructor for the BearImage class.
     */
    public BearImage() {
    }

    /**
     * Parameterized constructor for the BearImage class to initialize the image properties.
     *
     * @param i    The byte array representing the image data.
     * @param h    The height of the bear image in pixels.
     * @param w    The width of the bear image in pixels.
     * @param date The creation date of the bear image in the format yyyy-MM-dd.
     */
    public BearImage(byte[] i, int h, int w, String date) {
        this.image = i;
        this.height = h;
        this.width = w;
        this.createdDate = date;
    }

    /**
     * Retrieves the image data of the bear image.
     *
     * @return The byte array representing the image data.
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Sets the image data of the bear image.
     *
     * @param image The byte array representing the image data to set.
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    /**
     * Retrieves the height of the bear image.
     *
     * @return The height of the bear image in pixels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the bear image.
     *
     * @param height The height of the bear image in pixels to set.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Retrieves the width of the bear image.
     *
     * @return The width of the bear image in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the bear image.
     *
     * @param width The width of the bear image in pixels to set.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Retrieves the creation date of the bear image.
     *
     * @return The creation date of the bear image in the format yyyy-MM-dd.
     */
    public String getCreatedDate() {
        return createdDate;
    }


    /**
     * Returns the string representation of the BearImage object.
     *
     * @return The string representation of the BearImage object.
     */
    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
