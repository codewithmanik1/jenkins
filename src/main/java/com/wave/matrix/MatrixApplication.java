package com.wave.matrix;

import com.wave.matrix.storage.StoragePropertiesMaster;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StoragePropertiesMaster.class)  // Ensure this is added
public class MatrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatrixApplication.class, args);
	}

}
