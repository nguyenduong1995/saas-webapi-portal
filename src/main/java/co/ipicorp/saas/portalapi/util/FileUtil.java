package co.ipicorp.saas.portalapi.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * FileUtil.java
 * @author hienlt1
 *
 */
public class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class);

    public static void saveMultipartFile(MultipartFile mulFile, String path) {
        if(mulFile == null) return;
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(mulFile.getBytes());
                fos.close();
            } else {
                logger.info("File was exists : " + path);
            }
        } catch (IOException ex) {
            logger.info("Exception when save attachments : " + ex.getMessage());
        }
    }

    public static String createDirectory(String path) {
        File file = new File(path);
        if (file.mkdirs()) {
            logger.info("Directory created successfully with path : " + path);
        } else {
            logger.info("File was exists : " + path);
        }
        return path;
    }

    public static String renameFile(String fileName) {
        LocalDateTime time = LocalDateTime.now();
        String hour = String.valueOf(time.getHour());
        String minute = String.valueOf(time.getMinute());
        String second = String.valueOf(time.getSecond());
        //
        return fileName = hour + "_" + minute + "_" + second + "_" + fileName;
    }
}
