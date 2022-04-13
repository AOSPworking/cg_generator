package aosp.working.cggenerator.dto;

import java.util.List;

public class MethodMetadata {
    private String fullyQualifiedName;
    private String nonFullyQualifiedName;
    private String returnType;
    private List<String> paramsType;

    public String getFullyQualifiedName() {
        return this.fullyQualifiedName;
    }

    public String getNonFullyQualifiedName() {
        return this.nonFullyQualifiedName;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public List<String> getParamsType() {
        return this.paramsType;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public void setNonFullyQualifiedName(String nonFullyQualifiedName) {
        this.nonFullyQualifiedName = nonFullyQualifiedName;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setParamsType(List<String> paramsType) {
        this.paramsType = paramsType;
    }
}
