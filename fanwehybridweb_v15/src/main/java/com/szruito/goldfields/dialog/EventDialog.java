package com.szruito.goldfields.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.szruito.goldfields.activity.FullscreenActivity;
import com.szruito.goldfields.app.App;
import com.szruito.goldfields.bean.EventModel;
import com.szruito.goldfields.utils.DateFormatUtil;
import com.szruito.goldfields.app.App;
import com.szruito.goldfields.bean.EventModel;

import java.util.Calendar;

import com.szruito.goldfields.R;


/**
 * @author Cw
 * @date 2017/7/10
 */
public class EventDialog extends AppCompatDialog implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView tv_time;
    private EditText tv_content;
    private Button bt_save;

    private EventModel mInfo;
    private DatePickerDialog mDateDialog;
    private TimePickerDialog mTimeDialog;
    private String mDate;

    public EventDialog(Context context, EventModel data) {
        super(context, R.style.NoTitleDialog);
        this.mInfo = data;
        init();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String month = monthOfYear < 9 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);
        String day = dayOfMonth < 10 ? ("0" + dayOfMonth) : ("" + dayOfMonth);
        mDate = year + "-" + month + "-" + day;
        showTimeDialog();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = hourOfDay + ":" + minute;
        //yyyy-MM-dd HH:mm
        tv_time.setText(mDate + " " + time);
    }

    private void init() {
        setContentView(R.layout.widget_dialog_add_event);
        tv_time = findViewById(R.id.tv_time);
        tv_content = findViewById(R.id.tv_content);
        bt_save = findViewById(R.id.bt_save);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = tv_content.getText().toString();
                String time = DateFormatUtil.parseTime(tv_time.getText().toString());
                if (TextUtils.isEmpty(time)) {
                    return;
                }
                if (mInfo == null) {
                    //添加模式
                    EventModel eventModel = new EventModel();
                    System.out.println("卢本伟牛逼!" + time);
                    eventModel.setTime(time);
                    eventModel.setContent(content);

                    if (mOnUpdateListener != null) {
                        mOnUpdateListener.onUpdate(true, eventModel);
                    }
                } else {
                    //更新模式
                    EventModel eventModel = new EventModel();
                    eventModel.setId(mInfo.getId());
                    System.out.println("卢本伟牛逼!2" + mInfo.getId());
                    eventModel.setTime(time);
                    eventModel.setContent(content);

                    if (mOnUpdateListener != null) {
                        mOnUpdateListener.onUpdate(false, eventModel);
                    }
                }
                Toast.makeText(App.sContext, "添加日程成功！", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });
        setCanceledOnTouchOutside(false);
        if (mInfo != null) {
            tv_time.setText(DateFormatUtil.transTime(mInfo.getTime()));
            tv_content.setText(mInfo.getContent());
        }
    }

    private void showDateDialog() {
        if (mDateDialog == null) {
            Calendar now = Calendar.getInstance();
            mDateDialog = new DatePickerDialog(getContext()
                    , AlertDialog.THEME_DEVICE_DEFAULT_DARK, this
                    , now.get(Calendar.YEAR)
                    , now.get(Calendar.MONTH)
                    , now.get(Calendar.DAY_OF_MONTH));
        }
        mDateDialog.show();
    }

    private void showTimeDialog() {
        if (mTimeDialog == null) {
            mTimeDialog = new TimePickerDialog(getContext()
                    , AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, 0, 0, true);
        }
        mTimeDialog.show();
    }

    private OnUpdateListener mOnUpdateListener = null;

    public interface OnUpdateListener {
        void onUpdate(boolean isNew, EventModel eventModel);
    }

    public void setOnUpdateListener(OnUpdateListener listener) {
        this.mOnUpdateListener = listener;
    }
}
