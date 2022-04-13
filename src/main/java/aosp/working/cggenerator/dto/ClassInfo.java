package aosp.working.cggenerator.dto;

import java.util.List;

public class ClassInfo {
    private String fullyQualifiedName;
    private List<MethodInfo> methodsInfo;

    public String getFullyQualifiedName() {
        return this.fullyQualifiedName;
    }

    public List<MethodInfo> getMethodsInfo() {
        return this.methodsInfo;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public void setMethodsInfo(List<MethodInfo> methodsInfo) {
        this.methodsInfo = methodsInfo;
    }
}
