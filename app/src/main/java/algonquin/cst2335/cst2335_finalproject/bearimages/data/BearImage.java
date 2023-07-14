package algonquin.cst2335.cst2335_finalproject.bearimages.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Blob;
import java.sql.Date;

@Entity
public class BearImage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "img")
    protected Blob image;

    @ColumnInfo(name = "name")
    protected String name;

    @ColumnInfo(name = "created")
    protected Date created_date;

    public BearImage() {
    }

    public BearImage(Blob i, String n, Date date) {
        this.image = i;
        this.name = n;
        this.created_date = date;
    }

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }
}
