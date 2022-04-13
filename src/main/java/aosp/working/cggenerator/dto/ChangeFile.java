package aosp.working.cggenerator.dto;

import java.util.List;

public class ChangeFile {
    public String name;
    public List<String> methods;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMethods() {
        return this.methods;
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
    }
}
