/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.timeutils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author mwlltr
 */
public class TimeUtils {
    /**
    public static String getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.ssss");
        Date date = new Date();
        String now = dateFormat.format(date);
        return now;
    }
   
    public static String getCurrentDateTimeGui(){
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = new Date();
        String now = dateFormat.format(date);
        return now;
    }
    **/
    
    public static LocalDateTime getNow(){
        LocalDateTime time = LocalDateTime.now();
        return time;
    }
}
