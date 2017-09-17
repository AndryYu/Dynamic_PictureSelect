package com.andryyu.picture;

import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

/**
 * Created by yufei on 2017/9/17.
 */

public interface LoadPictureListener {

    void onSuccess(List<MediaBean> list);

    void onError();
}
