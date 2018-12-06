package com.szruito.goldfields.dao;

import com.szruito.goldfields.model.InitActModel;
import com.szruito.goldfields.model.InitActModel;

public class InitActModelDao
{
	public static boolean insertOrUpdate(InitActModel model)
	{
		return JsonDbModelDao.getInstance().insertOrUpdate(model);
	}

	public static InitActModel query()
	{
		return JsonDbModelDao.getInstance().query(InitActModel.class);
	}

	public static void delete()
	{
		JsonDbModelDao.getInstance().delete(InitActModel.class);
	}

}
