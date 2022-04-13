package aosp.working.cggenerator.dto;

import java.util.List;

public class MethodInfo {
    private MethodMetadata methodMetadata;
    private List<MethodMetadata> callingMethods;

    public MethodMetadata getMethodMetadata() {
        return this.methodMetadata;
    }

    public List<MethodMetadata> getCallingMethods() {
        return this.callingMethods;
    }

    public void setMethodMetadata(MethodMetadata methodMetadata) {
        this.methodMetadata = methodMetadata;
    }

    public void setCallingMethods(List<MethodMetadata> callingMethods) {
        this.callingMethods = callingMethods;
    }
}
