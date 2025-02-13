package com.wave.matrix.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(MultipartFile file);

    void store(MultipartFile file, String updatedName);

    void storeBaseImage(String baseImage, String updatedName, String fileType) throws IOException;

    Stream<Path> loadAll();

    Path load(String filename);

    Path load(String filename, String type);

    Resource loadAsResource(String filename);

    void deleteAll();

    boolean delete(String filename);

    Resource loadAsResource(String filename, String type);
}
