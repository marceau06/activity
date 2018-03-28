package com.koedia.activity.activityManager.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {

	private static String UPLOADED_FOLDER_DEFAULT= "../img/stored/";
	
    @PostMapping("/upload")
    public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

    	ModelAndView mav = new ModelAndView("upload-status");
    	
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return mav;
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER_DEFAULT + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return mav;
    }

    @GetMapping("/upload-status")
    public ModelAndView uploadStatus() {
        return new ModelAndView("upload-status");
    }
}
