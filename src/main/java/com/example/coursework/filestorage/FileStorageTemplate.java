package com.example.coursework.filestorage;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface FileStorageTemplate<I, T> {

    void saveInHashMap();

    void updateFromFile(I id, T entity, String dirname);

    void deleteFromFile(I id, String dirname);

    void writeToFile(T entity, String filepath);

    List<T> fileReader(File file);

    List<T> readAllFiles(String dirname);

    Integer incrementIdCounter();

    Map<I, T> getHashMap();

    List<T> getList();
}
