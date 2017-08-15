package com.thot.html.editor.core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public final class Editor extends Application
{
	
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
			
			final BorderPane borderPane = new BorderPane();
			
			this.scene = new Scene(borderPane,800,600);
			
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
