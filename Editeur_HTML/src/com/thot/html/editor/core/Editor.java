package com.thot.html.editor.core;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

	private final StackPane createStackPane()
	{

		final StackPane stackPane = new StackPane();

		return stackPane;

	}

	private final HBox createStatusBar()
	{

		final HBox statusBar = new HBox();

		statusBar.setPrefHeight(30);

		return statusBar;

	}

	private final TitledPane createPreferencesPane()
	{

		final TitledPane titledPane = new TitledPane();

		titledPane.setText       ("Préférences");
		titledPane.setPrefHeight (400          );

		final Pane gridPane = new GridPane();

		final ObservableList<Node> children = gridPane.getChildren();

		if (children != null)
		{

		}

		titledPane.setContent(gridPane);

		return titledPane;

	}

	private final Accordion createAccordion()
	{

		final Accordion accordion = new Accordion();

		accordion.setPrefWidth(400);

		final ObservableList<TitledPane> children = accordion.getPanes();

		if (children != null)
		{

			children.add(this.createPreferencesPane());

		}

		return accordion;

	}

	private final MenuItem createExitMenuItem()
	{

		final MenuItem menuItem = new MenuItem();

		menuItem.setText("Quitter");

		menuItem.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public final void handle(final ActionEvent event)
			{

				try
				{

			    	if (Editor.this.stage != null)
			    	{

			    		final WindowEvent closeEvent = new WindowEvent(Editor.this.stage,WindowEvent.WINDOW_CLOSE_REQUEST);

			    		Editor.this.stage.fireEvent(closeEvent);

			    	}

				}
				catch (final Throwable exception)
				{

					Logger.log(exception);

				}

			}

		});

		return menuItem;

	}

	private final Menu createFileMenu()
	{

		final Menu menu = new Menu("Fichier");

		final ObservableList<MenuItem> items = menu.getItems();

		if (items != null)
		{

			items.add(this.createExitMenuItem());

		}

		return menu;

	}

	private final MenuItem createAboutMenuItem()
	{

		final MenuItem menuItem = new MenuItem();

		menuItem.setText("\u00C0 propos\u2026");

		menuItem.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public final void handle(final ActionEvent event)
			{

				try
				{

				}
				catch (final Throwable exception)
				{

					Logger.log(exception);

				}

			}

		});

		return menuItem;

	}

	private final Menu createHelpMenu()
	{

		final Menu menu = new Menu("Aide");

		final ObservableList<MenuItem> items = menu.getItems();

		if (items != null)
		{

			items.add(this.createAboutMenuItem());

		}

		return menu;

	}

	private final MenuBar createMenuBar()
	{

		final MenuBar menuBar = new MenuBar();

		final ObservableList<Menu> menus = menuBar.getMenus();

		if (menus != null)
		{

			menus.add(this.createFileMenu());
			menus.add(this.createHelpMenu());

		}

		return menuBar;

	}

	private final Pane createRoot()
	{

		final BorderPane borderPane = new BorderPane();

		borderPane.setCache(true);

		borderPane.setTop    (this.createMenuBar()  );
		borderPane.setLeft   (this.createAccordion());
		borderPane.setCenter (this.createStackPane());
		borderPane.setBottom (this.createStatusBar());

		return borderPane;

	}

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

			final Pane root = this.createRoot();

			final double width  = bounds.getWidth()  * RATIO;
			final double height = bounds.getHeight() * RATIO;
			final double left   = bounds.getMinX() + (bounds.getWidth()  - width ) / 2;
			final double top    = bounds.getMinY() + (bounds.getHeight() - height) / 2;

			this.scene = new Scene(root);

			this.stage.setScene(this.scene);

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
