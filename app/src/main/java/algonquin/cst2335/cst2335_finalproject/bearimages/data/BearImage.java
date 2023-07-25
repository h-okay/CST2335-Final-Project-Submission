package algonquin.cst2335.cst2335_finalproject.bearimages.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class BearImage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "img")
    protected byte[] image;

    @ColumnInfo(name = "height")
    protected int height;

    @ColumnInfo(name = "width")
    protected int width;

    @ColumnInfo(name = "created")
    protected String createdDate;

    public BearImage() {
    }

    public BearImage(byte[] i, int h, int w, String date) {
        this.image = i;
        this.height = h;
        this.width = w;
        this.createdDate = date;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int weight) {
        this.width = weight;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreated_date(String created_date) {
        this.createdDate = created_date;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
