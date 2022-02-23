package model;

public interface MerchantFee {

    /**
     * the new conversion value, via the merchant fee
     * <b>Preconditions:</b><br>
     *     merchant fee and the value of the original conversion need to have.
     * <br>
     * <b>Postconditions:</b><br>
     *     result of new conversion value.
     * <br>
     *
     * @param fee The merchant fee applied to all currency exchanges
     * @param value The 'currency * rate' or the original conversion value.
     * @return return the result of new conversion value.
     */
    String currency(String fee, String value);

    /**
     * check if the range is 0 to 99 and if the input in an integer
     * <b>Preconditions:</b><br>
     *     none
     * <br>
     * <b>Postconditions:</b><br>
     *     result of the violation of the input
     * <br>
     *
     * @param fee The merchant fee applied to all currency exchanges
     * @return return the result for any violation
     */
    boolean checkRange(String fee);



}
