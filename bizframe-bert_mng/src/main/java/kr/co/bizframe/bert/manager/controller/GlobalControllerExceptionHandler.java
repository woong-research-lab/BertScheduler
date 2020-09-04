package kr.co.bizframe.bert.manager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	private static Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

	@ExceptionHandler(value = { Throwable.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected void handleConflict(Throwable ex) {
		logger.error("GlobalControllerExceptionHandler error " + ex.getMessage(), ex);
	}
}
