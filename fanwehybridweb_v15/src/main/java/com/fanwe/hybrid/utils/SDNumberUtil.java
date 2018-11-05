package com.fanwe.hybrid.utils;

import android.os.Build;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import java.math.BigDecimal;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class SDNumberUtil
{
    /**
     * 默认使用的测试机版本 5.1
     */
    public static final int DEFAULT_TEST_VERSION = Build.VERSION_CODES.LOLLIPOP;

    public static double distance(double lat1, double lon1, double lat2, double lon2)
    {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        double miles = dist * 60 * 1.1515 * 1.609344 * 1000;
        return miles;
    }

    // 将角度转换为弧度
    static double deg2rad(double degree)
    {
        return degree / 180 * Math.PI;
    }

    // 将弧度转换为角度
    static double rad2deg(double radian)
    {
        return radian * 180 / Math.PI;
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     *            需要四舍五入的数字
     * @param scale
     *            小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double value, int scale)
    {
        if (scale < 0)
        {
            scale = 0;
        }
        BigDecimal bdValue = new BigDecimal(value);
        BigDecimal oneValue = new BigDecimal(1);
        return bdValue.divide(oneValue, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 乘法
     *
     * @param value1
     * @param value2
     * @param scale
     *            保留小数位
     * @return
     */
    public static double multiply(double value1, double value2, int scale)
    {
        double result = 0;
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));

        result = b1.multiply(b2).doubleValue();
        result = round(result, scale);
        return result;
    }

    /**
     * 加法
     *
     * @param value1
     * @param value2
     * @param scale
     *            保留小数位
     * @return
     */
    public static double add(double value1, double value2, int scale)
    {
        double result = 0;
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));

        result = b1.add(b2).doubleValue();
        result = round(result, scale);
        return result;
    }

    /**
     * 减法
     *
     * @param value1
     * @param value2
     * @param scale
     *            保留小数位
     * @return
     */
    public static double subtract(double value1, double value2, int scale)
    {
        double result = 0;
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));

        result = b1.subtract(b2).doubleValue();
        result = round(result, scale);
        return result;
    }

    /**
     * 除法
     *
     * @param value1
     * @param value2
     * @param scale
     *            保留小数位
     * @return
     */
    public static double divide(double value1, double value2, int scale)
    {
        double result = 0;
        BigDecimal b1 = new BigDecimal(String.valueOf(value1));
        BigDecimal b2 = new BigDecimal(String.valueOf(value2));

        result = b1.divide(b2).doubleValue();
        result = round(result, scale);
        return result;
    }

    /**
     * 字符串转整数，默认返回-1
     */
    public static int string2Integer(String number)
    {
        if(!TextUtils.isEmpty(number))
        {
            try
            {
                return  Integer.valueOf(number);
            } catch (NumberFormatException e)
            {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    public static double roundHalfUp(double value, int scale)
    {
        if (scale < 0)
        {
            scale = 0;
        }
        BigDecimal bdValue = new BigDecimal(value);
        BigDecimal oneValue = new BigDecimal(1);
        return bdValue.divide(oneValue, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 字符串转Float，默认返回-1
     */
    public static float string2Float(String number)
    {
        if(!TextUtils.isEmpty(number))
        {
            try
            {
                return  Float.valueOf(number);
            } catch (NumberFormatException e)
            {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }

    /**
     * 找出个位的索引,如 1245.512  个位(5)的索引为3
     * @param n
     * @return
     */
    public static int getIntegerIndex(Number n){
        final String temp=String.valueOf(n);
        if(TextUtils.isEmpty(temp)){
            return -1;
        }else {
            if(temp.contains(".")){
                return temp.indexOf(".")-1;
            }else {
                return temp.length()-1;
            }
        }
    }

    public static int getIntegerIndex(String n){
        if(TextUtils.isEmpty(n)){
            return -1;
        }else {
            if(n.contains(".")){
                return n.indexOf(".")-1;
            }else {
                return n.length()-1;
            }
        }
    }

    /**
     * 判断字符串是否为正数
     * @param price
     * @return
     */
    public static boolean isPositive(String price)
    {
        boolean b = false;
        try
        {
            if (!TextUtils.isEmpty(price) && Double.parseDouble(price) > 0)
            {
                b=true;
            } else
            {
                b=false;
            }
        } catch (NumberFormatException e)
        {
            e.printStackTrace();
        } finally
        {
            return b;
        }
    }

    /**
     * 常用的价格格式:  ¥15.00  15为大字体
     * 默认当前使用是5.1的机子进行调试
     * @param size
     * @param price
     * @return
     */
    public static SpannableString getFormatedPrice(int size, String price)
    {
        return getFormatedPrice(size, DEFAULT_TEST_VERSION, price);
    }

    /**
     *  常用的价格格式:  ¥15.00  15为大字体
     *  @param size
     * @param price
     * @return
     */
    public static SpannableString getFormatedPrice(int size,int testSdkVersion,String price){
        return getFormatedPrice(size,testSdkVersion,price,null);
    }

    /**
     * 常用的价格格式:  ¥15.00  15为大字体
     * @param size
     * @param price
     * @param testSdkVersion 当前测试机的sdk版本
     * @param prefix 价格的前缀比如:售价 售价¥15.00
     * @return
     */
    public static SpannableString getFormatedPrice(int size,int testSdkVersion,String price,String prefix){
        final StringBuilder sb=new StringBuilder();
        if(TextUtils.isEmpty(price)){
            return null;
        }
        if(price.contains(".")){
            final int index=price.indexOf(".");
            if (price.length()-index==2){//只有一位小数
                price=price+"0";
            } else if (price.length()-index>3){
                final double p=Double.parseDouble(price);
                price = String.valueOf(SDNumberUtil.roundHalfUp(p, 2));
            }
            sb.append("¥").append(price);
        }else {
            sb.append("¥").append(price).append(".00");
        }
        SpannableString s = null;
        final AbsoluteSizeSpan span = new AbsoluteSizeSpan(compatAbsoluteSizeSpan(size,testSdkVersion));
        if(!TextUtils.isEmpty(prefix))
        {
            s = new SpannableString(prefix+sb);
            s.setSpan(span, prefix.length()+1, String.valueOf(prefix+sb).indexOf("."), SPAN_INCLUSIVE_INCLUSIVE);
        }else
        {
            s = new SpannableString(sb);
            s.setSpan(span, 1, String.valueOf(sb).indexOf("."), SPAN_INCLUSIVE_INCLUSIVE);
        }
        return s;
    }

    /**
     *  常用的价格格式:  ¥15.00  无局部大字体
     * @param price
     * @return
     */
    public static String getFormatedPrice(String price){
        final StringBuilder sb=new StringBuilder();
        if(TextUtils.isEmpty(price)){
            return null;
        }
        if(price.contains(".")){
            final int index=price.indexOf(".");
            if (price.length()-index==2){//只有一位小数
                price=price+"0";
            }else if (price.length()-index>3){
                final double p=Double.parseDouble(price);
                price = String.valueOf(SDNumberUtil.roundHalfUp(p, 2));
            }
            sb.append("¥").append(price);
        }else {
            sb.append("¥").append(price).append(".00");
        }
        return sb.toString();
    }

    /**
     * @param size
     * @param testSdkVersion 调试时使用的测试机的sdk版本;
     * @return
     */
    public static int compatAbsoluteSizeSpan(int size, int testSdkVersion)
    {
        int curSdkVersion = Build.VERSION.SDK_INT;
        if ((curSdkVersion < Build.VERSION_CODES.LOLLIPOP) && testSdkVersion >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (size > 75)
            {
                return (int) (size * 0.65);
            }else if (size<50)
            {
                return (int) (size * 1.25);
            }else
            {
                return (int) 55;
            }

        } else
        {
            return size;
        }

    }

    /**
     * 判断数量是否显示,
     * 注:0不显示
     * @param count
     * @return
     */
    public static boolean isValidCount(String count)
    {
        if ("0".equals(count) || "false".equals(count) || "null".equals(count) || TextUtils.isEmpty(count))
        {
            return false;
        }else
        {
            return true;
        }
    }

}
