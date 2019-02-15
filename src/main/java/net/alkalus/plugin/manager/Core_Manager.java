package net.alkalus.plugin.manager;

import net.alkalus.api.interfaces.IPlugin;
import net.alkalus.api.objects.data.AutoMap;
import net.alkalus.api.objects.misc.AcLog;
import net.alkalus.core.util.reflect.ReflectionUtils;

public class Core_Manager {

	public static AutoMap<IPlugin> mPlugins = new AutoMap<IPlugin>();
	
	/**
	 * @param plugin - Dynamically registers the plugin for loading.
	 */
	public static void registerPlugin(IPlugin plugin) {
		AcLog.INFO("[Plugin] " + "Registered "+plugin.getPluginName()+".");
		mPlugins.put(plugin);
	}	
	
	/**
	 * Dynamically loads all class objects within the "gtPlusPlus.plugin" package.
	 */
	public static void veryEarlyInit() {
		if (ReflectionUtils.dynamicallyLoadClassesInPackage("gtPlusPlus.plugin")) {
			AcLog.INFO("[Plugin] Plugin System loaded.");
		}
	}
	
	public static boolean preInit() {
		try {			
			for (IPlugin h : mPlugins) {
				if (h.preInit()) {
					AcLog.INFO("[Plugin] Completed Pre-Init Phase for "+h.getPluginName()+".");
				}
				else {
					AcLog.INFO("[Plugin] Failed during Pre-Init Phase for "+h.getPluginName()+".");					
				}
			}			
			return true;
		}
		catch (Throwable t) {}
		return false;		
	}
	
	public static boolean init() {
		try {			
			for (IPlugin h : mPlugins) {
				if (h.init()) {
					AcLog.INFO("[Plugin] Completed Init Phase for "+h.getPluginName()+".");
				}
				else {
					AcLog.INFO("[Plugin] Failed during Init Phase for "+h.getPluginName()+".");					
				}
			}			
			return true;
		}
		catch (Throwable t) {}
		return false;		
	}
	
	public static boolean postInit() {
		try {			
			for (IPlugin h : mPlugins) {
				if (h.postInit()) {
					AcLog.INFO("[Plugin] Completed Post-Init Phase for "+h.getPluginName()+".");
				}
				else {
					AcLog.INFO("[Plugin] Failed during Post-Init Phase for "+h.getPluginName()+".");					
				}
			}			
			return true;
		}
		catch (Throwable t) {}
		return false;		
	}
	
	
}
