package com.nf.mvc.parameter.reference;

import com.nf.mvc.MethodParameter;
import com.nf.mvc.file.MultipartFile;
import com.nf.mvc.file.StandardMultipartFile;
import com.nf.mvc.parameter.AbstractParameterProcessor;
import com.nf.mvc.support.Order;
import com.nf.mvc.util.FileCopyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Order(4)
public class MultipartFileMethodParameterProcessor extends AbstractParameterProcessor {
    @Override
    public boolean supports(MethodParameter methodParameter) {
        Class<?> parameterType = methodParameter.getParamType();
        return isFileType(parameterType)
                || (parameterType.isArray() && isFileType(parameterType.getComponentType()));
//                || ReflectionUtils.isListOrSet(paramType) && isFileType();
    }

    private boolean isFileType(Class<?> fileType) {
        return Part.class == fileType ||
                MultipartFile.class == fileType;
    }

    @Override
    public Object processor(MethodParameter methodParameter, HttpServletRequest request) throws IOException, ServletException {
        Class<?> paramType = methodParameter.getParamType();
        String paramName = this.getKey(methodParameter);
        if (paramType.isArray()) {
            return handleMultiFile(request.getParts(), paramType.getComponentType());
        } else {
            return handleSingleFile(request.getPart(paramName), paramType);
        }

    }

    private List<?> handleMultiFile(Collection<Part> parts, Class<?> fileType) {
        List<?> files = new ArrayList<>();
        for (Part part : parts) {
            Object uploaded = handleSingleFile(part, fileType);
            // files.add(uploaded);
        }
        return files;
    }

    private Object handleSingleFile(Part part, Class<?> paramType) {
        if (Part.class == paramType) {
            return part;
        } else {
            String disposition = part.getHeader(FileCopyUtils.CONTENT_DISPOSITION);
            String filename = getFileName(disposition);
            return new StandardMultipartFile(part, filename);
        }
    }

    /**
     * TODO:获取文件名的代码可以再改进一下
     *
     * @param disposition
     * @return
     */
    private String getFileName(String disposition) {
        String fileName = disposition.substring(disposition.indexOf("filename=\"") + 10, disposition.lastIndexOf("\""));
        System.out.println(fileName);
        return fileName;
    }
}
