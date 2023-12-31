package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The ConversionDAO class represents a data object for currency conversions.
 *
 * This class is used to store information about a single currency conversion, including the
 * source currency, target currency, original amount, and converted amount. It is used in the
 * app's data model to represent individual conversion entries in the RecyclerView in the
 * ConversionMain activity.
 *
 * @version 1.0
 * @since 2023-07-26
 * @author Kang Dowon
 */

@Entity
public class ConversionDAO implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo(name="SourceCurrency")
    private String sourceCurrency;

    @ColumnInfo(name="TargetCurrency")
    private String targetCurrency;

    @ColumnInfo(name="Amount")
    private String amount;

    @ColumnInfo(name="ConvertedAmount")
    private String convertedAmount;

    /**
     * Constructor for creating a new ConversionDAO object with the specified conversion details.
     *
     * @param sourceCurrency  The source currency code.
     * @param targetCurrency  The target currency code.
     * @param amount          The original amount to be converted.
     * @param convertedAmount The converted amount.
     */
    public ConversionDAO(String sourceCurrency, String targetCurrency, String amount, String convertedAmount) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    protected ConversionDAO(Parcel in) {
        id = in.readLong();
        sourceCurrency = in.readString();
        targetCurrency = in.readString();
        amount = in.readString();
        convertedAmount = in.readString();
    }

    public static final Creator<ConversionDAO> CREATOR = new Creator<ConversionDAO>() {
        @Override
        public ConversionDAO createFromParcel(Parcel in) {
            return new ConversionDAO(in);
        }

        @Override
        public ConversionDAO[] newArray(int size) {
            return new ConversionDAO[size];
        }
    };

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public String getAmount() {
        return amount;
    }

    public String getConvertedAmount() {
        return convertedAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sourceCurrency);
        dest.writeString(targetCurrency);
        dest.writeString(amount);
        dest.writeString(convertedAmount);
    }
}


