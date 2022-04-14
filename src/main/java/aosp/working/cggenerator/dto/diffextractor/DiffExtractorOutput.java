package aosp.working.cggenerator.dto.diffextractor;

import aosp.working.cggenerator.dto.ChangeFile;

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
}
