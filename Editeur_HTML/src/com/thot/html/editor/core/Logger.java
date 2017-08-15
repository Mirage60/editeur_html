package com.thot.html.editor.core;

public final class Logger
{
	
	private static final Logger instance = new Logger();
	
	//=========================================================================
	// CONSTRUCTEURS
	//=========================================================================
	
	private Logger()
	{
		
		super();
		
	}
	
	//=========================================================================
	// METHODES
	//=========================================================================

	public static final Logger getInstance()
	{

		return instance;

	}

	public static final synchronized void log(final Throwable error)
	{

		try
		{

			if (error == null) return;

			error.printStackTrace(System.err);

			System.err.flush();

		}
		catch (final Throwable exception)
		{

			exception.printStackTrace(System.err);

			System.err.flush();

		}

	}

	public static final synchronized void log(final String message)
	{

		try
		{

			if ((message == null ? "" : message.trim()).length() == 0) return;

			System.out.println(message);

			System.out.flush();

		}
		catch (final Throwable exception)
		{

			exception.printStackTrace(System.err);

			System.err.flush();

		}

	}

}
