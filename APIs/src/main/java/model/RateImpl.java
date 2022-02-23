package model;

public class RateImpl implements Rate {
    private float fee;
    @Override
    public Float rate(String fee) {
        try {
            // the 'fee' itself has been checked of its range and if it is an integer. So I choose to make it as a floating number here for a better calculation later.
            this.fee = Float.parseFloat(fee);

            // user is able to change the rate in the future
            return (1 - (this.fee / 100));
        }catch (Exception e){
            return null;
        }
    }
}
