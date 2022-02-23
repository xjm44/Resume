package model;

public class MerchantFeeImpl implements MerchantFee {

    private float con_value;
    private Rate newRate= new RateImpl();

    @Override
    public String currency(String fee, String value){

        // check for valid range first or if it is an integer
        if(!checkRange(fee)){
            return null;
        }

        try{
            con_value = Float.parseFloat(value);
            // I choose to return the new rate as a floating number is because the original conversion value is not an int
            float temp = newRate.rate(fee);
            float total = temp*con_value;
            return Float.toString(total);

        }catch (Exception e){
            return null;
        }

    }

    @Override
    public boolean checkRange(String fee) {
        try {
            int range = Integer.parseInt(fee);
            return !(range < 0) && !(range > 99);
        }catch (Exception e){
            return false;
        }
    }


//    public static void main(String[] args){
//        MerchantFee mf =new MerchantFeeImpl();
//        System.out.println(mf.currency("1a2","1a"));
//
//    }

}
