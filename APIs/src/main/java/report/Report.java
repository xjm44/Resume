package report;

public interface Report {

    /**
     * <b>Preconditions:</b><br>
     *     valid token
     * <br>
     * <b>Postconditions:</b><br>
     *     output in the gui is sent as email.
     * <br>
     *
     * @param input email content.
     * @return if email is sent.
     * @throws IllegalArgumentException If any of the other preconditions are violated.
     */
    boolean send(String input)throws IllegalArgumentException;

    /**
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     return if the report is running online or offline
     * <br>
     *
     * @return if it is dummy.
     */
    boolean checkDummy();


}
