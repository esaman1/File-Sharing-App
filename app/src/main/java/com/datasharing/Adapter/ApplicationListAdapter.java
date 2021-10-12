package com.datasharing.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.datasharing.Const.Constant;
import com.datasharing.Model.ApplicationModel;
import com.datasharing.R;
import com.datasharing.TransferFragment;

import java.util.ArrayList;

public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.MyClassView> implements Filterable {

    public ArrayList<ApplicationModel> contactListFiltered = new ArrayList<>();
    ArrayList<ApplicationModel> applicationModels;
    Activity activity;

    public ApplicationListAdapter(ArrayList<ApplicationModel> applicationModels, Activity activity) {
        this.applicationModels = applicationModels;
        this.activity = activity;
        this.contactListFiltered = applicationModels;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_of_apps, null, false);
        return new MyClassView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        ApplicationModel applicationModel = contactListFiltered.get(position);

        Glide.with(activity)
                .load(applicationModel.getAppIcon())
                .into(holder.img_app_icon);

        holder.tv_app_name.setText(applicationModel.getAppName());

        holder.itemView.setOnClickListener(v -> {
            if (holder.img_select.getVisibility() == View.VISIBLE) {
                Constant.filePaths.remove(applicationModel.getAppPath());
                Constant.FileName.remove(applicationModel.getAppName());
                holder.img_select.setVisibility(View.GONE);
                holder.img_unselect.setVisibility(View.VISIBLE);
            } else {
                Constant.filePaths.add(applicationModel.getAppPath());
                Constant.FileName.add(applicationModel.getAppName());
                holder.img_select.setVisibility(View.VISIBLE);
                holder.img_unselect.setVisibility(View.GONE);
            }

            TransferFragment.fragmentPlayListBinding.tvSendFile.setText("SEND ( " + Constant.filePaths.size() + " )");
        });
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = applicationModels;
                } else {
                    ArrayList<ApplicationModel> filteredList = new ArrayList<>();
                    for (ApplicationModel row : applicationModels) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAppName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<ApplicationModel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        ImageView img_app_icon, img_unselect, img_select;
        TextView tv_app_name;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            img_app_icon = itemView.findViewById(R.id.img_app_icon);
            tv_app_name = itemView.findViewById(R.id.tv_app_name);
            img_unselect = itemView.findViewById(R.id.img_unselect);
            img_select = itemView.findViewById(R.id.img_select);
        }
    }
}
