package com.nf.mvc.exception;

import com.nf.mvc.ViewResult;
import com.nf.mvc.exception.exceptions.NoInstantiateException;
import com.nf.mvc.exception.exceptions.RepeatException;
import com.nf.mvc.exception.exceptions.UnableToExecuteException;
import com.nf.mvc.exception.exceptions.UnableToProcessTypeException;
import com.nf.mvc.exception.vo.ResponseVO;

import static com.nf.mvc.handler.HandlerHelper.json;

public class CustomExceptionHandlerResolver {
    @ExceptionHandler(RepeatException.class)
    public ViewResult exception(RepeatException e){
        return json(new ResponseVO(500,"映射重复的异常",e));
    }

    @ExceptionHandler(UnableToExecuteException.class)
    public ViewResult exception(UnableToExecuteException e){
        return json(new ResponseVO(500,"无法调用异常程序",e));
    }

    @ExceptionHandler(UnableToProcessTypeException.class)
    public ViewResult exception(UnableToProcessTypeException e){
        return json(new ResponseVO(500,"无法转化类型异常",e));
    }

    @ExceptionHandler(NoInstantiateException.class)
    public ViewResult exception(NoInstantiateException e){
        return json(new ResponseVO(500,"无法转化类型异常",e));
    }
}
