package com.thot.html.editor.interfaces;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface IUserPreferences
{

	public static final boolean DEFAULT_CONFIRM_ON_EXIT = true;

	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	public static enum PREFERENCES_ID
	{

		CONFIRM_ON_EXIT,
		CHARSET;

	}

}
