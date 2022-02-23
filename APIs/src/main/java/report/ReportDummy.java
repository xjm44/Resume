package report;

public class ReportDummy implements Report {
    @Override
    public boolean send(String input) throws IllegalArgumentException {
        return true;
    }

    @Override
    public boolean checkDummy() {
        return true;
    }
}
