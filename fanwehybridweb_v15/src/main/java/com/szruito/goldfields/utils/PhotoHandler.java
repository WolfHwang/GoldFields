package com.szruito.goldfields.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.fanwe.lib.utils.FFileUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.UriFileUtils;

import java.io.File;

public class PhotoHandler extends OnActivityResultHandler
{
    public static final String TAKE_PHOTO_FILE_DIR_NAME = "take_photo";
    public static final int REQUEST_CODE_GET_PHOTO_FROM_CAMERA = 16542;
    public static final int REQUEST_CODE_GET_PHOTO_FROM_ALBUM = REQUEST_CODE_GET_PHOTO_FROM_CAMERA + 1;

    private PhotoHandlerListener listener;
    private File takePhotoFile;
    private File takePhotoDir;

    public void setListener(PhotoHandlerListener listener)
    {
        this.listener = listener;
    }

    public PhotoHandler(Fragment mFragment)
    {
        super(mFragment);
        init();
    }

    public PhotoHandler(FragmentActivity mActivity)
    {
        super(mActivity);
        init();
    }

    private void init()
    {
        takePhotoDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!takePhotoDir.exists())
        {
            takePhotoDir.mkdirs();
        }
    }

    public void getPhotoFromAlbum()
    {
        try
        {
            Intent intent = com.fanwe.library.utils.SDIntentUtil.getIntentSelectLocalImage2();
            startActivityForResult(intent, REQUEST_CODE_GET_PHOTO_FROM_ALBUM);
        } catch (android.content.ActivityNotFoundException e)
        {
            SDToast.showToast("ActivityNotFoundException");
        }
    }

    public void getPhotoFromCamera()
    {
        if (takePhotoDir == null)
        {
            if (listener != null)
            {
                listener.onFailure("获取SD卡缓存目录失败");
            }
        } else
        {
            File takePhotoFile = FFileUtil.newFileUnderDir(takePhotoDir, ".jpg");
            getPhotoFromCamera(takePhotoFile);
        }
    }

    public void getPhotoFromCamera(File saveFile)
    {
        takePhotoFile = saveFile;
        Intent intent = com.fanwe.library.utils.SDIntentUtil.getIntentTakePhoto(saveFile);
        startActivityForResult(intent, REQUEST_CODE_GET_PHOTO_FROM_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_GET_PHOTO_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK)
                {
                    if (listener != null)
                    {
                        if (takePhotoFile != null)
                        {
                            scanFile(mActivity, takePhotoFile);
                            listener.onResultFromCamera(takePhotoFile);
                        } else
                        {

                        }
                    }
                }
                break;
            case REQUEST_CODE_GET_PHOTO_FROM_ALBUM:
                if (resultCode == Activity.RESULT_OK)
                {
                    //String  path = SDImageUtil.getImageFilePathFromIntent(data, mActivity);
                    String path = UriFileUtils.getPath(mActivity, data.getData());

                    if (listener != null)
                    {
                        if (TextUtils.isEmpty(path))
                        {
                            listener.onFailure("从相册获取图片失败");
                        } else
                        {
                            listener.onResultFromAlbum(new File(path));
                        }
                    }
                }
                break;

            default:
                break;
        }
    }
    public  void scanFile(Context context, File file)
    {
        if (context != null && file != null && file.exists())
        {
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
    }
    public interface PhotoHandlerListener
    {
        void onResultFromAlbum(File file);

        void onResultFromCamera(File file);

        void onFailure(String msg);
    }

}

