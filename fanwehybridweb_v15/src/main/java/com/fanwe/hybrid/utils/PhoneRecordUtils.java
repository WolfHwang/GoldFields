package com.fanwe.hybrid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.fanwe.hybrid.bean.PhoneRecordBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * 获取通话记录根据类
 * <p>
 * [{
 * "name": "瑞通公司项目",
 * "number": "18681450824",
 * "time": "2018-08-30 14:51:44"
 * },
 * {
 * "name": "深圳的我",
 * "number": "18818778695",
 * "time": "2018-09-02 19:08:29"
 * },
 * ......
 * ]
 */

public class PhoneRecordUtils {
    private static ArrayList<PhoneRecordBean> list;

    public static ArrayList<PhoneRecordBean> getRecord(Context context) {
        list = new ArrayList<>();

        @SuppressLint("MissingPermission") Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                PhoneRecordBean prb = new PhoneRecordBean();
                //获取号码
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                //格式转换 Long -> Date -> String
                SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE))));
                //获取呼叫时间
                String time = sfd.format(date);
                //获取联系人
                String name = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.CACHED_NAME));
                long longTime = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE)));
                prb.name = name;
                prb.time = longTime;
                prb.number = number;
                list.add(prb);
            } while (cursor.moveToNext());

        }
        //排序
        //自定义比较器对list进行比较排序。
        // 先对电话号码排序，再对通话时间记录进行排序，方便后面的筛选
        ComparatorRecord comparator = new ComparatorRecord();
        Collections.sort(list, comparator);

        //筛选
        //去除重复出相同号码最近一个时间的通话记录
        ArrayList<PhoneRecordBean> phoneRecordBeans = removeDuplicteRecord(list);
        ComparatorRecord2 comparator2 = new ComparatorRecord2();
        Collections.sort(phoneRecordBeans, comparator2);

        long lastest = phoneRecordBeans.get(phoneRecordBeans.size() - 1).time;
        if (lastest != (Long) SPUtils.getParam(context, "latest", 0L)) {
            SPUtils.setParam(context, "latest", phoneRecordBeans.get(phoneRecordBeans.size() - 1).time);
            SPUtils.setParam(context, "update", true);
        } else {
            SPUtils.setParam(context, "latest", phoneRecordBeans.get(phoneRecordBeans.size() - 1).time);
            SPUtils.setParam(context, "update", false);
        }
        return phoneRecordBeans;
    }

    /**
     * 将列表中重复的用户移除，重复指的是number：电话号码相同
     *
     * @param list
     * @return
     */
    public static ArrayList<PhoneRecordBean> removeDuplicteRecord(ArrayList<PhoneRecordBean> list) {
        ArrayList<PhoneRecordBean> anotherList = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i - 1).number.equals(list.get(i).number) && !list.get(i).number.equals(list.get(i + 1).number)) {
                anotherList.add(list.get(i));
            } else {
            }
        }
        return anotherList;
    }

    /**
     * 自定义比较器类
     */
    static class ComparatorRecord implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            PhoneRecordBean bean1 = (PhoneRecordBean) o1;
            PhoneRecordBean bean2 = (PhoneRecordBean) o2;
            //首先比较号码
            int flag = bean1.number.compareTo(bean2.number);
            if (flag == 0) {
                //接下来比较时间
                return bean1.time > bean2.time ? 1 : -1;
            } else {
                return flag;
            }
        }
    }

    static class ComparatorRecord2 implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            PhoneRecordBean bean1 = (PhoneRecordBean) o1;
            PhoneRecordBean bean2 = (PhoneRecordBean) o2;
            return bean1.time > bean2.time ? 1 : (bean1.time < bean2.time ? -1 : 0);
        }
    }
}
