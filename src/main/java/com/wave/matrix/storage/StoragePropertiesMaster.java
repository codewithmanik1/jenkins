package com.wave.matrix.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("storage")
public class StoragePropertiesMaster {
    /**
     * Folder location for storing files
     */
    private String location = "/home/maanik/ccodes/wavematrix";
    private String productFurniture = "/home/maanik/ccodes/wavematrix/products";

}
