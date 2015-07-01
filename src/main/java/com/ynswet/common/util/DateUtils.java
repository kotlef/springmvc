package com.ynswet.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 时间工具类 扩展org.apache.commons.lang3.time.DateUtils
 * 
 * @author elfmatian
 */
public class DateUtils
    extends org.apache.commons.lang3.time.DateUtils
{

    /**
     * 存储SimpleDateFormat对应格式发的String类型
     */
    private static Map<String, SimpleDateFormat> map = new HashMap<String, SimpleDateFormat>();

    /**
     * 格式化时间，返回格式化后的时间字符串
     * 
     * <pre>
     * 例子,如有一个Date = 2012-08-09:
     * DateUtils.format(date,"yyyy-MM-dd") = "2012-08-09"
     * DateUtils.format(date,"yyyy年MM月dd日") = "2012年08月09日"
     * DateUtils.format(date,"") = null
     * DateUtils.format(date,null) = null
     * </pre>
     * 
     * @param date 时间
     * @param parsePatterns 格式化字符串
     * @return String
     */
    public static String format( Date date, String parsePatterns )
    {

        if ( StringUtils.isEmpty( parsePatterns ) || date == null )
        {
            return null;
        }

        return getSimpleDateFormat( parsePatterns ).format( date );
    }

    /**
     * 通过时间格式化字符串获取SimpleDateFormat
     * 
     * @param parsePatterns 时间格式化
     * @return {@link SimpleDateFormat}
     */
    public static SimpleDateFormat getSimpleDateFormat( String parsePatterns )
    {
        SimpleDateFormat dateFormat = null;
        if ( map.containsKey( parsePatterns ) )
        {
            dateFormat = map.get( parsePatterns );
        }
        else
        {
            dateFormat = new SimpleDateFormat( parsePatterns );
            map.put( parsePatterns, dateFormat );
        }
        return dateFormat;
    }

    /**
     * 通过时间格式化字符串获取SimpleDateFormat parsePatterns yyyy-MM-dd
     * 
     * @param time 时间格式化
     * @return {@link SimpleDateFormat}
     */
    public static Calendar convertStringToCalendar( String time )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
        Calendar calendarDate = Calendar.getInstance();
        Date dateObj = null;
        try
        {
            dateObj = sdf.parse( time );

            calendarDate.setTime( dateObj );
        }
        catch ( ParseException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return calendarDate;
    }
}
