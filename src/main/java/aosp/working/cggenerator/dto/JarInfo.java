package aosp.working.cggenerator.dto;

import aosp.working.cggenerator.Global;
import aosp.working.cggenerator.util.FileUtil;
import aosp.working.cggenerator.util.JSONFormatUtils;
import com.google.gson.Gson;

import java.util.*;

public class JarInfo implements JSONWriter {
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

    /**
     * 通过 “非全限定名” 检查，得到 Jar 中所有调用过 “传入 Set” 中任意一个方法的那些方法。
     * 因为没有编译成 class，仅仅扫描源码无法获得 “全限定名”。
     *
     * 因为是通过短名的，所以在重载方面非常不准确，非常的 "solid"。
     * @param methodsName 传入的 Set。里面都是 “非全限定类+方法名”
     * @return 所有调用过 Set 中任意一个方法的方法集合
     */
    public List<CenterGraph.CallRelationship> getAllMethodWhoCalling(Set<String> methodsName) {
        Map<MethodMetadata, List<MethodMetadata>> result = new HashMap<>();
        // each class in jar
        for (ClassInfo classInfo : this.classesInfo) {
            // each method in class
            for (MethodInfo methodInfo : classInfo.getMethodsInfo()) {
                // methodInfo is of who-calling-method (val);
                // methodMetadata is of calling-method (key).
                for (MethodMetadata methodMetadata : methodInfo.getCallingMethods()) {
                    String nonFullyQualifiedMethodName = methodMetadata.getNonFullyQualifiedName();
                    if (methodsName.contains(nonFullyQualifiedMethodName)) {
                        if (!result.containsKey(nonFullyQualifiedMethodName)) {
                            List<MethodMetadata> whoCallingMethods = new ArrayList<>();
                            whoCallingMethods.add(methodInfo.getMethodMetadata());
                            result.put(methodMetadata, whoCallingMethods);
                        }
                    }
                }
            }
        }
        List<CenterGraph.CallRelationship> callRelationships = new ArrayList<>();
        for (Map.Entry<MethodMetadata, List<MethodMetadata>> entry : result.entrySet()) {
            CenterGraph.CallRelationship relationship
                    = new CenterGraph.CallRelationship(entry.getKey(), entry.getValue());
            callRelationships.add(relationship);
        }
        return callRelationships;
    }

    public void writeJSON() {
        Gson gson = new Gson();
        String result = gson.toJson(this);
        String filePath =
                Global.props.getProperty("call.graph.output.file.path") + FileUtil.getJarName(this.jarPath) + ".json";
        FileUtil.writeFile(filePath, JSONFormatUtils.formatJson(result));
    }

}
