package com.szruito.goldfields.activity.gesture;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.widget.TextView;


import cn.fanwe.yi.R;

import com.fanwe.gesture.customview.LockPatternView;
import com.fanwe.gesture.customview.LockPatternView.Cell;
import com.fanwe.gesture.utils.LockPatternUtils;
import com.szruito.goldfields.activity.BaseActivity;
import com.szruito.goldfields.activity.MainActivity;
import com.szruito.goldfields.app.App;
import com.szruito.goldfields.dao.LoginSuccessModelDao;
import com.szruito.goldfields.model.LoginSuccessModel;
import com.fanwe.library.utils.SDToast;
import com.szruito.goldfields.activity.MainActivity;
import com.szruito.goldfields.app.App;
import com.szruito.goldfields.dao.LoginSuccessModelDao;
import com.szruito.goldfields.model.LoginSuccessModel;

public class UnlockGesturePasswordActivity extends BaseActivity
{
	private LockPatternView mLockPatternView;
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	private CountDownTimer mCountdownTimer = null;
	private Handler mHandler = new Handler();
	private TextView mHeadTextView, mForgetPassword, mChangePassword;
	private Animation mShakeAnim;

	private void showToast(CharSequence message)
	{
		SDToast.showToast(message.toString());
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesturepassword_unlock);

		mLockPatternView = (LockPatternView) this.findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
		mForgetPassword = (TextView) findViewById(R.id.forget_password);
		mForgetPassword.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// 提示对话框
				AlertDialog.Builder builder = new Builder(UnlockGesturePasswordActivity.this);
				builder.setTitle("忘记密码将清除手势并注销当前账号,确定忘记密码?").setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						SDToast.showToast("清除密码成功!");
						CookieManager.getInstance().removeSessionCookie();
						App.getApplication().mLockPatternUtils.clearLock();
						LoginSuccessModel model = LoginSuccessModelDao.queryModelCurrentLogin();
						if (model != null)
						{
							LoginSuccessModelDao.deleteModel(String.valueOf(model.getUserid()));
						}

						Intent intent = new Intent(UnlockGesturePasswordActivity.this, MainActivity.class);
						startActivity(intent);

						finish();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}).show();

			}
		});
		mChangePassword = (TextView) findViewById(R.id.change_password);
		mChangePassword.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// 提示对话框
				AlertDialog.Builder builder = new Builder(UnlockGesturePasswordActivity.this);
				builder.setTitle("确定关闭手势?").setPositiveButton("确定", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Intent intent = new Intent(UnlockGesturePasswordActivity.this, VerifyLockGesturePasswordActivity.class);
						startActivity(intent);
						finish();

					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}).show();
			}
		});
		mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mCountdownTimer != null)
			mCountdownTimer.cancel();
	}

	private Runnable mClearPatternRunnable = new Runnable()
	{
		public void run()
		{
			mLockPatternView.clearPattern();
		}
	};

	protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener()
	{

		public void onPatternStart()
		{
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
			patternInProgress();
		}

		public void onPatternCleared()
		{
			mLockPatternView.removeCallbacks(mClearPatternRunnable);
		}

		public void onPatternDetected(List<LockPatternView.Cell> pattern)
		{
			if (pattern == null)
				return;
			if (App.getApplication().mLockPatternUtils.checkPattern(pattern))
			{
				showToast("解锁成功");
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
				Intent intent = new Intent(UnlockGesturePasswordActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			} else
			{
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
				if (pattern.size() >= LockPatternUtils.MIN_PATTERN_REGISTER_FAIL)
				{
					mFailedPatternAttemptsSinceLastTimeout++;
					int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
					if (retry >= 0)
					{
						if (retry == 0)
							showToast("您已5次输错密码，请30秒后再试");
						mHeadTextView.setText("密码错误，还可以再输入" + retry + "次");
						mHeadTextView.setTextColor(Color.RED);
						mHeadTextView.startAnimation(mShakeAnim);
					}

				} else
				{
					showToast("输入长度不够，请重试");
				}

				if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT)
				{
					mHandler.postDelayed(attemptLockout, 2000);
				} else
				{
					mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
				}
			}
		}

		public void onPatternCellAdded(List<Cell> pattern)
		{

		}

		private void patternInProgress()
		{
		}
	};
	Runnable attemptLockout = new Runnable()
	{

		@Override
		public void run()
		{
			mLockPatternView.clearPattern();
			mLockPatternView.setEnabled(false);
			mCountdownTimer = new CountDownTimer(LockPatternUtils.FAILED_ATTEMPT_TIMEOUT_MS + 1, 1000)
			{

				@Override
				public void onTick(long millisUntilFinished)
				{
					int secondsRemaining = (int) (millisUntilFinished / 1000) - 1;
					if (secondsRemaining > 0)
					{
						mHeadTextView.setText(secondsRemaining + " 秒后重试");
					} else
					{
						mHeadTextView.setText("请绘制手势密码");
						mHeadTextView.setTextColor(Color.WHITE);
					}

				}

				@Override
				public void onFinish()
				{
					mLockPatternView.setEnabled(true);
					mFailedPatternAttemptsSinceLastTimeout = 0;
				}
			}.start();
		}
	};
}
