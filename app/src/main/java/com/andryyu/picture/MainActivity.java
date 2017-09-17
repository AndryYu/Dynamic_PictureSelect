package com.andryyu.picture;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.bean.MediaBean;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RelativeLayout rlAddPic;
    private List<ImageModel> modelList;
    private AddImageAdapter adapter;
    private RxGalleryManager galleryManager;
    private int imageSize;


    private LoadPictureListener mListener = new LoadPictureListener() {
        @Override
        public void onSuccess(List<MediaBean> list) {
            int size = list.size();
            if(size>0) {
                rlAddPic.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            for(int i = 0; i< size ;i++){
               ImageModel model = new ImageModel();
                model.setSequenceNo((imageSize + i)%3 + 1);
                String[] types = list.get(i).getMimeType().split("/");
                model.setFileExtension(types[1]);
                model.setImageSrc(DataUtil.getImageBase64(list.get(i).getOriginalPath()));
                modelList.add(model);
            }
            imageSize +=size;
            adapter.setRefresh();
        }

        @Override
        public void onError() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initData();
    }

    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.rv_modify_pic);
        rlAddPic = (RelativeLayout) findViewById(R.id.layout_add_pic_empty);
        modelList = new ArrayList<>();
        if(modelList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            rlAddPic.setVisibility(View.VISIBLE);
        }

        imageSize = modelList.size();
        adapter = new AddImageAdapter(this, modelList);
        galleryManager = RxGalleryManager.getInstance(this);
    }

    private void initData(){
        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
        //設置圖片間隔距離
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        rlAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               galleryManager.addPhto(3, mListener);
            }
        });

        adapter.setOnItemClickListener(new AddImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()){
                    case R.id.iv_add_pic:
                        //图片选择数量
                        int addSize = 6 - imageSize;
                        int reSize = addSize%3;
                        if(reSize==0){
                            reSize=3;
                        }
                        galleryManager.addPhto(reSize, mListener);
                        break;
                    case R.id.iv_add_delete:
                        modelList.remove(position);
                        imageSize = modelList.size();
                        adapter.setRefresh();
                        break;
                }
            }
        });
    }
}
