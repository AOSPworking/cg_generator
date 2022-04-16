package aosp.working.cggenerator;

import aosp.working.cggenerator.cg.JCallGraph;
import aosp.working.cggenerator.dto.CenterGraph;
import aosp.working.cggenerator.dto.JarInfo;
import aosp.working.cggenerator.dto.ninjahacked.NinjaOriginOutput;
import aosp.working.cggenerator.dto.diffextractor.DiffExtractorOutput;
import aosp.working.cggenerator.dto.ninjahacked.Node;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void setup() {
        Global.initConfig();
        Global.initLogger();
    }

    public static void main(String[] args) {
        setup();
        String ninjaHackedOriginOutputJSONPath
                = Global.props.getProperty("ninja.hacked.output.file.path");
        String diffExtractorOutputJSONPath
                = Global.props.getProperty("diff.extractor.output.file.path");
        NinjaOriginOutput noo = NinjaOriginOutput.make(ninjaHackedOriginOutputJSONPath);
        DiffExtractorOutput deo = DiffExtractorOutput.make(diffExtractorOutputJSONPath);

        if (args.length < 1) {
            throw new IllegalArgumentException("args.length < 1, need jar-path prefix in order to get all affected jar files using ninja-hacked output.");
        }
        String prefix = args[0];
        List<String> jarFilesPath = noo.getAllAffectedJarFilesPath(prefix);

        // get jar info
        JCallGraph jcg = new JCallGraph();
        List<JarInfo> jarInfos = new ArrayList<>();
        for (String jarFilePath : jarFilesPath) {
            JarInfo jarInfo = jcg.getAllInfoOfJar(jarFilePath);
            if (jarInfo != null) {
                jarInfos.add(jarInfo);
            }
            jarInfo.writeJSON();
        }


        Set<String> changeMethods = deo.getAllChangeMethods();
        for (JarInfo jarInfo : jarInfos) {
            List<CenterGraph.CallRelationship> relationships
                    = jarInfo.getAllMethodWhoCalling(changeMethods);
            CenterGraph centerGraph = new CenterGraph(jarInfo.getJarPath(), relationships);
            centerGraph.writeJSON();
        }


    }

}
