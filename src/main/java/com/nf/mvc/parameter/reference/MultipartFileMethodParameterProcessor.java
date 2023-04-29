package com.nf.mvc.parameter.reference;

import com.nf.mvc.Handler;
import com.nf.mvc.ParameterProcessor;
import com.nf.mvc.file.MultipartFile;
import com.nf.mvc.file.StandardMultipartFile;
import com.nf.mvc.support.Order;
import com.nf.mvc.util.FileCopyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Order(4)
public class MultipartFileMethodParameterProcessor implements ParameterProcessor {

    protected Parameter parameter;

    protected Class<?> parameterType;

    /**
     * @param parameter
     * @return
     */
    @Override
    public boolean supports(Parameter parameter) {
        this.parameter = parameter;
        this.parameterType = parameter.getType();
        return isFileType(this.parameterType)
                || (this.parameterType.isArray() && isFileType(this.parameterType.getComponentType()));
//                || ReflectionUtils.isListOrSet(paramType) && isFileType();
    }

    private boolean isFileType(Class<?> fileType) {
        return Part.class == fileType ||
                MultipartFile.class == fileType;
    }

    @Override
    public  Object processor(Handler handler, HttpServletRequest request) throws IOException, ServletException {
        Class<?> paramType = this.parameterType;
        String paramName = handler.getParamName(this.parameter);
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
     * @param disposition
     * @return
     */
    private String getFileName(String disposition) {
        String fileName = disposition.substring(disposition.indexOf("filename=\"") + 10, disposition.lastIndexOf("\""));
        System.out.println(fileName);
        return fileName;
    }
}
