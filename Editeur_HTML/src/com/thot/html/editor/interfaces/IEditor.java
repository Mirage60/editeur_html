package com.thot.html.editor.interfaces;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javafx.geometry.Insets;

public interface IEditor
{

	public static final double DIALOG_RATIO       = 0.9;
	public static final double HORIZONTAL_PADDING = 10;
	public static final double VERTICAL_PADDING   = 8;
	public static final double HORIZONTAL_SPACING = 10;
	public static final double VERTICAL_SPACING   = 8;

	public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

	public static final Insets GRID_PADDING = new Insets(VERTICAL_PADDING,HORIZONTAL_PADDING,VERTICAL_PADDING,HORIZONTAL_PADDING);

	public static enum WIDGET_ID
	{

		MENUBAR,
		ACCORDION,
		CENTER_AREA,
		STATUS_BAR,
		PREFERENCES_PANE,
		METADATA_PANE,
		MAIL_PANE;

	}

	public static enum FIELD_ID
	{

		DESCRIPTION,
		SEND_TO,
		SUBJECT,
		HTML_EDITOR,
		CHARSET;

	}

	public static enum MENUITEM_ID
	{

		ABOUT,
		EXIT,
		SAVE,
		SAVE_AS;

	}

}
