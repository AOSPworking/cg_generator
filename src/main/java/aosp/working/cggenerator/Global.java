package aosp.working.cggenerator;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Global {
    public static Logger logger = Logger.getLogger(Global.class);

    public static void initLogger() {
        PropertyConfigurator.configure("log4j.properties");
        Global.logger.info("org.apache.log4j.Logger has set up!");
    }
}
