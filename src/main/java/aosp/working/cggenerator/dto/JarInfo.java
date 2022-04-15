package aosp.working.cggenerator.dto;

import aosp.working.cggenerator.util.FileUtil;
import aosp.working.cggenerator.util.JSONFormatUtils;
import com.google.gson.Gson;

import java.util.List;

public class JarInfo {
    private String jarPath;
    private List<ClassInfo> classesInfo;

    public String getJarPath() {
        return this.jarPath;
    }

    public List<ClassInfo> getClassesInfo() {
        return this.classesInfo;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath;
    }

    public void setClassesInfo(List<ClassInfo> classesInfo) {
        this.classesInfo = classesInfo;
    }

    public void writeJSON(String filePath) {
        Gson gson = new Gson();
        String result = gson.toJson(this);
        FileUtil.writeFile(filePath, JSONFormatUtils.formatJson(result));
    }
}
