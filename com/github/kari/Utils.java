
package com.github.kari;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {
    public static String formatTimeStamp(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
    }
}