package com.koedia.activty.activityManager.controller.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public class GlobalExceptionHandler {

	@ExceptionHandler(MultipartException.class)
	public String handlerErrorMaxSizeFileToUpload(MultipartException e, RedirectAttributes redirectAttribute) {
		
		redirectAttribute.addFlashAttribute("message", e.getCause().getMessage());
		
		return "redirect:upload-status";
	}
}
