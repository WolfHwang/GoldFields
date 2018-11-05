package com.fanwe.hybrid.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.fanwe.hybrid.bean.MyContacts;
import com.fanwe.library.utils.LogUtil;

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
    public static ArrayList<MyContacts> getAllContacts(Context context) {
        ArrayList<MyContacts> contacts = new ArrayList<MyContacts>();

        Cursor cursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            //新建一个联系人实例
            MyContacts temp = new MyContacts();
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            //获取联系人姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            temp.name = name;

            //获取联系人电话号码
            Cursor phoneCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            while (phoneCursor.moveToNext()) {
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone = phone.replace("-", "");
                phone = phone.replace(" ", "");
                temp.phone = phone;
            }

            //获取联系人备注信息
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
            }

            contacts.add(temp);
            setWhatHappen(context, contacts);
        }
        return contacts;
    }

    private static void setWhatHappen(Context context, ArrayList<MyContacts> contacts) {
        LogUtil.d("See What Happen!");
        int contactsNum = contacts.size();
        if (contactsNum != (Integer) SharedPreferencesUtils.getParam(context, "contactsNum", 0)) {
            SharedPreferencesUtils.setParam(context, "isUpdate", true);
            SharedPreferencesUtils.setParam(context, "contactsNum", contactsNum);
        } else {
            SharedPreferencesUtils.setParam(context, "isUpdate", false);
            SharedPreferencesUtils.setParam(context, "contactsNum", contactsNum);
        }
    }
}
