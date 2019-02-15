package net.alkalus.api.objects.misc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.alkalus.core.lib.CORE;

public class AcLog {
	
	private final Logger logger;	
	
	public AcLog(String aLoggerName) {
		logger = makeLogger(aLoggerName, true);
	}
	
	// Static Logger used by core
	private static final Logger modLogger = AcLog.makeLogger("AlkCore_API", false);

	// Generate Logger
	private static Logger makeLogger(String aLoggerName, boolean aChild) {
		return LogManager.getLogger(aLoggerName);
	}

	public final Logger getLogger(){
		return logger;
	}
	
	/*
	 * Static Helpers
	 */

	/**
	 * Static Function to log INFO
	 * @param s - String to log.
	 */
	public static void INFO(final String s) {
		modLogger.info(s);
	}

	/**
	 * Static Function to log WARNINGS
	 * @param s - String to log.
	 */
	public static void WARNING(final String s) {
		if (CORE.DEBUG) {
			modLogger.warn(s);
		}
	}

	/**
	 * Static Function to log ERRORS
	 * @param s - String to log.
	 */
	public static void ERROR(final String s) {
		if (CORE.DEBUG) {
			modLogger.fatal(s);
		}
	}	
	
	/*
	 * Instanced Functions
	 */
	
	/**
	 * Instanced Function to log INFO
	 * @param s
	 */
	public void info(final String s) {
		getLogger().info(s);
	}
	
	/**
	 * Instanced Function to log WARNINGS
	 * @param s
	 */
	public void warn(final String s) {
		getLogger().warn(s);
	}
	
	/**
	 * Instanced Function to log ERRORS
	 * @param s
	 */
	public void error(final String s) {
		getLogger().error(s);
	}
	
	

}