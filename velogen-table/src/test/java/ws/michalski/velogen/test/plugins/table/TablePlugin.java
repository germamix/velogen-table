package ws.michalski.velogen.test.plugins.table;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import ws.michalski.velogen.VeloGenCmd;
import ws.michalski.velogen.VeloGenManager;
import ws.michalski.velogen.config.Plugin;
import ws.michalski.velogen.exceptions.VeloGenCommandException;

public class TablePlugin {

	static VeloGenManager manager;
	static VeloGenCmd veloCmd = null;
	static Map<String, Object> pluginContext = null;
	
	@BeforeClass
	public static void startUp(){
		manager = new VeloGenManager();
		
		Plugin plugin = new Plugin();
		plugin.setClazz("ws.michalski.velogen.plugins.table.TablePlugIn");
		plugin.setName("table");
		plugin.setDescription("keine");
		
		manager.addPlugin(plugin);
		
	}
	
	@Test
	public void test() throws VeloGenCommandException {
		
		String[] parm = {"table", 
				"-d", "com.ibm.as400.access.AS400JDBCDriver",
				"-u", "jdbc:as400://192.168.xxx.xx/;metadata source=0",
				"-s", "mmtest",
				"-U", "user",
				"-P", "pass",
				"TABLE1", "VIEW1"};
		
		// Parameter verarbeiten
		veloCmd = manager.getCommands(parm);
		
//		manager.getUsage();
//		System.out.println(veloCmd.);
		
		// Plugin ausführen
		pluginContext = manager.run();

		System.out.println(pluginContext.size());
		
		fail("Not yet implemented");
	}

}
