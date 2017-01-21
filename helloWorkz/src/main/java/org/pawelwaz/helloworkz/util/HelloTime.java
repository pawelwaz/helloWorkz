/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.util;

/**
 *
 * @author A20111
 */
public class HelloTime {
    
    public static String getYear(String input) {
        return input.substring(0, 4);
    }
    
    public static String getMonth(String input) {
        return input.substring(5, 7);
    }
    public static String getDay(String input) {
        return input.substring(8, 10);
    }
    
    public static String getHour(String input) {
        return input.substring(11, 13);
    }
    
    public static String getMinutes(String input) {
        return input.substring(14);
    }
    
}
