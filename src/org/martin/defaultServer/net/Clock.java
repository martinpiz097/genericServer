/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.defaultServer.net;

import java.util.Date;

/**
 *
 * @author martin
 */
public class Clock {
    /*private byte hours;
    private byte minutes;
    private byte seconds;
    private byte day;
    private byte month;
    private byte year;*/
    private StringBuilder sBuilder;
    private Date date;
    private static final Clock clock = new Clock();
    
    public static Clock getInstance(){
        return clock;
    }

    public Clock() {
        sBuilder = new StringBuilder();
    }
        
    public String getCurrentTime(){
        sBuilder.delete(0, sBuilder.length());
        date = new Date(System.currentTimeMillis());
        String strDate = date.toGMTString();
        
        sBuilder.append('[').append(strDate.substring(0, strDate.length()-4));
        sBuilder.append(']').append(' ');
        return sBuilder.toString();
    }
    
}
