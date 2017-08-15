package com.thot.html.editor.interfaces;

public interface IConstants
{

    public static enum SYSTEM_PROPERTY_ID
    {

		USER_HOME        ("user.home"      ),
		TEMP_DIRECTORY   ("java.io.tmpdir" ),
		LINE_SEPARATOR   ("line.separator" ),
		CLASS_PATH       ("java.class.path"),
		OPERATING_SYSTEM ("os.name"        );

		private String name = "";

		@SuppressWarnings("hiding")
		SYSTEM_PROPERTY_ID(final String name)
		{

		    this.name= name == null ? "" : name.trim();

		}

		public final String getValue()
		{

		    final String value = "".equals(this.name) ? "" : System.getProperty(this.name);

		    return value == null ? "" : LINE_SEPARATOR.equals(this) ? value : value.trim();

		}

    }

}
