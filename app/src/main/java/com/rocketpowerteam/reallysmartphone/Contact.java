package com.rocketpowerteam.reallysmartphone;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gina4_000 on 7/1/2017.
 */

public class Contact {
    String name;
    String number;
    Pattern digits = Pattern.compile("[0-9]*");
    Pattern numbers = Pattern.compile("zero|one|two|three|four|five|six|seven|eight|nine");

    public void setName(String name){ this.name = name;}
    public void setNumber(String number){ this.number = number;}
    public String getName(){ return this.name; }
    public String getNumber(){ return this.number; }

    public void fixNumber(){
        String[] splitted = number.split(" ");
        if(splitted.length == 1){
            // either it will be the word of a digit and it will return a digit or it will return ""
            String fixed = toDigit(number);
            if(!fixed.equals("")){
                number = fixed;
            }else {
                number = number.replaceAll("\\D+", "");
                Matcher m = digits.matcher(number);
                if (m.matches()) {
                    return;
                } else {
                    number = "";
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
            number = fixed;
        }

    }

    private String toDigit(String s){

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
