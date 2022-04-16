package aosp.working.cggenerator;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Global {
    public static Logger logger = Logger.getLogger(Global.class);
    public static Properties props = new Properties();

    public static void initConfig() {
        try (FileInputStream fis = new FileInputStream("io.properties")) {
            Global.props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initLogger() {
        PropertyConfigurator.configure("log4j.properties");
        Global.logger.info("org.apache.log4j.Logger has set up!");
    }
}
