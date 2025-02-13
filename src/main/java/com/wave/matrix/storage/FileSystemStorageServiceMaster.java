package com.wave.matrix.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;


@Slf4j
@Service
public class FileSystemStorageServiceMaster implements StorageService {

    private final Path rootLocation;
    private final Path productFurniture;
    public final static String PRODUCTS = "products";

    @Autowired
    public FileSystemStorageServiceMaster(StoragePropertiesMaster properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.productFurniture = Paths.get(properties.getProductFurniture());
    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }
            Path destinationFile = this.rootLocation.resolve(Paths.get(Objects.requireNonNull(file.getOriginalFilename()))).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public void store(MultipartFile file, String updatedName) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path destinationFile = this.rootLocation.resolve(Paths.get(Objects.requireNonNull(updatedName))).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                // This is a security check
                throw new StorageException("Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    @Override
    public void storeBaseImage(String baseImage, String updatedName, String fileType) throws IOException {
        byte[] byteArray = Base64.decodeBase64(baseImage);
        FileOutputStream fos = null;
        System.out.println("File type: " + fileType);

        try {
            // Ensure the directories exist before creating the file
            File furnitureDir = new File(productFurniture.toString());
            if (!furnitureDir.exists()) {
                if (furnitureDir.mkdirs()) {
                    System.out.println("Directory created: " + furnitureDir.getAbsolutePath());
                } else {
                    System.out.println("Failed to create directory: " + furnitureDir.getAbsolutePath());
                }
            } else {
                System.out.println("Directory already exists: " + furnitureDir.getAbsolutePath());
            }

            // Log the file path
            String filePath = productFurniture + "/" + updatedName;
            System.out.println("Attempting to write to path: " + filePath);

            // Switch case for different file types
            switch (fileType) {
                case PRODUCTS:
                    fos = new FileOutputStream(filePath);
                    System.out.println("In Furniture case");
                    break;
                default:
                    // Handle unknown file types or throw an exception
                    throw new IllegalArgumentException("Unsupported file type: " + fileType);
            }

            // Ensure fos is not null before writing
            if (fos != null) {
                fos.write(byteArray);
            } else {
                throw new IOException("File output stream was not initialized.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Failed to store the base image.", e);
        } finally {
            // Safely close the FileOutputStream if it's not null
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IOException("Failed to close FileOutputStream.", e);
                }
            }
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1).filter(path -> !path.equals(this.rootLocation)).map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Path load(String filename, String fileType) {
        System.out.println("filename " + filename);
        System.out.println("fileType " + fileType);
        switch (fileType) {
            case "p-furniture":
                return productFurniture.resolve(filename);
        }
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public Resource loadAsResource(String filename, String type) {
        System.out.println("FileName FileName" + filename);
        System.out.println("Type Type "+ type);
        try {
            Path file = load(filename, type);
            System.out.println("file.getFileSystem()"+file.getFileSystem());
            System.out.println("File load " + file);
            Resource resource = new UrlResource(file.toUri());
            System.out.println("resource.getFile().getAbsolutePath()"+resource.getFilename());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        FileSystemUtils.deleteRecursively(productFurniture.toFile());
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path file= new File(filename).toPath();
            System.out.println("file:"+file);
            System.out.println("file deletion started....");
            boolean check=Files.deleteIfExists(file);
            if(check)
            System.out.println("file deleted....");
            else
                System.out.println("file not deleted...");
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e);
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
            Files.createDirectories(productFurniture);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
