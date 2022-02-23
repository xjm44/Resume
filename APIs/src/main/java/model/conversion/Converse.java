package model.conversion;

import model.cryptocurrencies.Crypto;

public interface Converse {

    /**
     * conversion using symbols.
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     result of conversion between them via symbol.
     * <br>
     *
     * @param cur1 The base cryptocurrency. May not be null.
     * @param cur2 The cryptocurrency to be compared. May not be null.
     * @param amount The cryptocurrency's amount to be compared. May not be null.
     * @param fee The merchant fee applied to all currency exchanges
     * @return return the result of conversion.
     * @throws IllegalArgumentException If any of the preconditions are violated.
     */
    String converseSymbol(String cur1, String cur2, String amount, String fee)throws IllegalArgumentException;

    /**
     * conversion using id.
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     result of conversion between them via id.
     * <br>
     *
     * @param cur1 The base cryptocurrency. May not be null.
     * @param cur2 The cryptocurrency to be compared. May not be null.
     * @param amount The cryptocurrency's amount to be compared. May not be null.
     * @param fee The merchant fee applied to all currency exchanges
     * @return return the result of conversion.
     * @throws IllegalArgumentException If any of the preconditions are violated.
     */
    String converseID(String cur1, String cur2, String amount, String fee)throws IllegalArgumentException;


}
