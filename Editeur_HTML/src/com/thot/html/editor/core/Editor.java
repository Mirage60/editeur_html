package com.thot.html.editor.core;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public final class Editor extends Application
{

	private static final double RATIO              = 0.9;
	private static final double HORIZONTAL_PADDING = 10;
	private static final double VERTICAL_PADDING   = 8;
	private static final double HORIZONTAL_SPACING = 10;
	private static final double VERTICAL_SPACING   = 8;

	private static final Insets GRID_PADDING = new Insets(VERTICAL_PADDING,HORIZONTAL_PADDING,VERTICAL_PADDING,HORIZONTAL_PADDING);

	private static enum WIDGET_ID
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

		SEND_TO,
		SUBJECT;

	}

	public static enum MENUITEM_ID
	{

		ABOUT,
		EXIT;

	}

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

		stackPane.setUserData(WIDGET_ID.CENTER_AREA);

		final ObservableList<Node> children = stackPane.getChildren();

		if (children != null)
		{

			final HTMLEditor htmlEditor = new HTMLEditor();

		    children.add(htmlEditor);

		}

		return stackPane;

	}

	private final HBox createStatusBar()
	{

		final HBox statusBar = new HBox();

		statusBar.setPrefHeight(30);
		statusBar.setUserData(WIDGET_ID.STATUS_BAR);

		return statusBar;

	}

	private final TitledPane createPreferencesPane()
	{

		final TitledPane titledPane = new TitledPane();

		titledPane.setText       ("Préférences"             );
		titledPane.setPrefHeight (400                       );
		titledPane.setUserData   (WIDGET_ID.PREFERENCES_PANE);

		final Pane gridPane = new GridPane();

		final ObservableList<Node> children = gridPane.getChildren();

		if (children != null)
		{

		}

		titledPane.setContent(gridPane);

		return titledPane;

	}

	private final TitledPane createMetadataPane()
	{

		final TitledPane titledPane = new TitledPane();

		titledPane.setText       ("Métadonnées"          );
		titledPane.setPrefHeight (400                    );
		titledPane.setUserData   (WIDGET_ID.METADATA_PANE);

		final Pane gridPane = new GridPane();

		final ObservableList<Node> children = gridPane.getChildren();

		if (children != null)
		{

		}

		titledPane.setContent(gridPane);

		return titledPane;

	}

	private final TitledPane createMailPane()
	{

		final TitledPane titledPane = new TitledPane();

		titledPane.setText       ("Messagerie"       );
		titledPane.setPrefHeight (200                );
		titledPane.setUserData   (WIDGET_ID.MAIL_PANE);

		final GridPane gridPane = new GridPane();

		final ObservableList<Node> children = gridPane.getChildren();

		if (children != null)
		{

			gridPane.setPadding (GRID_PADDING      );
			gridPane.setHgap    (HORIZONTAL_SPACING);
			gridPane.setVgap    (VERTICAL_SPACING  );

			final ColumnConstraints leftConstraint = new ColumnConstraints();

			leftConstraint.setMinWidth(100);
			leftConstraint.setHalignment(HPos.RIGHT);

			final ColumnConstraints rightConstraint = new ColumnConstraints();

			rightConstraint.setHgrow(Priority.NEVER);

			gridPane.getColumnConstraints().addAll(leftConstraint,rightConstraint);

			final ObservableList<String> sendItems = FXCollections.observableArrayList("To:", "Cc:", "Bcc:");

	        final ChoiceBox<String> sendTo = new ChoiceBox<>(sendItems);

	        sendTo.setPrefWidth(150);

	        final TextField sendToField = new TextField();

	        sendToField.setPrefWidth (400             );
	        sendToField.setUserData  (FIELD_ID.SEND_TO);

	        gridPane.addRow(0,sendTo,sendToField);

	        final Label subjectLabel = new Label("Sujet :");

	        subjectLabel.setAlignment (Pos.CENTER_RIGHT        );
	        subjectLabel.setMaxWidth  (Double.POSITIVE_INFINITY);

	        GridPane.setConstraints(subjectLabel,0,1);

	        final TextField subjectField = new TextField();

	        subjectField.setPrefWidth (400             );
	        subjectField.setUserData  (FIELD_ID.SUBJECT);

	        GridPane.setConstraints(subjectField,1,1);

	        gridPane.addRow(1,subjectLabel,subjectField);

		}

		titledPane.setContent(gridPane);

		return titledPane;

	}

	private final Accordion createAccordion()
	{

		final Accordion accordion = new Accordion();

		accordion.setPrefWidth (450                );
		accordion.setUserData  (WIDGET_ID.ACCORDION);

		final ObservableList<TitledPane> children = accordion.getPanes();

		if (children != null)
		{

			children.add(this.createPreferencesPane());
			children.add(this.createMetadataPane()   );
			children.add(this.createMailPane()       );

		}

		return accordion;

	}

	private final MenuItem createExitMenuItem()
	{

		final MenuItem menuItem = new MenuItem();

		menuItem.setText     ("Quitter"       );
		menuItem.setUserData (MENUITEM_ID.EXIT);

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

		menuItem.setText     ("\u00C0 propos\u2026");
		menuItem.setUserData (MENUITEM_ID.ABOUT    );

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

		menuBar.setUserData(WIDGET_ID.MENUBAR);

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

		borderPane.setCache   (true         );
		borderPane.setPadding (new Insets(0));

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
