package algonquin.cst2335.cst2335_finalproject.CurrencyConverter;

/**
 * @author Kang Dowon
 *
 * Represents a conversion between two currencies.
 */
class ConversionDAO {
    private String sourceCurrency;
    private String targetCurrency;
    private String amount;
    private String convertedAmount;

    public ConversionDAO(String sourceCurrency, String targetCurrency, String amount, String convertedAmount) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

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
}

