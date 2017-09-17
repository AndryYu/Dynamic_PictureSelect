package com.andryyu.picture;

import android.content.Context;
import android.widget.Toast;

import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultDisposable;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.ui.RxGalleryListener;
import cn.finalteam.rxgalleryfinal.ui.base.IMultiImageCheckedListener;

/**
 * Created by yufei on 2017/9/17.
 */

public class RxGalleryManager {

    private  static Context mContext;
    private  LoadPictureListener mListener;
    private  static RxGalleryManager rxGalleryManager;

    public static RxGalleryManager getInstance(Context context){
        mContext = context.getApplicationContext();
        if(rxGalleryManager==null)
            rxGalleryManager= new RxGalleryManager();

        return rxGalleryManager;
    }

    public  void addPhto( int maxSize, LoadPictureListener loadPictureListener) {
        mListener = loadPictureListener;
        //开启照片选择器
        openMulti(maxSize);
        //设置选择监听
        setPhtoLisen();
    }

    private  void openMulti(int maxSize) {
        RxGalleryFinal rxGalleryFinal = RxGalleryFinal
                .with(mContext)
                .image()
                .multiple();

        rxGalleryFinal.maxSize(maxSize)
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultDisposable<ImageMultipleResultEvent>() {

                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                        List<MediaBean> list = imageMultipleResultEvent.getResult();
                        mListener.onSuccess(list);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        // Toast.makeText(getBaseContext(), "OVER", Toast.LENGTH_SHORT).show();
                    }
                })
                .openGallery();
    }

    private  void setPhtoLisen() {
        RxGalleryListener
                .getInstance()
                .setMultiImageCheckedListener(
                        new IMultiImageCheckedListener() {
                            @Override
                            public void selectedImg(Object t, boolean isChecked) {
                                // Toast.makeText(mContext, isChecked ? "选中" : "取消选中", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void selectedImgMax(Object t, boolean isChecked, int maxSize) {
                                Toast.makeText(mContext, "你最多只能選擇" + maxSize + "張圖片", Toast.LENGTH_SHORT).show();
                            }
                        });
    }
}
