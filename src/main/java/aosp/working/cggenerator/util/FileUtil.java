package aosp.working.cggenerator.util;

import java.io.*;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 文件读写
 */
public class FileUtil {

    /**
     * 使用FileWriter类写文本文件
     */
    public static void writeFile(String fileName, String content) {
        writeFile(fileName, content, "UTF-8");
    }

    public static void writeFile(String fileName, String content, String encoding) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, false), encoding));
            //FileWriter writer = new FileWriter(fileName);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}