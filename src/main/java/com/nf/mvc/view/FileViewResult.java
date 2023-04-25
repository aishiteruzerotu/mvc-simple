package com.nf.mvc.view;

import com.nf.mvc.ViewResult;
import com.nf.mvc.util.FileCopyUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileViewResult extends ViewResult {
    public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";

    /**
     * realPath指的文件的物理路径,比如D:/downloads/1.jpg
     */
    private String realPath;

    public FileViewResult(String realPath) {
        this.realPath = realPath;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String filename = getFileName();
        //attachment表示以附件的形式下载，对文件名编码以防止中文文件名在保存对话框中是乱码的
        resp.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename,"UTF-8"));
        Path path = Paths.get(realPath);
        FileCopyUtils.copy(Files.newInputStream(path), resp.getOutputStream());
    }

    private String getFileName() {
        int lastSlash = realPath.lastIndexOf("/");
        return realPath.substring(lastSlash + 1);
    }

    private String getMediaType(String filename) {
        //guessContentTypeFromName是从文件名猜测其内容类型，如果为null就猜测失败
        String midiaType = URLConnection.guessContentTypeFromName(filename);
        if (midiaType == null) {
            midiaType = APPLICATION_OCTET_STREAM_VALUE;
        }
        return midiaType;
    }

}
