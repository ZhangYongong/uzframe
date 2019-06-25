package com.ughen.constants;

/**
 * Author:@Yonghong Zhang
 * Date: 14:16 2019/1/22
 */
public enum FileType {
    BMP(".bmp", "image/bmp"),
    GIF(".gif", "image/gif"),
    JPEG(".jpeg", "image/jpeg"),
    JPG(".jpg", "image/jpeg"),
    PNG(".png", "image/jpeg"),
    DCX(".dcx", "image/x-dcx"),
    TIFF(".tiff", "image/tiff"),
    VSD(".vsd", "application/vnd.visio"),
    PPTX(".pptx", "application/vnd.ms-powerpoint"),
    PPT(".ppt", "application/vnd.ms-powerpoint"),
    DOCX(".docx", "application/msword"),
    DOC(".doc", "application/msword"),
    HTML(".html", "text/html"),
    TXT(".txt", "text/plain"),
    AFP(".afp", "application/afp"),
    PDF(".pdf", "application/pdf");

    String suffix;
    String contentType;

    FileType(String suffix, String contentType) {
        this.suffix = suffix;
        this.contentType = contentType;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public String getContentType() {
        return contentType;
    }

}
