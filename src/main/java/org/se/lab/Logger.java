package org.se.lab;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple logger that saves all messages into a list of Strings
 *
 * @author rene
 */
class Logger
{
	/*
	 * Singleton Pattern
	 */
	private final static Logger INSTANCE = new Logger();
	public static Logger getInstance()
	{
		return INSTANCE;
	}


	/*
	 * Log List
	 */
	private List<String> logs;
	public final List<String> getLogList() {
		return logs;
	}

	/*
	 * Constructor
	 */
	protected Logger() {
		// init the list
		logs = new ArrayList<>();
	}


	/*
	 * Operations
	 */
	public void log(String msg)
	{
		logs.add(msg);
	}
}
