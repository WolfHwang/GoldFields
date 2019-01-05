package com.szruito.goldfields.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.szruito.goldfields.R;
import com.szruito.goldfields.activity.MainActivity;
import com.szruito.goldfields.constant.ApkConstant;

public class CustomEditDialog extends Dialog {
    /**
     * 确认和取消按钮
     */
    private Button negtiveBn, positiveBn;
    private Spinner spinner;
    private EditText editText;
    private Context context;
    private static final String[] mCountries = {"192.168.2.172:8889", "192.168.2.154:8889", "清空"};
    private ArrayAdapter<String> adapter;
    /**
     * 按钮之间的分割线
     */
    private View columnLineView;

    private String positive;
    private String negtive;

    /**
     * 底部是否只有一个按钮
     */
    private boolean isSingle = false;
    private String url;

    public CustomEditDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_url);

        initView();
        refreshView();
        initEvent();
    }

    /**
     * 设置确定取消按钮的回调
     */
    public CustomEditDialog.OnClickBottomListener onClickBottomListener;

    public CustomEditDialog setOnClickBottomListener(CustomEditDialog.OnClickBottomListener onClickBottomListener) {
        this.onClickBottomListener = onClickBottomListener;
        return this;
    }

    public interface OnClickBottomListener {
        /**
         * 点击确定按钮事件
         */
        public void onPositiveClick();

        /**
         * 点击取消按钮事件
         */
        public void onNegtiveClick();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {
        adapter = new ArrayAdapter<String>(context, R.layout.custom_spiner_text_item, mCountries);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selUrl = mCountries[position];
                if (selUrl.equals("清空")) {
                    editText.setText("");
                } else {
                    editText.setText(selUrl);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //设置确定按钮被点击后，向外界提供监听
        positiveBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    if (!editText.getText().toString().trim().isEmpty()) {
                        ApkConstant.SERVER_URL = "http://" + editText.getText().toString().trim();
                        context.startActivity(new Intent(context, MainActivity.class));
                    } else {
                        Toast.makeText(context, "请选择Url", Toast.LENGTH_SHORT).show();
                    }
//                    onClickBottomListener.onPositiveClick();
                }
            }
        });
        //设置确定按钮被点击后，向外界提供监听
        negtiveBn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickBottomListener != null) {
                    onClickBottomListener.onNegtiveClick();
                }
            }
        });
    }

    private void refreshView() {
        //如果设置按钮的文字
        if (!TextUtils.isEmpty(positive)) {
            positiveBn.setText(positive);
        } else {
            positiveBn.setText("确定");
        }
        if (!TextUtils.isEmpty(negtive)) {
            negtiveBn.setText(negtive);
        } else {
            negtiveBn.setText("取消");
        }
        /**
         * 只显示一个按钮的时候隐藏取消按钮，回掉只执行确定的事件
         */
        if (isSingle) {
            columnLineView.setVisibility(View.GONE);
            negtiveBn.setVisibility(View.GONE);
        } else {
            negtiveBn.setVisibility(View.VISIBLE);
            columnLineView.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        negtiveBn = findViewById(R.id.negtive);
        positiveBn = findViewById(R.id.positive);
        spinner = findViewById(R.id.spinner);
        editText = findViewById(R.id.edit_text);
        columnLineView = findViewById(R.id.column_line);
    }

    public String getPositive() {
        return positive;
    }

    public CustomEditDialog setPositive(String positive) {
        this.positive = positive;
        return this;
    }

    public String getNegtive() {
        return negtive;
    }

    public CustomEditDialog setNegtive(String negtive) {
        this.negtive = negtive;
        return this;
    }

    public boolean isSingle() {
        return isSingle;
    }

    public CustomEditDialog setSingle(boolean single) {
        isSingle = single;
        return this;
    }
}
