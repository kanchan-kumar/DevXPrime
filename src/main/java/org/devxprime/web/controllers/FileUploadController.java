package org.devxprime.web.controllers;

import java.io.IOException;
import java.util.stream.Collectors;

import org.devxprime.utils.DecompilerConst;
import org.devxprime.web.exception.StorageFileNotFoundException;
import org.devxprime.web.services.DecompilerService;
import org.devxprime.web.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Controller
public class FileUploadController {

    private final StorageService storageService;
    private final DecompilerService decompilerService;
    private static Logger logger = LogManager.getLogger(FileUploadController.class);


    @Autowired
    public FileUploadController(StorageService storageService, DecompilerService decompilerService) {
	this.storageService = storageService;
	this.decompilerService = decompilerService;
    }

    @GetMapping("/upload")
    public String listUploadedFiles(Model model) throws IOException {

	model.addAttribute("files",
		storageService.loadAll()
			.map(path -> MvcUriComponentsBuilder
				.fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
				.build().toString())
			.collect(Collectors.toList()));

	model.addAttribute("active_files", storageService.getTotalFileCount());

	return "uploadForm";
    }

    @GetMapping("/uploaded-files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

	Resource file = storageService.loadAsResource(filename);
	return ResponseEntity.ok()
		.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
		.body(file);
    }

    @PostMapping("/upload-java-files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

	String fileName = storageService.store(file);
	String data;
	try {
	    data = decompilerService.decompile(fileName, DecompilerConst.PROCYON_MODE);
	} catch (Exception e) {
	    return "ERROR: " + e.getMessage();
	}
	redirectAttributes.addFlashAttribute("message",
		"You successfully uploaded " + file.getOriginalFilename() + "! data = " + data);

	return "redirect:/upload";
    }

    @PostMapping("/uploaFile")
    public String uploadFiles(@RequestParam("file") MultipartFile file) {
	try {
	    
	    return storageService.store(file);
	} catch (Exception e) {
	    logger.error("Error while uploading file", e);
	    return "ERROR: " + e.getMessage();
	}
    }

    @GetMapping("/decompile")
    public String decompileJavaFiles(@RequestParam("fileName") String fileName, @RequestParam("mode") byte mode) {
	try {

	    return decompilerService.decompile(fileName, DecompilerConst.PROCYON_MODE);
	} catch (Exception e) {
	    logger.error("Error while decompiling file", e);
	    return "ERROR: " + e.getMessage();
	}
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
	return ResponseEntity.notFound().build();
    }

}
