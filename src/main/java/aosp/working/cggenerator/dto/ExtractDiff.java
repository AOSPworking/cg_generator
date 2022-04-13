package aosp.working.cggenerator.dto;

import java.util.List;
import java.util.Map;

public class ExtractDiff {
    public Map<String, List<ChangeFile>> commits;

    public Map<String, List<ChangeFile>> getCommits() {
        return this.commits;
    }

    public void setCommits(Map<String, List<ChangeFile>> commits) {
        this.commits = commits;
    }
}
