package com.szruito.goldfields.jshandler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.fanwe.library.utils.SDToast;

import org.xutils.common.Callback;
import org.xutils.common.util.FileUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * Created by yhz on 2017/4/26.
 */

public class RequestHandler
{
    private Activity activity;

    public RequestHandler(Activity activity)
    {
        this.activity = activity;
    }

    /**
     * 保存图片
     */
    public void savePicture(String url)
    {
        File dir = null;
        String dirName = x.app().getPackageName();
        if (FileUtil.existsSdcard())
        {
            dir = new File(Environment.getExternalStorageDirectory(), dirName);
        } else
        {
            dir = new File(Environment.getDataDirectory(), dirName);
        }
        final String dirPath = dir.getAbsolutePath();
        String path = dir.getAbsolutePath() + File.separator + url + ".jpg";

        RequestParams params = new RequestParams(url);
        params.setSaveFilePath(path);
        params.setAutoRename(false);
        params.setAutoResume(false);
        x.http().get(params, new Callback.ProgressCallback<File>()
        {

            @Override
            public void onSuccess(File result)
            {
                SDToast.showToast("保存成功");
                folderScan(dirPath);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback)
            {
                SDToast.showToast("保存失败");
            }

            @Override
            public void onCancelled(CancelledException cex)
            {

            }

            @Override
            public void onFinished()
            {

            }

            @Override
            public void onWaiting()
            {

            }

            @Override
            public void onStarted()
            {

            }

            @Override
            public void onLoading(long total, long current,
                    boolean isDownloading
            )
            {

            }

        });
    }

    /**
     * 扫描文件
     *
     * @param filePath
     */
    private void fileScan(String filePath)
    {
        Uri data = Uri.parse("file://" + filePath);
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, data));
    }

    /**
     * 遍历文件夹中的文件，挨个扫描
     *
     * @param dirPath 文件夹路径
     */
    private void folderScan(String dirPath)
    {
        File file = new File(dirPath);
        if (file.isDirectory())
        {
            File[] array = file.listFiles();
            Log.d("FILE", array.length + "个文件");
            for (int i = 0; i < array.length; i++)
            {
                File f = array[i];
                if (f.isFile())
                {
                    fileScan(f.getAbsolutePath());
                } else
                {
                    folderScan(f.getAbsolutePath());
                }
            }
        }
    }
}
