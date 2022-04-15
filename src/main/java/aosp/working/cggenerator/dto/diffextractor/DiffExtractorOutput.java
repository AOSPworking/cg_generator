package aosp.working.cggenerator.dto.diffextractor;

import aosp.working.cggenerator.dto.ChangeFile;
import aosp.working.cggenerator.util.FileUtil;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class DiffExtractorOutput {
    public Map<String, List<ChangeFile>> commits;

    public Map<String, List<ChangeFile>> getCommits() {
        return this.commits;
    }

    public void setCommits(Map<String, List<ChangeFile>> commits) {
        this.commits = commits;
    }

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
}
