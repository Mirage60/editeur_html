package com.thot.html.editor.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;

import com.thot.html.editor.interfaces.IEditor;
import com.thot.html.editor.utils.FileUtils;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public final class Editor extends Application implements IEditor
{

	private final UserPreferences userPreferences = new UserPreferences()
	{

		@Override
		public final Editor getEditor()
		{

			return Editor.this;

		}

	};


	private static final ColumnConstraints leftConstraint = new ColumnConstraints();
	private static final ColumnConstraints rightConstraint = new ColumnConstraints();
	private final EventHandler<WindowEvent> onExitEventHandler = new OnExitEventHandler();
	private final Map<FIELD_ID,Node> fields = new Hashtable<>();
	private Stage stage = null;
	private Scene scene = null;
	private boolean isClosing = false;
	private File defaultDirectory = FileUtils.getUserHome();

	static
	{

		try
		{

			leftConstraint.setMinWidth   (100       );
			leftConstraint.setHalignment (HPos.RIGHT);

			rightConstraint.setHgrow(Priority.NEVER);

		}
		catch (final Throwable exception)
		{

			exception.printStackTrace();

		}

	}

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

	public final UserPreferences getUserPreferences()
	{

		return this.userPreferences;

	}

	public final Charset getCharset() throws Exception
	{

 		final FIELD_ID fieldID = FIELD_ID.CHARSET;

 		final Node node = (this.fields == null ? 0 : this.fields.size()) == 0 ? null : this.fields.containsKey(fieldID) ? this.fields.get(fieldID) : null;

 		final ChoiceBox<?> selector = node == null ? null : node instanceof ChoiceBox ? (ChoiceBox<?>) node : null;

 		if (selector == null) throw new Exception("Failed to retrieve selector instance");

 		final Object value = selector.getValue();

 		String valueStr = value == null ? "" : value.toString();

 		valueStr = valueStr == null ? "" : valueStr.trim();

 		return "".equals(valueStr) ? null : Charset.forName(valueStr);

	}

	public final String getHTML() throws Exception
	{

 		final FIELD_ID fieldID = FIELD_ID.HTML_EDITOR;

 		final Node node = (this.fields == null ? 0 : this.fields.size()) == 0 ? null : this.fields.containsKey(fieldID) ? this.fields.get(fieldID) : null;

 		final HTMLEditor htmlEditor = node == null ? null : node instanceof HTMLEditor ? (HTMLEditor) node : null;

 		if (htmlEditor == null) throw new Exception("Failed to retrieve HTML editor instance");

 		final String html = htmlEditor.getHtmlText();

 		return html == null ? "" : html.trim();

	}

	private final StackPane createStackPane()
	{

		final StackPane stackPane = new StackPane();

		stackPane.setUserData(WIDGET_ID.CENTER_AREA);

		final ObservableList<Node> children = stackPane.getChildren();

		if (children != null)
		{

			final HTMLEditor htmlEditor = new HTMLEditor();

			final FIELD_ID htmlEditorFieldID = FIELD_ID.HTML_EDITOR;

			htmlEditor.setEffect   (new DropShadow(5,4,4,Color.GRAY));
			htmlEditor.setPadding  (new Insets(0)                   );
			htmlEditor.setUserData (htmlEditorFieldID               );

			this.fields.put(htmlEditorFieldID,htmlEditor);

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

		final GridPane gridPane = new GridPane();

		final ObservableList<Node> children = gridPane.getChildren();

		if (children != null)
		{

			gridPane.setPadding (GRID_PADDING      );
			gridPane.setHgap    (HORIZONTAL_SPACING);
			gridPane.setVgap    (VERTICAL_SPACING  );

			gridPane.getColumnConstraints().addAll(leftConstraint,rightConstraint);

	        final Label charsetLabel = new Label("Jeu de caractères :");

	        charsetLabel.setAlignment (Pos.CENTER_RIGHT        );
	        charsetLabel.setMaxWidth  (Double.POSITIVE_INFINITY);

	        final FIELD_ID charsetFieldID = FIELD_ID.CHARSET;

	        final SortedMap<String,Charset> charsets = Charset.availableCharsets();

	        final int itemCount = charsets == null ? 0 : charsets.size();

	        final List<String> list = itemCount == 0 ? new ArrayList<>() : new ArrayList<>(charsets.keySet());

			final ObservableList<String> charsetItems = FXCollections.observableArrayList(list);

			String value = DEFAULT_CHARSET == null ? "" : DEFAULT_CHARSET.name();

			value = value == null ? "" : value.trim();

	        final ChoiceBox<String> charsetField = new ChoiceBox<>(charsetItems);

	        charsetField.setPrefWidth (400           );
	        charsetField.setUserData  (charsetFieldID);
	        charsetField.setDisable   (itemCount == 0);
	        charsetField.setValue     (value         );

	        this.fields.put(charsetFieldID,charsetField);

	        gridPane.addRow(0,charsetLabel,charsetField);

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

		final GridPane gridPane = new GridPane();

		final ObservableList<Node> children = gridPane.getChildren();

		if (children != null)
		{

			gridPane.setPadding (GRID_PADDING      );
			gridPane.setHgap    (HORIZONTAL_SPACING);
			gridPane.setVgap    (VERTICAL_SPACING  );

			gridPane.getColumnConstraints().addAll(leftConstraint,rightConstraint);

	        final Label descriptionLabel = new Label("Description :");

	        descriptionLabel.setAlignment (Pos.CENTER_RIGHT        );
	        descriptionLabel.setMaxWidth  (Double.POSITIVE_INFINITY);

	        final FIELD_ID descriptionFieldID = FIELD_ID.DESCRIPTION;

	        final TextArea descriptionField = new TextArea();

	        descriptionField.setPrefWidth (400               );
	        descriptionField.setUserData  (descriptionFieldID);

	        this.fields.put(descriptionFieldID,descriptionField);

	        gridPane.addRow(0,descriptionLabel,descriptionField);

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

			gridPane.getColumnConstraints().addAll(leftConstraint,rightConstraint);

			final ObservableList<String> sendItems = FXCollections.observableArrayList("To:", "Cc:", "Bcc:");

	        final ChoiceBox<String> sendTo = new ChoiceBox<>(sendItems);

	        sendTo.setPrefWidth(150);

	        final TextField sendToField = new TextField();

	        final FIELD_ID sendToFieldID = FIELD_ID.SEND_TO;

	        sendToField.setPrefWidth (400          );
	        sendToField.setUserData  (sendToFieldID);

	        this.fields.put(sendToFieldID,sendToField);

	        gridPane.addRow(0,sendTo,sendToField);

	        final Label subjectLabel = new Label("Sujet :");

	        subjectLabel.setAlignment (Pos.CENTER_RIGHT        );
	        subjectLabel.setMaxWidth  (Double.POSITIVE_INFINITY);

	        GridPane.setConstraints(subjectLabel,0,1);

	        final TextField subjectField = new TextField();

	        final FIELD_ID subjectFieldID = FIELD_ID.SUBJECT;

	        subjectField.setPrefWidth (400           );
	        subjectField.setUserData  (subjectFieldID);

	        this.fields.put(subjectFieldID,subjectField);

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

	private final MenuItem createSaveMenuItem()
	{

		final MenuItem menuItem = new MenuItem();

		menuItem.setText     ("Enregistrer"   );
		menuItem.setUserData (MENUITEM_ID.SAVE);

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

	private final MenuItem createSaveAsMenuItem()
	{

		final MenuItem menuItem = new MenuItem();

		menuItem.setText     ("Enregistrer sous\u2026");
		menuItem.setUserData (MENUITEM_ID.SAVE_AS     );

		menuItem.setOnAction(new EventHandler<ActionEvent>()
		{

			@Override
			public final void handle(final ActionEvent event)
			{

				try
				{

					Editor.this.saveAs();

				}
				catch (final Throwable exception)
				{

					Logger.log(exception);

				}

			}

		});

		return menuItem;

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

			    	Editor.this.stop();

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

			items.add(this.createSaveMenuItem()  );
			items.add(this.createSaveAsMenuItem());
			items.add(new SeparatorMenuItem()    );
			items.add(this.createExitMenuItem()  );

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

	public final SplitPane createSplitPane()
	{

		final SplitPane splitPane = new SplitPane();

		splitPane.setOrientation(Orientation.HORIZONTAL);
		splitPane.setDividerPositions(0.20);

		final ObservableList<Node> items = splitPane.getItems();

		if (items != null)
		{

			items.add(this.createAccordion());
			items.add(this.createStackPane());

		}

		return splitPane;

	}

	private final Pane createRoot()
	{

		final BorderPane borderPane = new BorderPane();

		borderPane.setCache   (true         );
		borderPane.setPadding (new Insets(0));

		borderPane.setTop    (this.createMenuBar()  );
		borderPane.setCenter (this.createSplitPane());
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

			this.stage.setOnCloseRequest(this.onExitEventHandler);

			final Screen screen = Screen.getPrimary();

			if (screen == null) throw new Exception("Failed to retrieve screen instance");

			final Rectangle2D bounds = screen.getVisualBounds();

			if (bounds == null) throw new Exception("Failed to retrieve screen bounds instance");

			if (this.stage == null) throw new IllegalArgumentException("Invalid stage instance");

			final Pane root = this.createRoot();

			final double width  = bounds.getWidth()  * DIALOG_RATIO;
			final double height = bounds.getHeight() * DIALOG_RATIO;
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

    @Override
	public final void stop() throws Exception
    {

    	super.stop();

    	if (this.stage != null)
    	{

    		final WindowEvent event = new WindowEvent(this.stage,WindowEvent.WINDOW_CLOSE_REQUEST);

    		this.stage.fireEvent(event);

    	}

    }

 	public final boolean confirm(final String title,final String header,final String message) throws Exception
    {

    	final Alert dialog = new Alert(AlertType.CONFIRMATION);

    	dialog.setResizable(false);
        dialog.setHeaderText(header == null ? "" : header.trim());
        dialog.setTitle(title == null ? "" : title.trim());
        dialog.setContentText(message == null ? "" : message.trim());

        final ObservableList<ButtonType> buttonTypes = dialog.getButtonTypes();

        if (buttonTypes == null) throw new Exception("Failed to retrieve button types instance");

        buttonTypes.setAll(ButtonType.YES,ButtonType.NO);

        final Optional<ButtonType> result = dialog.showAndWait();

        return result.isPresent() && result.get() == ButtonType.YES;

    }

 	private final void error(final String title,final String header,final String message)
 	{

 		try
 		{

 	    	final Alert dialog = new Alert(AlertType.ERROR);

 	    	dialog.setResizable(false);
 	        dialog.setHeaderText(header == null ? "" : header.trim());
 	        dialog.setTitle(title == null ? "" : title.trim());
 	        dialog.setContentText(message == null ? "" : message.trim());

 	        final ObservableList<ButtonType> buttonTypes = dialog.getButtonTypes();

 	        if (buttonTypes == null) throw new Exception("Failed to retrieve button types instance");

 	        buttonTypes.setAll(ButtonType.OK);

 	        dialog.showAndWait();

 		}
 		catch (final Throwable exception)
 		{

 			Logger.log(exception);

 		}

 	}

 	public final File saveAs()
 	{

 		try
 		{

 	 		if (this.stage == null) return null;

 	 		final FileChooser dialog = new FileChooser();

 	 		dialog.setInitialDirectory(this.defaultDirectory == null ? FileUtils.getUserHome() : this.defaultDirectory);

 	        final FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Fichiers texte (*.txt)","*.txt");

 	        final ObservableList<ExtensionFilter> filters = dialog.getExtensionFilters();

 	        if (filters != null)
 	        {

 		        filters.add(filter);

 	        }

 	        final File file = dialog.showSaveDialog(this.stage);

 	        if (file == null) return null;

 	        this.defaultDirectory = file.getParentFile();

 	        if (this.defaultDirectory == null) throw new Exception("Failed to retrieve directory instance");

 	        if (this.defaultDirectory.exists())
 	        {

 	        	if (this.defaultDirectory.canWrite() == false) throw new Exception("Write access denied to directory " + this.defaultDirectory.getAbsolutePath());

 	        }
 	        else
 	        {

 	        	if (this.defaultDirectory.mkdirs() == false) throw new Exception("Failed to create directory " + this.defaultDirectory.getAbsolutePath());

 	        }

 	        String html = this.getHTML();

 	       html = html == null ? "" : html.trim();

 	        final Charset charset = this.getCharset();

 	        if (charset == null) throw new Exception("Failed to retrieve charset instance");

 	        final FileOutputStream outputStream = new FileOutputStream(file);

 	        try
 	        {

 	        	final OutputStreamWriter writer = new OutputStreamWriter(outputStream,charset);

 	        	try
 	        	{

 	        		if ("".equals(html) == false)
 	        		{

 	        			writer.write(html);

 	        		}

 	        	}
 	        	finally
 	        	{

 	        		writer.flush();
 	        		writer.close();

 	        	}

 	        }
 	        finally
 	        {

 	        	outputStream.close();

 	        }

 	        return file;

 		}
 		catch (final Throwable exception)
 		{

 			Logger.log(exception);

 			this.error("Erreur","Enregistrement fichier","L'enregistrement du fichier a échoué");

 			return null;

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

    //==========================================================================
    // CLASSE INTERNE
    //==========================================================================

	private final class OnExitEventHandler implements EventHandler<WindowEvent>
	{

		public OnExitEventHandler()
		{

			super();

		}

		@Override
		public final void handle(final WindowEvent event)
		{

			try
			{

				if (event == null) return;

				if (Editor.this.isClosing == false)
				{

					if (Editor.this.confirm("Fin programme","Fin de programme demandée","Fin de programme confirmée ?"))
					{

						Editor.this.isClosing = true;

						Editor.this.stop();

					}
					else
					{

						event.consume();

					}

				}

			}
			catch (final Throwable exception)
			{

				Logger.log(exception);

			}

		}

	}
}
