package aosp.working.cggenerator.dto;

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
}
