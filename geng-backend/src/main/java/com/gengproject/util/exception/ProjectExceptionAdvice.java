package com.gengproject.util.exception;

import com.gengproject.util.model.constant.HttpCode;
import com.gengproject.util.model.constant.HttpMessage;
import com.gengproject.util.model.Result;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//@RestControllerAdvice用于标识当前类为REST风格对应的异常处理器
@RestControllerAdvice
public class ProjectExceptionAdvice {
    //@ExceptionHandler用于设置当前处理器类对应的异常类型
    @ExceptionHandler(SystemException.class)
    public Result doSystemException(SystemException ex){
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        System.out.println(ex);
        return new Result(ex.getCode(),null,ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result doBusinessException(BusinessException ex){
        System.out.println(ex);
        return new Result(ex.getCode(),null,ex.getMessage());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result doSystemException(HttpMessageNotReadableException ex){
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        System.out.println(ex);
        return new Result(HttpCode.ERROR,null, HttpMessage.PARAMA_INVALID);
    }

    //除了自定义的异常处理器，保留对Exception类型的异常处理，用于处理非预期的异常
    @ExceptionHandler(java.lang.Exception.class)
    public Result doOtherException(java.lang.Exception ex){
        //记录日志
        //发送消息给运维
        //发送邮件给开发人员,ex对象发送给开发人员
        System.out.println(ex);
        return new Result(HttpCode.SYSTEM_UNKNOW_ERR,null,"系统繁xxz忙，请稍后再试！");
    }
}
