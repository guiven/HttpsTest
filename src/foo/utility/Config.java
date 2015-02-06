package foo.utility;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.xml.DOMConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 配置实现工具类
 * @author ChenJie
 *
 */

public class Config {
	public static Properties prop;
	static Logger logger = LoggerFactory.getLogger(Config.class);
	public static void init() throws FileNotFoundException, IOException{
		String configDir = "config";
		String log4jFile = configDir+File.separator+"log4j.xml";
		String propFile = configDir+File.separator+"Config.properties";
		DOMConfigurator.configure(log4jFile);
		logger.info(StringOpt.concat("配置Log4j信息：", new File(log4jFile).getAbsolutePath()));
		prop = new Properties();
		prop.load(new FileReader(propFile));
		logger.info(StringOpt.concat("配置默认路径信息：", new File(propFile).getAbsolutePath()));
	}
}
