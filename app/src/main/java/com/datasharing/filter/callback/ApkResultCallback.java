package com.datasharing.filter.callback;

import com.datasharing.filter.entity.BaseFile;
import com.datasharing.filter.entity.Directory;

import java.util.List;

public interface ApkResultCallback<T extends BaseFile> {
    void onResult(List<Directory<T>> directories);
}