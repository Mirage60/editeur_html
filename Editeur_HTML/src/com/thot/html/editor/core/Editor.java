package com.thot.html.editor.core;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public final class Editor extends Application
{

	private static final double RATIO = 0.9;

	private Stage stage = null;
	private Scene scene = null;

	//=========================================================================
	// CONSTRUCTEURS
	//=========================================================================

	public Editor()
	{

		super();

	}

	//=========================================================================
	// METHODES
	//=========================================================================

	@Override
	public final void start(final Stage primaryStage) throws Exception
	{

		try
		{

			this.stage = primaryStage;

			if (this.stage == null) return;

			final Screen screen = Screen.getPrimary();

			if (screen == null) throw new Exception("Failed to retrieve screen instance");

			final Rectangle2D bounds = screen.getVisualBounds();

			if (bounds == null) throw new Exception("Failed to retrieve screen bounds instance");

			if (this.stage == null) throw new IllegalArgumentException("Invalid stage instance");

			final BorderPane borderPane = new BorderPane();

			borderPane.setCache(true);

			final double width  = bounds.getWidth()  * RATIO;
			final double height = bounds.getHeight() * RATIO;
			final double left   = bounds.getMinX() + (bounds.getWidth()  - width ) / 2;
			final double top    = bounds.getMinY() + (bounds.getHeight() - height) / 2;

			this.scene = new Scene(borderPane,800,600);

			this.stage.setTitle  ("Editeur HTML");
			this.stage.setX      (left          );
			this.stage.setY      (top           );
			this.stage.setWidth  (width         );
			this.stage.setHeight (height        );

			this.stage.show();

		}
		catch (final Throwable exception)
		{

			Logger.log(exception);

		}

	}

	public static final void main(final String[] arguments)
	{

		try
		{

			launch(arguments);

			System.exit(0);

		}
		catch (final Throwable exception)
		{

			exception.printStackTrace();

			System.exit(1);

		}

	}

}
