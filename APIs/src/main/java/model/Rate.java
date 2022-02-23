package model;

public interface Rate {

    /**
     * calculate the desired new rate
     * <b>Preconditions:</b><br>
     *     none
     * <br>
     * <b>Postconditions:</b><br>
     *     result of the new rate
     * <br>
     *
     * @param fee The merchant fee applied to all currency exchanges
     * @return return the result of the new rate
     */
    Float rate(String fee);
}
