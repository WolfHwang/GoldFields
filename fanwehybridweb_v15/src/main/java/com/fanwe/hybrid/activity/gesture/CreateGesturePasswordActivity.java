package com.fanwe.hybrid.activity.gesture;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


import cn.fanwe.yi.R;

import com.fanwe.gesture.customview.LockPatternView;
import com.fanwe.gesture.customview.LockPatternView.Cell;
import com.fanwe.gesture.customview.LockPatternView.DisplayMode;
import com.fanwe.gesture.utils.LockPatternUtils;
import com.fanwe.hybrid.activity.BaseActivity;
import com.fanwe.hybrid.app.App;
import com.fanwe.hybrid.dao.LoginSuccessModelDao;
import com.fanwe.hybrid.model.LoginSuccessModel;
import com.fanwe.library.utils.SDToast;

public class CreateGesturePasswordActivity extends BaseActivity implements OnClickListener
{
	private static final int ID_EMPTY_MESSAGE = -1;
	private static final String KEY_UI_STAGE = "uiStage";
	private static final String KEY_PATTERN_CHOICE = "chosenPattern";
	private LockPatternView mLockPatternView;
	private TextView mFooterRightButton;
	private TextView mFooterLeftButton;
	protected TextView mHeaderText;
	protected List<LockPatternView.Cell> mChosenPattern = null;
	private Stage mUiStage = Stage.Introduction;
	private View mPreviewViews[][] = new View[3][3];
	/**
	 * The patten used during the help screen to show how to draw a pattern.
	 */
	private final List<LockPatternView.Cell> mAnimatePattern = new ArrayList<LockPatternView.Cell>();

	/**
	 * The states of the left footer button.
	 */
	enum LeftButtonMode
	{
		Cancel(android.R.string.cancel, true), CancelDisabled(android.R.string.cancel, false), Retry(R.string.lockpattern_retry_button_text, true), RetryDisabled(
				R.string.lockpattern_retry_button_text, false), Gone(ID_EMPTY_MESSAGE, false);

		/**
		 * @param text
		 *            The displayed text for this mode.
		 * @param enabled
		 *            Whether the button should be enabled.
		 */
		LeftButtonMode(int text, boolean enabled)
		{
			this.text = text;
			this.enabled = enabled;
		}

		final int text;
		final boolean enabled;
	}

	/**
	 * The states of the right button.
	 */
	enum RightButtonMode
	{
		Continue(R.string.lockpattern_continue_button_text, true), ContinueDisabled(R.string.lockpattern_continue_button_text, false), Confirm(R.string.lockpattern_confirm_button_text, true), ConfirmDisabled(
				R.string.lockpattern_confirm_button_text, false), Ok(android.R.string.ok, true);

		/**
		 * @param text
		 *            The displayed text for this mode.
		 * @param enabled
		 *            Whether the button should be enabled.
		 */
		RightButtonMode(int text, boolean enabled)
		{
			this.text = text;
			this.enabled = enabled;
		}

		final int text;
		final boolean enabled;
	}

	/**
	 * Keep track internally of where the user is in choosing a pattern.
	 */
	protected enum Stage
	{

		Introduction(R.string.lockpattern_recording_intro_header, LeftButtonMode.Cancel, RightButtonMode.ContinueDisabled, ID_EMPTY_MESSAGE, true), HelpScreen(
				R.string.lockpattern_settings_help_how_to_record, LeftButtonMode.Gone, RightButtonMode.Ok, ID_EMPTY_MESSAGE, false), ChoiceTooShort(R.string.lockpattern_recording_incorrect_too_short,
				LeftButtonMode.Retry, RightButtonMode.ContinueDisabled, ID_EMPTY_MESSAGE, true), FirstChoiceValid(R.string.lockpattern_pattern_entered_header, LeftButtonMode.Retry,
				RightButtonMode.Continue, ID_EMPTY_MESSAGE, false), NeedToConfirm(R.string.lockpattern_need_to_confirm, LeftButtonMode.Cancel, RightButtonMode.ConfirmDisabled, ID_EMPTY_MESSAGE, true), ConfirmWrong(
				R.string.lockpattern_need_to_unlock_wrong, LeftButtonMode.Cancel, RightButtonMode.ConfirmDisabled, ID_EMPTY_MESSAGE, true), ChoiceConfirmed(
				R.string.lockpattern_pattern_confirmed_header, LeftButtonMode.Cancel, RightButtonMode.Confirm, ID_EMPTY_MESSAGE, false);

		/**
		 * @param headerMessage
		 *            The message displayed at the top.
		 * @param leftMode
		 *            The mode of the left button.
		 * @param rightMode
		 *            The mode of the right button.
		 * @param footerMessage
		 *            The footer message.
		 * @param patternEnabled
		 *            Whether the pattern widget is enabled.
		 */
		Stage(int headerMessage, LeftButtonMode leftMode, RightButtonMode rightMode, int footerMessage, boolean patternEnabled)
		{
			this.headerMessage = headerMessage;
			this.leftMode = leftMode;
			this.rightMode = rightMode;
			this.footerMessage = footerMessage;
			this.patternEnabled = patternEnabled;
		}

		final int headerMessage;
		final LeftButtonMode leftMode;
		final RightButtonMode rightMode;
		final int footerMessage;
		final boolean patternEnabled;
	}

	private void showToast(CharSequence message)
	{
		SDToast.showToast(message.toString());
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gesturepassword_create);
		// 初始化演示动画
		mAnimatePattern.add(LockPatternView.Cell.of(0, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(2, 0));
		mAnimatePattern.add(LockPatternView.Cell.of(0, 2));
		mAnimatePattern.add(LockPatternView.Cell.of(1, 2));
		mAnimatePattern.add(LockPatternView.Cell.of(2, 1));
		mAnimatePattern.add(LockPatternView.Cell.of(1, 0));
		mAnimatePattern.add(LockPatternView.Cell.of(0, 0));
		mAnimatePattern.add(LockPatternView.Cell.of(2, 2));
		mAnimatePattern.add(LockPatternView.Cell.of(0, 1));

		mLockPatternView = (LockPatternView) this.findViewById(R.id.gesturepwd_create_lockview);
		mHeaderText = (TextView) findViewById(R.id.gesturepwd_create_text);
		mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
		mLockPatternView.setTactileFeedbackEnabled(true);

		mFooterRightButton = (TextView) this.findViewById(R.id.right_btn);
		mFooterLeftButton = (TextView) this.findViewById(R.id.reset_btn);
		mFooterRightButton.setOnClickListener(this);
		mFooterLeftButton.setOnClickListener(this);
		initPreviewViews();
		if (savedInstanceState == null)
		{
			updateStage(Stage.Introduction);
			// updateStage(Stage.HelpScreen); 不需要引导效果
		} else
		{
			// restore from previous state
			final String patternString = savedInstanceState.getString(KEY_PATTERN_CHOICE);
			if (patternString != null)
			{
				mChosenPattern = LockPatternUtils.stringToPattern(patternString);
			}
			updateStage(Stage.values()[savedInstanceState.getInt(KEY_UI_STAGE)]);
		}

	}

	private void initPreviewViews()
	{
		mPreviewViews = new View[3][3];
		mPreviewViews[0][0] = findViewById(R.id.gesturepwd_setting_preview_0);
		mPreviewViews[0][1] = findViewById(R.id.gesturepwd_setting_preview_1);
		mPreviewViews[0][2] = findViewById(R.id.gesturepwd_setting_preview_2);
		mPreviewViews[1][0] = findViewById(R.id.gesturepwd_setting_preview_3);
		mPreviewViews[1][1] = findViewById(R.id.gesturepwd_setting_preview_4);
		mPreviewViews[1][2] = findViewById(R.id.gesturepwd_setting_preview_5);
		mPreviewViews[2][0] = findViewById(R.id.gesturepwd_setting_preview_6);
		mPreviewViews[2][1] = findViewById(R.id.gesturepwd_setting_preview_7);
		mPreviewViews[2][2] = findViewById(R.id.gesturepwd_setting_preview_8);
	}

	private void updatePreviewViews()
	{
		if (mChosenPattern == null)
			return;
		// Log.i("way", "result = " + mChosenPattern.toString());
		for (LockPatternView.Cell cell : mChosenPattern)
		{
			// Log.i("way", "cell.getRow() = " + cell.getRow() +
			// ", cell.getColumn() = " + cell.getColumn());
			mPreviewViews[cell.getRow()][cell.getColumn()].setBackgroundResource(R.drawable.gesture_create_grid_selected);

		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_UI_STAGE, mUiStage.ordinal());
		if (mChosenPattern != null)
		{
			outState.putString(KEY_PATTERN_CHOICE, LockPatternUtils.patternToString(mChosenPattern));
		}
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event)
	// {
	// if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
	// {
	// if (mUiStage == Stage.HelpScreen)
	// {
	// updateStage(Stage.Introduction);
	// return true;
	// }
	// }
	// if (keyCode == KeyEvent.KEYCODE_MENU && mUiStage ==
	// Stage.Introduction)
	// {
	// updateStage(Stage.HelpScreen);
	// return true;
	// }
	// return false;
	// }

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
			// Log.i("way", "result = " + pattern.toString());
			if (mUiStage == Stage.NeedToConfirm || mUiStage == Stage.ConfirmWrong)
			{
				if (mChosenPattern == null)
					throw new IllegalStateException("null chosen pattern in stage 'need to confirm");
				if (mChosenPattern.equals(pattern))
				{
					// updateStage(Stage.ChoiceConfirmed);
					saveChosenPatternAndFinish();
				} else
				{
					updateStage(Stage.ConfirmWrong);
				}
			} else if (mUiStage == Stage.Introduction || mUiStage == Stage.ChoiceTooShort)
			{
				if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE)
				{
					updateStage(Stage.ChoiceTooShort);
				} else
				{
					mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
					// updateStage(Stage.FirstChoiceValid);
					updateStage(Stage.NeedToConfirm);
				}
			} else
			{
				throw new IllegalStateException("Unexpected stage " + mUiStage + " when " + "entering the pattern.");
			}
		}

		public void onPatternCellAdded(List<Cell> pattern)
		{

		}

		private void patternInProgress()
		{
			mHeaderText.setText(R.string.lockpattern_recording_inprogress);
			mFooterLeftButton.setEnabled(false);
			mFooterRightButton.setEnabled(false);
		}
	};

	private void updateStage(Stage stage)
	{
		mUiStage = stage;
		if (stage == Stage.ChoiceTooShort)
		{
			mHeaderText.setText(getResources().getString(stage.headerMessage, LockPatternUtils.MIN_LOCK_PATTERN_SIZE));
		} else
		{
			mHeaderText.setText(stage.headerMessage);
		}

		if (stage.leftMode == LeftButtonMode.Gone)
		{
			mFooterLeftButton.setVisibility(View.GONE);
		} else
		{
			mFooterLeftButton.setVisibility(View.VISIBLE);
			mFooterLeftButton.setText(stage.leftMode.text);
			mFooterLeftButton.setEnabled(stage.leftMode.enabled);
		}

		mFooterRightButton.setText(stage.rightMode.text);
		mFooterRightButton.setEnabled(stage.rightMode.enabled);

		// same for whether the patten is enabled
		if (stage.patternEnabled)
		{
			mLockPatternView.enableInput();
		} else
		{
			mLockPatternView.disableInput();
		}

		mLockPatternView.setDisplayMode(DisplayMode.Correct);

		switch (mUiStage)
		{
		case Introduction:
			mLockPatternView.clearPattern();
			break;
		case HelpScreen:
			mLockPatternView.setPattern(DisplayMode.Animate, mAnimatePattern);
			break;
		case ChoiceTooShort:
			mLockPatternView.setDisplayMode(DisplayMode.Wrong);
			postClearPatternRunnable();
			break;
		case FirstChoiceValid:
			break;
		case NeedToConfirm:
			mLockPatternView.clearPattern();
			updatePreviewViews();
			break;
		case ConfirmWrong:
			mLockPatternView.setDisplayMode(DisplayMode.Wrong);
			postClearPatternRunnable();
			break;
		case ChoiceConfirmed:
			break;
		}

	}

	// clear the wrong pattern unless they have started a new one
	// already
	private void postClearPatternRunnable()
	{
		mLockPatternView.removeCallbacks(mClearPatternRunnable);
		mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.reset_btn:
			if (mUiStage.leftMode == LeftButtonMode.Retry)
			{
				mChosenPattern = null;
				mLockPatternView.clearPattern();
				updateStage(Stage.Introduction);
			} else if (mUiStage.leftMode == LeftButtonMode.Cancel)
			{
				// They are canceling the entire wizard
				finish();
			} else
			{
				throw new IllegalStateException("left footer button pressed, but stage of " + mUiStage + " doesn't make sense");
			}

			break;
		case R.id.right_btn:
			if (mUiStage.rightMode == RightButtonMode.Continue)
			{
				if (mUiStage != Stage.FirstChoiceValid)
				{
					throw new IllegalStateException("expected ui stage " + Stage.FirstChoiceValid + " when button is " + RightButtonMode.Continue);
				}
				updateStage(Stage.NeedToConfirm);
			} else if (mUiStage.rightMode == RightButtonMode.Confirm)
			{
				if (mUiStage != Stage.ChoiceConfirmed)
				{
					throw new IllegalStateException("expected ui stage " + Stage.ChoiceConfirmed + " when button is " + RightButtonMode.Confirm);
				}
				saveChosenPatternAndFinish();
			} else if (mUiStage.rightMode == RightButtonMode.Ok)
			{
				if (mUiStage != Stage.HelpScreen)
				{
					throw new IllegalStateException("Help screen is only mode with ok button, but " + "stage is " + mUiStage);
				}
				mLockPatternView.clearPattern();
				mLockPatternView.setDisplayMode(DisplayMode.Correct);
				updateStage(Stage.Introduction);
			}
			break;
		}
	}

	@SuppressWarnings("static-access")
	private void saveChosenPatternAndFinish()
	{
		showToast("手势密码设置成功");
		int code = getIntent().getExtras().getInt(EXTRA_CODE, 0);
		App.getApplication().mLockPatternUtils.saveLockPattern(mChosenPattern);
		String patternpassword = App.getApplication().mLockPatternUtils.patternToString(mChosenPattern);
		LoginSuccessModel model = LoginSuccessModelDao.queryModelCurrentLogin();
		if (model != null)
		{
			model.setPatternpassword(patternpassword);
			LoginSuccessModelDao.updateModelPatternPassword(model);
		}
		if (code == 1)
		{

		} else
		{
			Intent intent = new Intent(this, UnlockGesturePasswordActivity.class);
			startActivity(intent);
		}
		finish();
	}

	public static final String EXTRA_CODE = "extra_code";

	public static final class ExtraCodel
	{
		/** 直接关闭,不跳转 */
		public static final int EXTRA_CODE_1 = 1;
		/** 跳转 */
		public static final int EXTRA_CODE_0 = 0;
	}
}
