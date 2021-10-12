package com.datasharing.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.datasharing.R;
import com.datasharing.filter.entity.Directory;

import java.util.ArrayList;

/**
 * Created by Vincent Woo
 * Date: 2018/2/27
 * Time: 10:25
 */

public class FolderListAdapter extends BaseAdapter<Directory, FolderListAdapter.FolderListViewHolder> {
    private FolderListListener mListener;

    public FolderListAdapter(Context ctx, ArrayList<Directory> list) {
        super(ctx, list);
    }

    @Override
    public FolderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.rv_layout_item_files_list,
                parent, false);
        return new FolderListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FolderListViewHolder holder, int position) {
        holder.mTvTitle.setText(mList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onFolderListClick(mList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setListener(FolderListListener listener) {
        this.mListener = listener;
    }

    public interface FolderListListener {
        void onFolderListClick(Directory directory);
    }

    class FolderListViewHolder extends RecyclerView.ViewHolder {
        private final TextView mTvTitle;

        public FolderListViewHolder(View itemView) {
            super(itemView);

            mTvTitle = itemView.findViewById(R.id.tv_folder_title);
        }
    }
}
