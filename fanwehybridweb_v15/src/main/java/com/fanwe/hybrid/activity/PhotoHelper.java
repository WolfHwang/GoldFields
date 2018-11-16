package com.fanwe.hybrid.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.ValueCallback;

import com.fanwe.hybrid.dialog.BotPhotoPopupView;
import com.fanwe.hybrid.dialog.DialogCropPhoto;
import com.fanwe.hybrid.model.CutPhotoModel;
import com.fanwe.hybrid.utils.FileUtils;
import com.fanwe.hybrid.utils.SDImageUtil;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zerowolf on 2018/11/5.
 */

public class PhotoHelper {
    public static PhotoHelper mPhotoHelper;
    private static final String mPath = "/sdcard/myImage/";
    private static final String mFileName = "avatar.jpg";

    public PhotoHelper() {

    }

    public static PhotoHelper getInstace() {
        if (mPhotoHelper == null) {
            mPhotoHelper = new PhotoHelper();
        }
        return mPhotoHelper;
    }

    /******************************  图片处理  ************************************/
    public void fileChooserResultcode(Context context, Intent data, int resultCode, ValueCallback<Uri> uploadMessage, String cameraFilePath, OnCallBack onCallBack) {
        if (null == uploadMessage)
            return;
        Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
        if (result == null && data == null && resultCode == RESULT_OK) {
            File cameraFile = new File(cameraFilePath);
            if (cameraFile.exists()) {
                result = Uri.fromFile(cameraFile);
                // Broadcast to the media scanner that we have a new photo
                // so it will be added into the gallery for the user.
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
            }
        }
        if (result == null) {
            uploadMessage.onReceiveValue(null);
//            uploadMessage = null;
            onCallBack.callBack();

            return;
        }
        String file_path = FileUtils.getPath(context, result);
        if (TextUtils.isEmpty(file_path)) {
            uploadMessage.onReceiveValue(null);
            onCallBack.callBack();
//            uploadMessage = null;
            return;
        }
        Uri uri = Uri.fromFile(new File(file_path));

        uploadMessage.onReceiveValue(uri);
        onCallBack.callBack();
//        uploadMessage = null;

    }


    public void requestCodeTakePhoto(Activity context, Intent data, int resultCode, BotPhotoPopupView mBotPhotoPopupView, CutPhotoModel mCut_model) {
        if (resultCode == RESULT_OK) {
            String path = BotPhotoPopupView.getmTakePhotoPath();
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            dealImageSize(context, bitmap, mBotPhotoPopupView, mCut_model);
        }
    }

    public void dealImageSize(Activity context, Bitmap bitmap, BotPhotoPopupView mBotPhotoPopupView, CutPhotoModel mCut_model) {
        SDImageUtil.dealImageCompress(mPath, mFileName, bitmap, 100);
        File file = new File(mPath, mFileName);
        if (file != null && file.exists()) {
            DialogCropPhoto dialog = new DialogCropPhoto(context, file.getPath(), (DialogCropPhoto.OnCropBitmapListner) context, mCut_model);
            dialog.show();
        }
        onDismissPop(mBotPhotoPopupView);
    }

    public void requestCodeSelectPhoto(Activity context, Intent data, int resultCode, BotPhotoPopupView mBotPhotoPopupView, CutPhotoModel mCut_model) {
        if (resultCode == RESULT_OK) {
            String path = SDImageUtil.getImageFilePathFromIntent(data, context);
            DialogCropPhoto dialog = new DialogCropPhoto(context, path, (DialogCropPhoto.OnCropBitmapListner) context, mCut_model);
            dialog.show();
            onDismissPop(mBotPhotoPopupView);
        }
    }


    public void onDismissPop(BotPhotoPopupView mBotPhotoPopupView) {
        if (mBotPhotoPopupView != null && mBotPhotoPopupView.isShowing()) {
            mBotPhotoPopupView.dismiss();
        }
    }

    /******************************  图片处理  ************************************/


    /******************************  回调  ************************************/

    public interface OnCallBack {
        void callBack();
    }

}
