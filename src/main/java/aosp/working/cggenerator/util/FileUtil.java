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

    /**
     * 根据文件路径读取文件到 String
     * @param path
     * @return
     */
    public static String readFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        try {
            // Global.logger.info("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));

            String tempString = "";
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                result.append(tempString).append("\r\n");
                line++;
            }
            result = new StringBuilder(result.substring(0, result.length() - 2));
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    return result.toString();
                } catch (IOException e1) {
                }
            }
        }

        return null;
    }
}