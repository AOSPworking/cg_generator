package aosp.working.cggenerator.dto.ninjahacked;

import aosp.working.cggenerator.util.FileUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class NinjaOriginOutput {
    public List<Node> nodes;
    public List<Node> edges;
    public Integer node_num;
    public Integer edge_num;

    /**
     * 根据文件路径构造一个 NinjaOriginOutput 对象
     * @param path
     * @return
     */
    public static NinjaOriginOutput make(String path) {
        Gson gson = new Gson();
        String jsonContent = FileUtil.readFile(path);
        return gson.fromJson(jsonContent, NinjaOriginOutput.class);
    }

    /**
     * 获取所有 jar 包的路径
     * @return jar 包路径集合
     */
    public List<String> getAllAffectedJarFilesPath(String prefix) {
        List<String> result = new ArrayList<>();
        for (Node node : this.nodes) {
            if (node.name.endsWith(".jar")) {
                result.add(prefix + node.name);
            }
        }
        return result;
    }
}
