package com.datasharing.filter;

import androidx.fragment.app.FragmentActivity;

import com.datasharing.filter.callback.ApkResultCallback;
import com.datasharing.filter.callback.ApkfilecallBack;
import com.datasharing.filter.callback.FileLoaderCallbacks;
import com.datasharing.filter.callback.FilterResultCallback;
import com.datasharing.filter.entity.AudioFile;
import com.datasharing.filter.entity.ImageFile;
import com.datasharing.filter.entity.NormalFile;
import com.datasharing.filter.entity.VideoFile;

import static com.datasharing.filter.callback.FileLoaderCallbacks.TYPE_AUDIO;
import static com.datasharing.filter.callback.FileLoaderCallbacks.TYPE_FILE;
import static com.datasharing.filter.callback.FileLoaderCallbacks.TYPE_IMAGE;
import static com.datasharing.filter.callback.FileLoaderCallbacks.TYPE_VIDEO;

/**
 * Created by Vincent Woo
 * Date: 2016/10/11
 * Time: 10:19
 */

public class FileFilter {
    public static void getImages(FragmentActivity activity, FilterResultCallback<ImageFile> callback) {
        activity.getSupportLoaderManager().initLoader(0, null,
                new FileLoaderCallbacks(activity, callback, TYPE_IMAGE));
    }

    public static void getVideos(FragmentActivity activity, FilterResultCallback<VideoFile> callback) {
        activity.getSupportLoaderManager().initLoader(1, null,
                new FileLoaderCallbacks(activity, callback, TYPE_VIDEO));
    }

    public static void getAudios(FragmentActivity activity, FilterResultCallback<AudioFile> callback) {
        activity.getSupportLoaderManager().initLoader(2, null,
                new FileLoaderCallbacks(activity, callback, TYPE_AUDIO));
    }

    public static void getFiles(FragmentActivity activity,
                                FilterResultCallback<NormalFile> callback, String[] suffix) {
        activity.getSupportLoaderManager().initLoader(3, null,
                new FileLoaderCallbacks(activity, callback, TYPE_FILE, suffix));
    }

    public static void getApkFiles(FragmentActivity activity,
                                   ApkResultCallback<NormalFile> callback, String[] suffix) {
        activity.getSupportLoaderManager().initLoader(3, null,
                new ApkfilecallBack(activity, callback, TYPE_FILE, suffix));
    }
}
