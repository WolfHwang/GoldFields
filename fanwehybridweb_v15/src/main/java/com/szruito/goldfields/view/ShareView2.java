package com.szruito.goldfields.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.szruito.goldfields.R;
import com.szruito.goldfields.utils.PhoneUtils;

public class ShareView2 extends LinearLayout {

    private final int IMAGE_WIDTH = 750;
    private final int IMAGE_HEIGHT = 1350;
    // 长图的宽度，默认为屏幕宽度
    private int longPictureWidth;
    private Context context;

    private ImageView mImageView;
    private LinearLayout mLL;

    public ShareView2(@NonNull Context context) {

        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;

        View layout = View.inflate(getContext(), R.layout.share_view_layout2, this);

        mImageView = layout.findViewById(R.id.mIV);
        mLL = layout.findViewById(R.id.ll_qrcode);

        layoutView(mImageView);
    }

    /**
     * 手动测量view宽高
     */
    private void layoutView(View v) {
        int width = PhoneUtils.getPhoneWid(context);
        int height = PhoneUtils.getPhoneHei(context);

        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    private Bitmap getLinearLayoutBitmap(LinearLayout linearLayout, int w, int h) {
        Bitmap originBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(originBitmap);
        linearLayout.draw(canvas);
        return null;
    }

    /**
     * 设置相关信息
     *
     * @param bitmap
     */

    public void setMyImage(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }

    /**
     * 生成图片
     *
     * @return
     */
    public Bitmap createImage() {
        //由于直接new出来的view是不会走测量、布局、绘制的方法的，所以需要我们手动去调这些方法，不然生成的图片就是黑色的。

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(IMAGE_WIDTH, MeasureSpec.AT_MOST);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(IMAGE_HEIGHT, MeasureSpec.AT_MOST);

        measure(widthMeasureSpec, heightMeasureSpec);

        layout(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

        Bitmap bitmap = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        draw(canvas);

        return bitmap;
    }

}

