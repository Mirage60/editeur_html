package com.thot.html.editor.utils;

import java.io.File;

import com.thot.html.editor.interfaces.IConstants.SYSTEM_PROPERTY_ID;

public final class FileUtils
{

	//=========================================================================
	// CONSTRUCTEURS
	//=========================================================================

	private FileUtils()
	{

		super();

	}

	//=========================================================================
	// METHODES
	//=========================================================================

    public static final File getUserHome()
    {

		String value = SYSTEM_PROPERTY_ID.USER_HOME.getValue();

		value = value == null ? "" : value.trim();

		return "".equals(value) ? null : new File(value);

    }

}
