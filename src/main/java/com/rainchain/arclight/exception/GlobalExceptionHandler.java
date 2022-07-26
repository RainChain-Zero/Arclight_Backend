package com.rainchain.arclight.exception;


import com.rainchain.arclight.utils.ResultData;
import io.lettuce.core.RedisCommandExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    //处理参数校验异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultData<String> validateException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<String> messages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());
        String message = StringUtils.join(messages, ';');
        return ResultData.fail(message);
    }

    @ExceptionHandler(value = BindException.class)
    public ResultData<String> bindException(BindException ex) {
        AtomicReference<String> message = new AtomicReference<>("");
        BindingResult bindingResult = ex.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        allErrors.forEach(e -> message.set(message + e.getDefaultMessage()));
        return ResultData.fail(message.toString());
    }


    //处理自定义异常
    @ExceptionHandler(value = OperationFailException.class)
    public ResultData<String> operationFailException(OperationFailException e) {
        return ResultData.fail(e.getErrMsg());
    }

    //http异常
    @ExceptionHandler(value = {HttpStatusCodeException.class, HttpClientErrorException.class, HttpServerErrorException.class})
    public ResultData<String> httpStatusCodeException(Exception e) {
        return ResultData.fail(e.getMessage());
    }

    //空指针异常
    @ExceptionHandler(value = NullPointerException.class)
    public ResultData<String> nullPointerException(NullPointerException e) {
        log.error("空指针异常:" + e.getMessage());
        return ResultData.fail("请检验参数是否正确，检查无误后仍然错误请联系我们");
    }

    //编码错误
    @ExceptionHandler(value = UnsupportedEncodingException.class)
    public ResultData<String> unsupportedEncodingException(UnsupportedEncodingException e) {
        log.error(e.getMessage());
        return ResultData.fail("不支持的编码格式！");
    }

    //SQL错误
    @ExceptionHandler(value = SQLException.class)
    public ResultData<String> sqlException(SQLException e) {
        e.printStackTrace();
        return ResultData.fail("服务器内部错误！");
    }

    //Redis错误
    @ExceptionHandler(value = RedisCommandExecutionException.class)
    public ResultData<String> redisCommandExecutionException(RedisCommandExecutionException e) {
        e.printStackTrace();
        return ResultData.fail("缓存读取失败，请重试一次");
    }

    @ExceptionHandler(value = IOException.class)
    public ResultData<String> iOException(IOException e) {
        e.printStackTrace();
        return ResultData.fail("服务器内部错误！");
    }

    @ExceptionHandler(value = Exception.class)
    public ResultData<String> innerErrorException(Exception e) {
        log.error(e.getMessage());
        return ResultData.fail(e.getMessage());
    }

}
