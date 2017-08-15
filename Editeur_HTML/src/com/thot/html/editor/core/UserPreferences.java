package com.thot.html.editor.core;

import java.util.UUID;
import java.util.prefs.Preferences;

import com.thot.html.editor.interfaces.IConstants.PREFERENCES_ID;
import com.thot.html.editor.interfaces.IUserPreferences;

public abstract class UserPreferences implements IUserPreferences
{

	private static final UUID PREFERENCES_ROOT_ID = UUID.fromString("A4AF15F8-0D47-498E-91DE-757ADAC82687");

	private final Preferences store = Preferences.userRoot().node(PREFERENCES_ROOT_ID.toString().toUpperCase());

	//=========================================================================
	// CONSTRUCTEURS
	//=========================================================================

	public UserPreferences()
	{

		super();

	}

	//=========================================================================
	// METHODES ABSTRAITES
	//=========================================================================

	public abstract Editor getEditor();

	//=========================================================================
	// METHODES
	//=========================================================================

    public final boolean getConfirmOnExit()
    {

    	try
    	{

    		return this.store == null ? DEFAULT_CONFIRM_ON_EXIT : this.store.getBoolean(PREFERENCES_ID.CONFIRM_ON_EXIT.name(),DEFAULT_CONFIRM_ON_EXIT);

    	}
    	catch (final Throwable exception)
    	{

    		Logger.log(exception);

    		return DEFAULT_CONFIRM_ON_EXIT;

    	}

    }

    public final void setConfirmOnExit(final boolean newValue)
    {

    	try
    	{

    		this.store.putBoolean(PREFERENCES_ID.CONFIRM_ON_EXIT.name(),newValue);

    	}
    	catch (final Throwable exception)
    	{

    		Logger.log(exception);

    	}

    }

}
