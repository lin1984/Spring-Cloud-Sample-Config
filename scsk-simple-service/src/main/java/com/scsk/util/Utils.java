package com.scsk.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具クラス
 */
public class Utils {
    
    /**
     * 
     * @param value チェック値
     * @return trueの場合、非nullと非空、その以外false
     */
    public static boolean isNotNullAndEmpty(String value) {
        return value != null && !value.isEmpty();
    }
    
    /**
     * 
     * @param dateFormat
     * @return
     */
    public static String currentTime(String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(new Date());
    }
}
