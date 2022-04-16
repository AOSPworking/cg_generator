package aosp.working.cggenerator.dto;

import aosp.working.cggenerator.Global;
import aosp.working.cggenerator.util.FileUtil;
import aosp.working.cggenerator.util.JSONFormatUtils;
import com.google.gson.Gson;

import java.util.List;

public class CenterGraph implements JSONWriter {
    public String jarPath;
    public List<CallRelationship> callRelationships;
    public CenterGraph(String jarPath, List<CallRelationship> callRelationships) {
        this.jarPath = jarPath;
        this.callRelationships = callRelationships;
    }

    public static class CallRelationship {
        public MethodMetadata center;
        public List<MethodMetadata> methodsCallingThis;
        public CallRelationship(MethodMetadata center, List<MethodMetadata> methodsCallingThis) {
            this.center = center;
            this.methodsCallingThis = methodsCallingThis;
        }
    }

    public void writeJSON() {
        Gson gson = new Gson();
        String result = gson.toJson(this);
        String filePath =
                Global.props.getProperty("center.graph.output.file.path") + FileUtil.getJarName(this.jarPath) + ".json";
        FileUtil.writeFile(filePath, JSONFormatUtils.formatJson(result));
    }
}

