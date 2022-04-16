package aosp.working.cggenerator.dto.diffextractor;

import aosp.working.cggenerator.dto.ChangeFile;
import aosp.working.cggenerator.util.FileUtil;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DiffExtractorOutput {
    public String id;
    public List<ChangeFile> changeFiles;

    /**
     * 根据 json 路径构造一个 DiffExtractorOutput 对象
     * @param path
     * @return
     */
    public static DiffExtractorOutput make(String path) {
        Gson gson = new Gson();
        String jsonContent = FileUtil.readFile(path);
        return gson.fromJson(jsonContent, DiffExtractorOutput.class);
    }

    /**
     * 获得一次 commit 中，所有被修改方法的全限定名
     * @return
     */
    public Set<String> getAllChangeMethods() {
        Set<String> allChangeMethods = new HashSet<>();
        for (ChangeFile changeFile : this.changeFiles) {
            allChangeMethods.addAll(changeFile.getMethods());
        }
        return allChangeMethods;
    }
}
