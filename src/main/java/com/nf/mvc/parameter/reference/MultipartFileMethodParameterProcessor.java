package com.nf.mvc.parameter.reference;

import com.nf.mvc.MethodParameter;
import com.nf.mvc.file.MultipartFile;
import com.nf.mvc.file.StandardMultipartFile;
import com.nf.mvc.parameter.AbstractMultipartFileMethodParameterProcessor;
import com.nf.mvc.support.Order;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Order(4)
public class MultipartFileMethodParameterProcessor extends AbstractMultipartFileMethodParameterProcessor {

    @Override
    protected boolean supportsInternal(Class<?> type) {
        return this.isFileType(type);
    }

    protected Object resolveArgumentInternal(Class<?> type, Object parameterValue, MethodParameter methodParameter) throws ServletException,IOException {
        //压根没有上传数据时，parameterValue就是null，这个时候直接返回null即可
        if (parameterValue == null) {
            return null;
        }

        return this.handleSingleFile((Part) parameterValue,type);
    }


    protected Object[] getSource(MethodParameter methodParameter, HttpServletRequest request) {
        Object[] source = null;
        try {
            List<Part> matchedParts = new ArrayList<>();
            Collection<Part> parts = request.getParts();
            for (Part part : parts) {
                if (part.getName().equals(methodParameter.getParamName())) {
                    matchedParts.add(part);
                }
            }
            source = matchedParts.toArray();
        } catch (IOException  | ServletException e) {
            //这里不抛出异常，什么也不干，相当于返回null，针对的一种场景是：比如修改商品记录不牵涉到图片的修改，
            //那么文件类型的参数就直接赋值为null即可，抛异常的话会中断控制器方法的执行
        }
        return source;
    }

    protected boolean isFileType(Class<?> fileType) {
        return Part.class == fileType ||
                MultipartFile.class == fileType;
    }

    protected  <T> T handleSingleFile(Part part, Class<T> paramType) {
        if (Part.class == paramType) {
            return (T) part;
        } else {
            return (T) new StandardMultipartFile(part, getFileName(part));
        }
    }

    protected String getFileName(Part part) {
        return part.getSubmittedFileName();
    }
}
