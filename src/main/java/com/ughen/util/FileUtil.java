package com.ughen.util;

import com.ughen.config.FileUploadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    private static FileUploadConfig fileUploadConfig;

    @Autowired
    public FileUtil(FileUploadConfig fileUploadConfig) {
        FileUtil.fileUploadConfig = fileUploadConfig;
    }


    public static String uploadFile(byte[] file, String fileName) throws IOException {
        String currentTimeStr = TimeUtils.getCurrentTimeStr();
        String folderPath = String.format("%s/%s", fileUploadConfig.getUploadPath(), currentTimeStr);
        fileName = FileUtil.renameToUUID(fileName);
        File targetFile = new File(folderPath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = null;
        try {
            String filePath = String.format("%s/%s", folderPath, fileName);
            out = new FileOutputStream(filePath);
            out.write(file);
            out.flush();
            String path = String.format("%s/%s/%s", fileUploadConfig.getDomain(), currentTimeStr, fileName);
            return path;
        } catch (IOException e) {
            logger.debug("上传失败", e);
            throw new IOException(e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String renameToUUID(String fileName) {
        return UUID.randomUUID().toString().replace("-","") + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
