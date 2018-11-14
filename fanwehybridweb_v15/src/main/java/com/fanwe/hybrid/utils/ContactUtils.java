package com.fanwe.hybrid.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.fanwe.hybrid.activity.ContactBean;

import java.util.ArrayList;

/**
 * 获取通讯录工具类
 * <p>
 * [{
 * "name": "马叉虫",
 * "note": "呵呵呵呵",
 * "phone": "18585514449"
 * },
 * {
 * "name": "深圳的我",
 * "phone": "18818778695"
 * },
 * {
 * "name": "郭哈哈哈哈哈",
 * "phone": "13333333334"
 * },
 * ......
 * ]
 */

public class ContactUtils {
    public static ArrayList<ContactBean> getAllContacts(Context context) {
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        ArrayList<ContactBean> beanArrayList = new ArrayList<>();

        while (cursor.moveToNext()) {
            //获取联系人姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            if (name != null && !"".equals(name)) {  //保证此条通讯录即有姓名又有电话号码
                //获取联系人电话号码
                String contactId = cursor.getString(cursor
                        .getColumnIndex(ContactsContract.Contacts._ID));
                Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);


                if (phoneCursor.moveToFirst()) {        //有只取第一条, 没有Pass
                    String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phone = phone.replace("-", "");
                    phone = phone.replace(" ", "");

                    ContactBean contactBean = new ContactBean();
                    contactBean.setName(name);
                    contactBean.setPhone(phone);
//                    Logger.i(contactBean.toString());
                    beanArrayList.add(contactBean);
                }
            }
        }

        return beanArrayList;
    }
}


          /*  //获取联系人备注信息
            Cursor noteCursor = context.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    new String[]{ContactsContract.Data._ID, ContactsContract.CommonDataKinds.Nickname.NAME},
                    ContactsContract.Data.CONTACT_ID + "=?" + " AND " + ContactsContract.Data.MIMETYPE + "='"
                            + ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE + "'",
                    new String[]{contactId}, null);
            if (noteCursor.moveToFirst()) {
                do {
                    String note = noteCursor.getString(noteCursor
                            .getColumnIndex(ContactsContract.CommonDataKinds.Nickname.NAME));
                    temp.note = note;
                    Log.i("note:", note);
                } while (noteCursor.moveToNext());
            }*/
