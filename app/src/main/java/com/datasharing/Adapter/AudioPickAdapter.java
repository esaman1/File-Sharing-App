package com.datasharing.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.datasharing.R;
import com.datasharing.ToastUtil;
import com.datasharing.Util;
import com.datasharing.filter.entity.AudioFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Vincent Woo
 * Date: 2016/10/25
 * Time: 10:57
 */

public class AudioPickAdapter extends BaseAdapter<AudioFile, AudioPickAdapter.AudioPickViewHolder> implements Filterable {
    private final int mMaxNumber;
    public ArrayList<AudioFile> filteredList = new ArrayList<>();
    private int mCurrentNumber = 0;

    public AudioPickAdapter(Context ctx, int max) {
        this(ctx, new ArrayList<AudioFile>(), max);
    }

    public AudioPickAdapter(Context ctx, ArrayList<AudioFile> list, int max) {
        super(ctx, list);
        mMaxNumber = max;
        filteredList = list;
    }

    @Override
    public AudioPickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.rv_layout_item_audio, parent, false);
        return new AudioPickViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AudioPickViewHolder holder, final int position) {
        final AudioFile file = filteredList.get(position);

        holder.mCbx.setSelected(false);
        mCurrentNumber = 0;
        filteredList.get(position).setSelected(holder.mCbx.isSelected());

        holder.mTvTitle.setText(file.getName());
        holder.mTvTitle.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        if (holder.mTvTitle.getMeasuredWidth() >
                Util.getScreenWidth(mContext) - Util.dip2px(mContext, 10 + 32 + 10 + 48 + 10 * 2)) {
            holder.mTvTitle.setLines(2);
        } else {
            holder.mTvTitle.setLines(1);
        }

        holder.mTvDuration.setText(Util.getDurationString(file.getDuration()));
        holder.mCbx.setSelected(file.isSelected());

        holder.mCbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()) {
                    holder.mCbx.setSelected(false);
                    mCurrentNumber--;
                } else {
                    holder.mCbx.setSelected(true);
                    mCurrentNumber++;
                }

                filteredList.get(holder.getAdapterPosition()).setSelected(holder.mCbx.isSelected());

                if (mListener != null) {
                    mListener.OnSelectStateChanged(holder.mCbx.isSelected(), filteredList.get(holder.getAdapterPosition()));
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    File f = new File(file.getPath());
                    uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", f);
                } else {
                    uri = Uri.parse("file://" + file.getPath());
                }
                intent.setDataAndType(uri, "audio/mp3");
                if (Util.detectIntent(mContext, intent)) {
                    mContext.startActivity(intent);
                } else {
                    ToastUtil.getInstance(mContext).showToast(mContext.getString(R.string.vw_no_audio_play_app));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public boolean isUpToMax() {
        return mCurrentNumber >= mMaxNumber;
    }

    public void setCurrentNumber(int number) {
        mCurrentNumber = number;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = mList;
                } else {
                    ArrayList<AudioFile> filteredList1 = new ArrayList<>();
                    for (AudioFile row : mList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList1.add(row);
                        }
                    }

                    filteredList = filteredList1;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<AudioFile>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    class AudioPickViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvTitle;
        private final TextView mTvDuration;
        private final ImageView mCbx;

        public AudioPickViewHolder(View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_audio_title);
            mTvDuration = itemView.findViewById(R.id.tv_duration);
            mCbx = itemView.findViewById(R.id.cbx);
        }
    }
}
