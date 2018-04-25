package com.pzp.manage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.exception</p>
 * <p>Title: 使用 @ControllerAdvice，不用任何的配置，
 * 只要把这个类放在项目中，Spring能扫描到的地方。就可以实现全局异常的回调。</p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/13 17:38 星期五
 */

//@ControllerAdvice
public class GlobalExceptionHandler{

    /**
     * 全局处理Exception
     * 错误的情况下返回500
     * @param ex
     * @param req
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(final Exception ex, final WebRequest req) {
        ExceptionMessage message = new ExceptionMessage();
        message.setCode("500");
        message.setMessage(ex.getMessage());
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}