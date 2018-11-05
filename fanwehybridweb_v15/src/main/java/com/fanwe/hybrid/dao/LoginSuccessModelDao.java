package com.fanwe.hybrid.dao;

import java.util.List;

import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import android.text.TextUtils;

import com.fanwe.hybrid.db.DbManagerX;
import com.fanwe.hybrid.model.LoginSuccessModel;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2015-12-15 下午4:31:05 类说明
 */
public class LoginSuccessModelDao
{
	public static void insertOrUpdateModel2(LoginSuccessModel model)
	{
		try
		{
			List<LoginSuccessModel> listModel = DbManagerX.getDb().selector(LoginSuccessModel.class).where("userid", "=", model.getUserid()).findAll();
			if (listModel != null && listModel.size() == 1)
			{
				LoginSuccessModel loginSuccessModel = listModel.get(0);
				if (!TextUtils.isEmpty(loginSuccessModel.getPatternpassword()))
				{
					model.setPatternpassword(loginSuccessModel.getPatternpassword());
				}

				KeyValue value1 = new KeyValue("user_name", model.getUser_name());
				KeyValue value2 = new KeyValue("patternpassword", model.getPatternpassword());
				KeyValue value3 = new KeyValue("is_current", model.getIs_current());

				DbManagerX.getDb().update(LoginSuccessModel.class, WhereBuilder.b("userid", "=", model.getUserid()), value1, value2, value3);

			} else
			{
				DbManagerX.getDb().save(model);
			}
		} catch (DbException e)
		{
			e.printStackTrace();
		}
	}

	public static void deleteModel(String id)
	{
		try
		{
			DbManagerX.getDb().delete(LoginSuccessModel.class, WhereBuilder.b("userid", "=", id));
		} catch (DbException e)
		{
			e.printStackTrace();
		}
	}

	public static void updateModelPatternPassword(LoginSuccessModel model)
	{
		try
		{
			KeyValue keyvalue = new KeyValue("patternpassword", model.getPatternpassword());
			DbManagerX.getDb().update(LoginSuccessModel.class, WhereBuilder.b("userid", "=", model.getUserid()), keyvalue);
		} catch (DbException e)
		{
			e.printStackTrace();
		}
	}

	public static LoginSuccessModel queryModelCurrentLogin()
	{
		try
		{
			List<LoginSuccessModel> listModel = DbManagerX.getDb().selector(LoginSuccessModel.class).where("is_current", "=", 1).findAll();
			if (listModel != null && listModel.size() == 1)
			{
				LoginSuccessModel model = listModel.get(0);
				return model;
			} else
			{
				// 出错了
			}
		} catch (DbException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
