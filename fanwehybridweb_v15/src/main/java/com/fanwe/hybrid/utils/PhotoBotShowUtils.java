package com.fanwe.hybrid.utils;

import android.app.Activity;
import android.view.View;

import com.fanwe.lib.dialog.impl.FDialog;
import com.fanwe.lib.dialog.impl.FDialogMenu;
import com.fanwe.library.adapter.iml.SDSimpleTextAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class PhotoBotShowUtils
{
    public final static int DIALOG_CAMERA = 0;
    public final static int DIALOG_ALBUM = 1;
    public final static int DIALOG_BOTH = 2;

    public static void openBotPhotoView(Activity activity, final PhotoHandler photoHandler, final int type)
    {
        if (photoHandler == null)
        {
            return;
        }
        String[] arrOption = null;
        if (type == DIALOG_CAMERA)
        {
            arrOption = new String[]{"拍照"};
        } else if (type == DIALOG_ALBUM)
        {
            arrOption = new String[]{"相册"};
        } else
        {
            arrOption = new String[]{"拍照", "相册"};
        }

        List<String> listOptions = Arrays.asList(arrOption);

        SDSimpleTextAdapter<String> adapter = new SDSimpleTextAdapter<String>(listOptions, activity);

         FDialogMenu dialog = new FDialogMenu(activity);

        dialog.setAdapter(adapter);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCallback(new FDialogMenu.Callback()
        {
            @Override
            public void onClickItem(View view, int i, FDialog sdDialogBase)
            {
                if (type == DIALOG_CAMERA)
                {
                    photoHandler.getPhotoFromCamera();
                } else if (type == DIALOG_ALBUM)
                {
                    photoHandler.getPhotoFromAlbum();
                } else
                {
                    switch (i)
                    {
                        case 0:
                            photoHandler.getPhotoFromCamera();
                            break;
                        case 1:
                            photoHandler.getPhotoFromAlbum();
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onClickCancel(View view, FDialog sdDialogBase)
            {

            }
        });
        dialog.showBottom();
    }
}
