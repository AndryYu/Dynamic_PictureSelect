package com.andryyu.picture;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import java.util.List;

/**
 * Created by yufei on 2017/9/17.
 */
public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.DeviceHolder> {

    private List<ImageModel> imageModels;
    private Context context;
    private int dataSize = 0;
    private int lastSize = 0;
    private OnItemClickListener mOnItemClickListener = null;

    public AddImageAdapter(Context context, List<ImageModel> imageModels) {
        this.context = context;
        this.imageModels = imageModels;
        //处理没图片，但要显示添加图片
        for(int i = 0; i<3;i++){
            ImageModel imageModel = new ImageModel();
            this.imageModels.add(imageModel);
        }
        lastSize = imageModels.size();
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_image, parent, false);
        return new DeviceHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {

        ImageModel imageModel = imageModels.get(position);
        //判断有无ImageSrc
        if(TextUtils.isEmpty(imageModel.getImageSrc())){
            holder.ivDelete.setVisibility(View.GONE);
            Resources res = context.getResources();
            Bitmap bmp = BitmapFactory.decodeResource(res, R.mipmap.btn_addphoto);
            holder.ivPic.setImageBitmap(bmp);
            holder.ivPic.setEnabled(true);
        }else {
            holder.ivDelete.setVisibility(View.VISIBLE);
            holder.ivPic.setEnabled(false);
            holder.ivPic.setImageBitmap(DataUtil.base64ToBitmap(imageModel.getImageSrc()));
        }
        holder.ivPic.setTag(position);
        holder.ivDelete.setTag(position);
      }

    @Override
    public int getItemCount() {
        return imageModels.size();
    }


    public void setRefresh(){
        dataSize = this.imageModels.size();
        if(dataSize<3) {
            for (int i = 0; i < 3; i++) {
                ImageModel imageModel = new ImageModel();
                this.imageModels.add(imageModel);
            }
        }else if( dataSize>3 && dataSize<7){
            if(dataSize > lastSize){
                for (int i = 0; i < 3; i++) {
                    this.imageModels.remove(lastSize%3);
                }

                for (int i = 0; i < 3; i++) {
                    ImageModel imageModel = new ImageModel();
                    this.imageModels.add(imageModel);
                }
            }
        }
        lastSize = dataSize;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    class DeviceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivPic;
        public ImageView ivDelete;
        public RelativeLayout rlPic;

        public DeviceHolder(View itemView) {
            super(itemView);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_add_pic);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_add_delete);
            rlPic = (RelativeLayout) itemView.findViewById(R.id.ll_add_pic);

            ivPic.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取position
                mOnItemClickListener.onItemClick(v, (int) v.getTag());
            }
        }
    }


    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
