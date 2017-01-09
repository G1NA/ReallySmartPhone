package helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gina4_000 on 10/1/2017.
 */

public class NumFixer {

    private static Pattern digits = Pattern.compile("[0-9]*");

    public static String fixNumber(String number){
        String[] splitted = number.split(" ");
        if(splitted.length == 1){
            // either it will be the word of a digit and it will return a digit or it will return ""
            String fixed = toDigit(number);
            if(!fixed.equals("")){
                return fixed;
            }else {
                fixed = number.replaceAll("\\D+", "");
                Matcher m = digits.matcher(fixed);
                if (m.matches()) {
                    return fixed;
                } else {
                    return "";
                }
            }
        }else{
            String fixed = "";
            for(String s: splitted){
                Matcher m = digits.matcher(s);
                if(m.matches()){
                    fixed += s;
                }else{
                    fixed += toDigit(s);
                }
            }
            return fixed;
        }

    }

    private static String toDigit(String s){

        if(s.equals("zero")){
            return "0";
        }else if(s.equals("one")) {
            return "1";
        }else if(s.equals("two")) {
            return "2";
        }else if(s.equals("three")) {
            return "3";
        }else if(s.equals("four")) {
            return "4";
        }else if(s.equals("five")) {
            return "5";
        }else if(s.equals("six")) {
            return "6";
        }else if(s.equals("seven")) {
            return "7";
        }else if(s.equals("eight")) {
            return "8";
        }else if(s.equals("nine")) {
            return "9";
        }else {
            return "";
        }
    }
}
