package com.fanwe.hybrid.activity.gesture;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.fanwe.gesture.customview.LockPatternView;
import com.fanwe.gesture.customview.LockPatternView.Cell;
import com.fanwe.gesture.utils.LockPatternUtils;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.activity.MainActivity;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.dao.LoginSuccessModelDao;
import com.fanwe.hybrid.model.LoginSuccessModel;
import com.fanwe.library.utils.SDToast;

import java.util.List;

import cn.fanwe.yi.R;

public class VerifyLockGesturePasswordActivity extends BaseActivity
{
	private LockPatternView mLockPatternView;
	private int mFailedPatternAttemptsSinceLastTimeout = 0;
	private CountDownTimer mCountdownTimer = null;
	private Handler mHandler = new Handler();
	private TextView mHeadTextView;
	private Animation mShakeAnim;

	private void showToast(CharSequence message)
	{
		SDToast.showToast(message.toString());
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesturepassword_verifyunlock);

		mLockPatternView = (LockPatternView) this.findViewById(R.id.gesturepwd_unlock_lockview);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);
		mHeadTextView = (TextView) findViewById(R.id.gesturepwd_unlock_text);
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
				showToast("关闭验证成功");
				mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
				App.getApplication().mLockPatternUtils.clearLock();
				LoginSuccessModel model = LoginSuccessModelDao.queryModelCurrentLogin();
				if (model != null)
				{
					LoginSuccessModelDao.deleteModel(String.valueOf(model.getId()));
				}
				Intent intent = new Intent(VerifyLockGesturePasswordActivity.this, MainActivity.class);
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
						mHeadTextView.setText("请绘制旧手势密码");
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
