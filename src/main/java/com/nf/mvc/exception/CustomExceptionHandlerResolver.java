package com.nf.mvc.exception;

import com.nf.mvc.ViewResult;
import com.nf.mvc.exception.exceptions.*;
import com.nf.mvc.exception.vo.ResponseVO;

import static com.nf.mvc.handler.HandlerHelper.json;

public class CustomExceptionHandlerResolver {
    @ExceptionHandler(RepeatException.class)
    public ViewResult exception(RepeatException e){
        return json(new ResponseVO(500,"映射重复的异常",e.getMessage()));
    }

    @ExceptionHandler(UnableToExecuteException.class)
    public ViewResult exception(UnableToExecuteException e){
        return json(new ResponseVO(500,"无法调用异常程序",e.getMessage()));
    }

    @ExceptionHandler(UnableToProcessTypeException.class)
    public ViewResult exception(UnableToProcessTypeException e){
        return json(new ResponseVO(500,"无法转化类型异常",e.getMessage()));
    }

    @ExceptionHandler(NoInstantiateException.class)
    public ViewResult exception(NoInstantiateException e){
        return json(new ResponseVO(500,"无法获取到数据",e.getMessage()));
    }

    @ExceptionHandler(NoAssignmentToPrimitiveIsNullException.class)
    public ViewResult exception(NoAssignmentToPrimitiveIsNullException e){
        return json(new ResponseVO(500,"无法获取到数据",e.getMessage()));
    }
}
