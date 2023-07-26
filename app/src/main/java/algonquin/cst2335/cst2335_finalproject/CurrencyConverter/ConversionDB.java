package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper class for managing the conversions table.
 *
 * @version 1.0
 * @since 2023-07-26
 * @author Kang Dowon
 */
public class ConversionDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "conversion.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "conversions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_SOURCE_CURRENCY = "source_currency";
    public static final String COLUMN_TARGET_CURRENCY = "target_currency";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CONVERTED_AMOUNT = "converted_amount";

    /**
     * Constructs a ConversionDB object
     * @param context The context.
     */
    public ConversionDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SOURCE_CURRENCY + " TEXT, " +
                COLUMN_TARGET_CURRENCY + " TEXT, " +
                COLUMN_AMOUNT + " TEXT, " +
                COLUMN_CONVERTED_AMOUNT + " TEXT)";

        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableQuery = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(dropTableQuery);
        onCreate(db);
    }

}
