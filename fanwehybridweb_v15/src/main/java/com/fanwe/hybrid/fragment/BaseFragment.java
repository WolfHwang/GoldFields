package com.fanwe.hybrid.fragment;

import android.app.Activity;
import android.app.Dialog;

import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.activity.MainActivity;
import com.fanwe.hybrid.dialog.SDProgressDialog;
import com.fanwe.hybrid.event.SDBaseEvent;
import com.fanwe.library.fragment.SDBaseFragment;

/**
 * @author 作者 yhz
 * @version 创建时间：2015-2-5 上午10:27:37 类说明 基类Fragment
 */
public class BaseFragment extends SDBaseFragment
{
	protected Dialog mDialog;

	protected void showDialog()
	{
		if (mDialog == null)
		{
			mDialog = new SDProgressDialog(getActivity());
			mDialog.show();
		}
	}

	protected void dimissDialog()
	{
		if (mDialog != null)
		{
			mDialog.dismiss();
			mDialog = null;
		}
	}

	public void onEventMainThread(SDBaseEvent event)
	{

	}


	public BaseActivity getBaseActivity()
	{
		return (BaseActivity) getActivity();
	}

	public MainActivity getMainActivity()
	{
		Activity activity = getActivity();
		if (activity != null)
		{
			if (activity instanceof MainActivity)
			{
				return ((MainActivity) activity);
			}
		}
		return null;
	}

	@Override
	protected int onCreateContentView()
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
