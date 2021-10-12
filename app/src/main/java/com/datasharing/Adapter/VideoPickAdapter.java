package com.datasharing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.datasharing.Const.Constant;
import com.datasharing.R;
import com.datasharing.Util;
import com.datasharing.filter.entity.VideoFile;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


/**
 * Created by Vincent Woo
 * Date: 2016/10/21
 * Time: 14:13
 */

public class VideoPickAdapter extends BaseAdapter<VideoFile, VideoPickAdapter.VideoPickViewHolder> {

    private final boolean isNeedCamera;
    private final int mMaxNumber;
    public String mVideoPath;
    private int mCurrentNumber = 0;
//    public ArrayList<VideoFile> filteredList=new ArrayList<>();

    public VideoPickAdapter(Context ctx, boolean needCamera, int max) {
        this(ctx, new ArrayList<VideoFile>(), needCamera, max);
    }

    public VideoPickAdapter(Context ctx, ArrayList<VideoFile> list, boolean needCamera, int max) {
        super(ctx, list);
        isNeedCamera = needCamera;
        mMaxNumber = max;
//        filteredList=list;
    }

    @Override
    public VideoPickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.rv_layout_item_videos, parent, false);
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (params != null) {
            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            params.height = width / Constant.COLUMN_NUMBER;
        }

        return new VideoPickViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoPickViewHolder holder, int position) {

        holder.mCbx.setSelected(false);
        mCurrentNumber = 0;
//        filteredList.get(position).setSelected(holder.mCbx.isSelected());
        mList.get(position).setSelected(holder.mCbx.isSelected());

        holder.mIvThumbnail.setVisibility(View.VISIBLE);
        holder.mCbx.setVisibility(View.VISIBLE);

        final VideoFile file;
        if (isNeedCamera) {
//            file = filteredList.get(position - 1);
            file = mList.get(position - 1);
        } else {
//            file = filteredList.get(position);
            file = mList.get(position);
        }

        RequestOptions options = new RequestOptions();
        Glide.with(mContext)
                .load(file.getPath())
                .apply(options.centerCrop())
                .transition(withCrossFade())
//                    .transition(new DrawableTransitionOptions().crossFade(500))
                .into(holder.mIvThumbnail);
        holder.mTitle.setText(file.getName());
        holder.mCbx.setSelected(file.isSelected());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()) {
                    holder.mCbx.setSelected(false);
                    mCurrentNumber--;
                } else {
                    holder.mCbx.setSelected(true);
                    mCurrentNumber++;
                }

                int index = isNeedCamera ? holder.getAdapterPosition() - 1 : holder.getAdapterPosition();
//                filteredList.get(index).setSelected(holder.mCbx.isSelected());
                mList.get(index).setSelected(holder.mCbx.isSelected());

                if (mListener != null) {
//                    mListener.OnSelectStateChanged(holder.mCbx.isSelected(), filteredList.get(index));
                    mListener.OnSelectStateChanged(holder.mCbx.isSelected(), mList.get(index));
                }
            }
        });

        holder.mDuration.setText(Util.getDurationString(file.getDuration()));

    }

    @Override
    public int getItemCount() {
        return isNeedCamera ? mList.size() + 1 : mList.size();
//        Log.e("video size :",filteredList.size() + "");
//        return isNeedCamera ? filteredList.size() + 1 : filteredList.size();
    }

    public boolean isUpToMax() {
        return mCurrentNumber >= mMaxNumber;
    }

    public void setCurrentNumber(int number) {
        mCurrentNumber = number;
    }

    class VideoPickViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mIvThumbnail;
        private final ImageView mCbx;
        private final TextView mDuration;
        private final TextView mTitle;

        public VideoPickViewHolder(View itemView) {
            super(itemView);
            mIvThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            mCbx = itemView.findViewById(R.id.cbx);
            mDuration = itemView.findViewById(R.id.txt_duration);
            mTitle = itemView.findViewById(R.id.tv_video_title);
        }
    }

}
