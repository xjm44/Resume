package view;

import database.Database;
import model.auth.Auth;
import model.conversion.Converse;
import model.detail.Detail;
import report.Report;

public interface Gui {

    /**
     * show the page.
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     able to display the gui.
     * <br>
     *
     */
    void basicPage();

    /**
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     have a Auth obj;
     * <br>
     *
     * @param auth auth obj.
     */
    void setAuth(Auth auth);
    /**
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     have a Converse obj.
     * <br>
     *
     * @param con Converse obj.
     */
    void setConverse(Converse con);
    /**
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     have a Detail obj.
     * <br>
     *
     * @param detail Detail obj.
     */
    void setDetail(Detail detail);

    /**
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     have a Report obj.
     * <br>
     *
     * @param report Report obj.
     */
    void setReport(Report report);

    /**
     * <b>Preconditions:</b><br>
     *     none.
     * <br>
     * <b>Postconditions:</b><br>
     *     have a Database obj.
     * <br>
     *
     * @param db Database obj.
     */
    void setDatabase(Database db);






}
