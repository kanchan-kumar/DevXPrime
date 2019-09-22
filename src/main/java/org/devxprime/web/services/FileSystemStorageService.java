package org.devxprime.web.services;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.devxprime.web.exception.StorageException;
import org.devxprime.web.exception.StorageFileNotFoundException;
import org.devxprime.web.props.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.PercentEscaper;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private static Logger logger = LogManager.getLogger(FileSystemStorageService.class);
    private StorageProperties props;


    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.props = properties;
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            
            logger.debug("Uploading file with fileName = " + filename);
            
            if (file.isEmpty()) {
                throw new StorageException("Failed to upload empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot upload file with relative path outside current directory "
                                + filename);
            }
            
            if (! filename.endsWith(".class")) {
            	 throw new StorageException(
                         "Only .class(java) files supported "
                                 + filename);
            }
            
            
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
            
            return filename;
        }
        catch (IOException e) {
            throw new StorageException("Failed to upload file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read uploaded files", e);
        }
    }
    
    @Override
    public long getTotalFileCount() {
        try {
            return Files.list(rootLocation).count();
        }
        catch (IOException e) {
            throw new StorageException("Failed to get uploaded files count.", e);
        }
    }
      
    @Override
    public void deleteOldFiles() {
        try {
        	
        	
        }
        catch (Exception e) {
            //throw new StorageException("Failed to delete old files.", e);
        }
    }
    

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public boolean cleanup() {
	
	try {
	    
	    Iterator<Path> iterator = Files.list(this.rootLocation).iterator();
	    
	    while (iterator.hasNext()) {
		File file = iterator.next().toFile();
		if ((System.currentTimeMillis() - file.lastModified()) > props.getMaxRetainTime()) {
		    file.deleteOnExit();
		}
	    }    
	} catch (Exception e) {
	    logger.error("Error in disk cleanup.", e);
	    return false;
	}
	return true;
    }
}
