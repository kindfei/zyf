package com.emcom.jtools.internal;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.emcom.jtools.JToolsPlugin;

/**
 * A simple wrapper over the eclipse built-in logger.
 * 
 * @author Adarsh
 * 
 * @version 1.0, 10th December 2006
 */
public class Logger {
	/**
	 * This instance will be used in all the log methods.
	 */
	private static final ILog LOG = JToolsPlugin.getDefault().getLog();

	/**
	 * Logs a message at debug level.
	 * 
	 * @param message
	 *            the message to be logged.
	 */
	public static void debug(String message) {
		IStatus status = new Status(Status.OK, JToolsPlugin.PLUGIN_ID, Status.OK, message, null);

		LOG.log(status);
	}

	/**
	 * Logs a message at information level.
	 * 
	 * @param message
	 *            the message to be logged.
	 */
	public static void info(String message) {
		IStatus status = new Status(Status.INFO, JToolsPlugin.PLUGIN_ID, Status.OK, message, null);

		LOG.log(status);
	}

	/**
	 * Logs a message at warning level.
	 * 
	 * @param message
	 *            the message to be logged.
	 */
	public static void warn(String message) {
		IStatus status = new Status(Status.WARNING, JToolsPlugin.PLUGIN_ID, Status.OK, message, null);

		LOG.log(status);
	}

	/**
	 * Logs a message at error level.
	 * 
	 * @param message
	 *            the message to be logged.
	 */
	public static void error(String message) {
		IStatus status = new Status(Status.ERROR, JToolsPlugin.PLUGIN_ID, Status.OK, message, null);

		LOG.log(status);
	}

	/**
	 * Logs a message at error level. This method must be used for logging
	 * exceptions.
	 * 
	 * @param message
	 *            the message to be logged.
	 * 
	 * @param throwable
	 *            the exception to be logged.
	 */
	public static void error(String message, Throwable throwable) {
		IStatus status = new Status(Status.ERROR, JToolsPlugin.PLUGIN_ID, Status.OK, message, throwable);

		LOG.log(status);
	}
}
