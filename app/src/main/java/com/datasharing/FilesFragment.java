package com.datasharing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.datasharing.Adapter.FolderListAdapter;
import com.datasharing.Adapter.NormalFilePickAdapter;
import com.datasharing.Adapter.OnSelectStateListener;
import com.datasharing.Const.Constant;
import com.datasharing.filter.FileFilter;
import com.datasharing.filter.callback.FilterResultCallback;
import com.datasharing.filter.entity.Directory;
import com.datasharing.filter.entity.NormalFile;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FilesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilesFragment extends MainFragment {

    public static final int DEFAULT_MAX_NUMBER = 9;
    public static final String SUFFIX = "Suffix";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final ArrayList<NormalFile> mSelectedList = new ArrayList<>();
    List<NormalFile> imgDownloadList = new ArrayList<>();
    List<NormalFile> imgMainDownloadList = new ArrayList<>();
    List<NormalFile> imgMain1DownloadList = new ArrayList<>();
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private RecyclerView mRecyclerView;
    private NormalFilePickAdapter mAdapter;
    private List<Directory<NormalFile>> mAll;
    //    private ProgressBar mProgressBar;
    private RelativeLayout rl_progress;
    private ImageView img_no_data;
    private AVLoadingIndicatorView avi;
    private String[] mSuffix;
    private TextView tv_count;
    private TextView tv_folder;
    private LinearLayout ll_folder;
    private SearchView searchView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MyReceiver r;

    public FilesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilesFragment newInstance(String param1, String param2) {
        FilesFragment fragment = new FilesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    void permissionGranted() {
//        loadData();
    }

    void startAnim() {
        rl_progress.setVisibility(View.VISIBLE);
        avi.show();
    }

    void stopAnim() {
        rl_progress.setVisibility(View.GONE);
        avi.hide();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(r,
                new IntentFilter("TAG_REFRESH"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_files, container, false);
        mMaxNumber = getActivity().getIntent().getIntExtra(Constant.MAX_NUMBER, DEFAULT_MAX_NUMBER);
        mSuffix = new String[]{"xlsx", "xls", "doc", "docx", "ppt", "pptx", "pdf"};
        initView(view);

        return view;
    }


    private void initView(View view) {
        searchView = view.findViewById(R.id.file_search);
        rl_progress = view.findViewById(R.id.rl_progress);
        avi = view.findViewById(R.id.avi);
        mRecyclerView = view.findViewById(R.id.rv_file_pick);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new NormalFilePickAdapter(getContext(), mMaxNumber);
        mRecyclerView.setAdapter(mAdapter);

        img_no_data = view.findViewById(R.id.img_no_data);

        mAdapter.setOnSelectStateListener(new OnSelectStateListener<NormalFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, NormalFile file) {
                if (state) {
                    mSelectedList.add(file);
                    Constant.filePaths.add(file.getPath());
                    Constant.FileName.add(file.getName());
                    mCurrentNumber++;
                } else {
                    mSelectedList.remove(file);
                    Constant.filePaths.remove(file.getPath());
                    Constant.FileName.remove(file.getName());
                    mCurrentNumber--;
                }
                TransferFragment.fragmentPlayListBinding.tvSendFile.setText("SEND ( " + Constant.filePaths.size() + " )");
            }
        });

        ll_folder = view.findViewById(R.id.ll_folder);
        if (isNeedFolderList) {
            ll_folder.setVisibility(View.GONE);
            tv_folder = view.findViewById(R.id.tv_folder);

            mFolderHelper.setFolderListListener(new FolderListAdapter.FolderListListener() {
                @Override
                public void onFolderListClick(Directory directory) {
                    tv_folder.setText(directory.getName());

                    if (TextUtils.isEmpty(directory.getPath())) { //All
                        new LongOperation(mAll).execute();
                    } else {
                        for (Directory<NormalFile> dir : mAll) {
                            if (dir.getPath().equals(directory.getPath())) {
                                List<Directory<NormalFile>> list = new ArrayList<>();
                                list.add(dir);
                                new LongOperation(list).execute();
                                break;
                            }
                        }
                    }
                }
            });
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {                    // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
    }

    private void loadData() {
        FileFilter.getFiles(getActivity(), new FilterResultCallback<NormalFile>() {
            @Override
            public void onResult(List<Directory<NormalFile>> directories) {
                // Refresh folder list
                if (isNeedFolderList) {
                    ArrayList<Directory> list = new ArrayList<>();
                    Directory all = new Directory();
                    all.setName(getResources().getString(R.string.vw_all));
                    list.add(all);
                    list.addAll(directories);
                    mFolderHelper.fillData(list);
                }

                mAll = directories;
                new LongOperation(directories).execute();
            }
        }, mSuffix);
    }


    private void onScrolledToBottom() {

        if (imgDownloadList.size() == 0) {
            getActivity().runOnUiThread(() -> {
                mRecyclerView.setVisibility(View.GONE);
            });
        } else {
            if (imgMain1DownloadList.size() < imgDownloadList.size()) {
                int x, y;
                if ((imgDownloadList.size() - imgMain1DownloadList.size()) >= 60) {
                    x = imgMain1DownloadList.size();
                    y = x + 60;
                } else {
                    x = imgMain1DownloadList.size();
                    y = x + imgDownloadList.size() - imgMain1DownloadList.size();
                }
                imgMainDownloadList.clear();
                for (int i = x; i < y; i++) {
                    imgMainDownloadList.add(imgDownloadList.get(i));
                    imgMain1DownloadList.add(imgDownloadList.get(i));
                }

                getActivity().runOnUiThread(() -> {
                    mAdapter.refresh(imgMainDownloadList);
                });
            }
        }
    }

    public void notifyData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.refresh(new ArrayList<>());
                mSelectedList.clear();
                Constant.filePaths.clear();
                Constant.FileName.clear();
                loadData();
                Log.e("LLLL_VideoNotify: ", "Done");
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(r);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private final class LongOperation extends AsyncTask<Void, Void, List<NormalFile>> {

        List<Directory<NormalFile>> directories;


        public LongOperation(List<Directory<NormalFile>> directories) {
            this.directories = directories;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnim();
        }

        @Override
        protected List<NormalFile> doInBackground(Void... params) {
            List<NormalFile> list = new ArrayList<>();
            for (Directory<NormalFile> directory : directories) {
                list.addAll(directory.getFiles());
            }

            for (NormalFile file : mSelectedList) {
                int index = list.indexOf(file);
                if (index != -1) {
                    list.get(index).setSelected(true);
                }
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<NormalFile> result) {
            Log.e("LLLL_size_folder: ", String.valueOf(result.size()));
            if (result.size() == 0)
                img_no_data.setVisibility(View.VISIBLE);
            imgDownloadList.clear();
            imgDownloadList = result;
            onScrolledToBottom();
            stopAnim();
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            FilesFragment.this.notifyData();
        }
    }

}