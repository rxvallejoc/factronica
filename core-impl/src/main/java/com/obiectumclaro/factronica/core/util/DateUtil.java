/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.core.util;

import java.util.Date;

/**
 *
 * @author marco
 */
public class DateUtil {
    public static  String getFormatedDate(Date date, String pattern){
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
