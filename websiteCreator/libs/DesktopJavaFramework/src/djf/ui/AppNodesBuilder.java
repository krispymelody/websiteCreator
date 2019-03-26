package djf.ui;

import static djf.AppTemplate.PATH_ICONS;
import djf.modules.AppGUIModule;
import djf.modules.AppLanguageModule;
import static djf.modules.AppLanguageModule.FILE_PROTOCOL;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import properties_manager.PropertiesManager;

/**
 * AppNodesBuilder
 *
 * This factory class provides convenient means for building common UI
 * components that can be easily integrated into DJF application.
 */
public class AppNodesBuilder {

    private AppLanguageModule languageSettings;
    private AppGUIModule gui;

    public AppNodesBuilder(AppGUIModule initGUI, AppLanguageModule initLanguageSettings) {
        gui = initGUI;
        languageSettings = initLanguageSettings;
    }

    public CheckBox buildCheckBox(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        CheckBox checkBox = new CheckBox();
        initNode(nodeId.toString(), checkBox, parentPane, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, checkBox);

        // AND RETURN IT
        return checkBox;
    }
    
        public CheckBox buildCheckBox(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        CheckBox checkBox = new CheckBox();
        initNode(nodeId.toString(), checkBox, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, checkBox);

        // AND RETURN IT
        return checkBox;
    }

    public ColorPicker buildColorPicker(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        ColorPicker colorPicker = new ColorPicker();
        initNode(nodeId, colorPicker, parentPane, styleClass, enabled);
        return colorPicker;
    }
    
    public ColorPicker buildColorPicker(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        ColorPicker colorPicker = new ColorPicker();
        initNode(nodeId, colorPicker, parent, col, row, colSpan, rowSpan, styleClass, enabled);
        return colorPicker;
    }    

    public ComboBox buildComboBox(Object nodeId,
            Object optionsListProperty,
            Object defaultValueProperty,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        // MAKE AND INIT THE COMBO BOX
        ComboBox comboBox = initComboBox(optionsListProperty, defaultValueProperty);
        initNode(nodeId, comboBox, parentPane, styleClass, enabled);        
        return comboBox;
    }
    
    public ComboBox buildComboBox(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled,
            Object optionsListProperty,
            Object defaultValueProperty) {
        // MAKE AND INIT THE COMBO BOX
        ComboBox comboBox = initComboBox(optionsListProperty, defaultValueProperty);
        initNode(nodeId, comboBox, parent, col, row, colSpan, rowSpan, styleClass, enabled);        
        return comboBox;
    }
    
    private ComboBox initComboBox(
            Object optionsListProperty,
            Object defaultValueProperty) {
        // LOAD THE OPTIONS INTO THE COMBO BOX
        ComboBox comboBox = new ComboBox();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ObservableList<String> items = comboBox.getItems();
        ArrayList<String> propertyOptions = props.getPropertyOptionsList(optionsListProperty);
        String defaultValue = props.getProperty(defaultValueProperty);
        if (defaultValue != null) {
            items.add(defaultValue);
            comboBox.getSelectionModel().select(defaultValue);
        }
        if (propertyOptions != null) {
            for (String s : propertyOptions) {
                if (!items.contains(s)) {
                    items.add(s);
                }
            }
        }
        return comboBox;
    }

    public GridPane buildGridPane(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        GridPane gridPane = new GridPane();
        initNode(nodeId, gridPane, parentPane, styleClass, enabled);
        return gridPane;
    }
    public HBox buildHBox(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        HBox box = new HBox();
        initNode(nodeId, box, parentPane, styleClass, enabled);
        return box;
    }
    
    public HBox buildHBox(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        HBox box = new HBox();
        initNode(nodeId, box, parent, col, row, colSpan, rowSpan, styleClass, enabled);
        return box;
    }

    public Label buildLabel(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        // MAKE AN INIT THE LABEL
        Label labelToBuild = new Label();
        initNode(nodeId, labelToBuild, parentPane, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        if (nodeId != null) {
            // MAKE SURE THE LANGUAGE MANAGER HAS IT
            // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
            initLabeledNode(nodeId, labelToBuild);
        }

        // AND RETURN IT. REMEMBER THAT THE TEXT AND TOOLTIP
        // WILL BE ADDED WHEN THE LANGUAGE SETTINGS ARE INITIALIZED
        // USING A LOADED LANGUAGE
        return labelToBuild;
    }
    public Label buildLabel(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // MAKE AN INIT THE LABEL
        Label labelToBuild = new Label();
        initNode(nodeId, labelToBuild, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        if (nodeId != null) {
            // MAKE SURE THE LANGUAGE MANAGER HAS IT
            // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
            initLabeledNode(nodeId, labelToBuild);
        }

        // AND RETURN IT. REMEMBER THAT THE TEXT AND TOOLTIP
        // WILL BE ADDED WHEN THE LANGUAGE SETTINGS ARE INITIALIZED
        // USING A LOADED LANGUAGE
        return labelToBuild;
    }
    private void initLabeledNode(Object nodeId, Labeled labeledNode) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String propName = nodeId.toString() + "_TEXT";
        String text = props.getProperty(propName);
        if (text != null) {
            languageSettings.addLabeledControlProperty(propName, labeledNode.textProperty());
        }
        initControlTooltip(nodeId, labeledNode);
    }
    private void initPromptNode(Object nodeId, TextInputControl tic) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String propName = nodeId.toString() + "_PROMPT_TEXT";
        String promptText = props.getProperty(propName);
        if (promptText != null) {
            languageSettings.addLabeledControlProperty(propName, tic.promptTextProperty());
        }        
        initControlTooltip(nodeId, tic);
    }
    private void initControlTooltip(Object nodeId, Control control) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String propName = nodeId.toString() + "_TOOLTIP";
        String tooltip = props.getProperty(propName);
        if (tooltip != null) {
            control.setTooltip(new Tooltip(""));
            languageSettings.addLabeledControlProperty(nodeId.toString() + "_TOOLTIP", control.tooltipProperty().get().textProperty());
        }        
    }
    public Slider buildSlider(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled,
            double minValue,
            double maxValue) {
        Slider slider = initSlider(minValue, maxValue);
        initNode(nodeId, slider, parentPane, styleClass, enabled);
        return slider;
    }
    public Slider buildSlider(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled,
            double minValue,
            double maxValue) {
        Slider slider = new Slider();
        initNode(nodeId, slider, parent, col, row, colSpan, rowSpan, styleClass, enabled);
        slider.setMin(minValue);
        slider.setMax(maxValue);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener((obs, oldval, newVal)
                -> slider.setValue(Math.round(newVal.doubleValue())));
        return slider;
    }    
    private Slider initSlider(double minValue, double maxValue) {
        Slider slider = new Slider();
        slider.setMin(minValue);
        slider.setMax(maxValue);
        slider.setSnapToTicks(true);
        slider.setMajorTickUnit(1);
        slider.setMinorTickCount(0);
        slider.setBlockIncrement(1);
        slider.setShowTickLabels(false);
        slider.setShowTickMarks(true);
        slider.valueProperty().addListener((obs, oldval, newVal)
                -> slider.setValue(Math.round(newVal.doubleValue())));
        return slider;
    }

    public VBox buildVBox(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        VBox box = new VBox();
        initNode(nodeId, box, parentPane, styleClass, enabled);
        return box;
    }
    public VBox buildVBox(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        VBox box = new VBox();
        initNode(nodeId, box, parent, col, row, colSpan, rowSpan, styleClass, enabled);
        return box;
    }
    public Button buildIconButton(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        // FIRST MAKE THE BASIC BUTTON
        Button button = buildTextButton(nodeId, parentPane, styleClass, enabled);

        attachIcon(button, nodeId);

        // AND RETURN THE BUTTON
        return button;
    }

    public Button buildTextButton(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE BUTTON
        Button button = initButton();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, button, parentPane, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, button);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }
    public Button buildTextButton(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE BUTTON
        Button button = initButton();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, button, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, button);
        button.setPadding(new Insets(5, 40, 5, 40));
        button.setMaxWidth(200);
        
        // AND RETURN THE COMPLETED BUTTON
        return button;
    }    
        public Button buildExpandButton(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE BUTTON
        Button button = initButton();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, button, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, button);
        button.setText("+");
        button.setStyle("-fx-background-radius: 10; -fx-pref-width: 35px; -fx-font-weight: bold;");
        //button.setPadding(new Insets(1, 10, 1, 10));
        
        // AND RETURN THE COMPLETED BUTTON
        return button;
    }  
        public String getImageViewID(ImageView im){
            String imID = im.getId().toString();
            return imID;
            
        }
        public Button buildDeleteButton(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE BUTTON
        Button button = initButton();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, button, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, button);
        button.setText("-");
        button.setStyle("-fx-background-radius: 10; -fx-pref-width: 35px; -fx-font-weight: bold;");
        //button.setPadding(new Insets(1, 10, 1, 10));

        
        // AND RETURN THE COMPLETED BUTTON
        return button;
    }  
                public Button buildDeleteButton(Object nodeId,
            HBox parent,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE BUTTON
        Button button = initButton();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, button, parent, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, button);
        button.setText("-");
        button.setStyle("-fx-background-radius: 10; -fx-pref-width: 35px");
        //button.setPadding(new Insets(1, 10, 1, 10));

        
        // AND RETURN THE COMPLETED BUTTON
        return button;
    }  
    
    
    private Button initButton() {
        Button button = new Button();
        button.setWrapText(true);
        button.setTextAlignment(TextAlignment.CENTER);   
        return button;
    }

    private void attachIcon(ButtonBase button, Object nodeId) {
        // LOAD THE ICON FROM THE PROVIDED FILE
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String iconProperty = nodeId.toString() + "_ICON";
        String imagePath = FILE_PROTOCOL + PATH_ICONS + props.getProperty(iconProperty);
        Image buttonImage = new Image(imagePath);
        if (!buttonImage.isError()) {
            button.setGraphic(new ImageView(buttonImage));
        }
    }

    public ToggleButton buildIconToggleButton(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TOGGLE BUTTON
        ToggleButton button = buildTextToggleButton(nodeId, parentPane, styleClass, enabled);

        // ADD THE IMAGE
        attachIcon(button, nodeId);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }
    public ToggleButton buildIconToggleButton(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TOGGLE BUTTON
        ToggleButton button = buildTextToggleButton(nodeId, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // ADD THE IMAGE
        attachIcon(button, nodeId);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }

    public ToggleButton buildTextToggleButton(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TOGGLE BUTTON
        ToggleButton button = initToggleButton();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, button, parentPane, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, button);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }
    
    public ToggleButton buildTextToggleButton(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TOGGLE BUTTON
        ToggleButton button = initToggleButton();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, button, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, button);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }    
    private ToggleButton initToggleButton() {
        ToggleButton button = new ToggleButton();
        button.setSelected(false);
        button.setDisable(false);
        return button;
    }

    // HELPER METHOD FOR INITIALIZING DJF NODES
    private void initNode(Object nodeId,
            Node node,
            Pane parentPane,
            String nodeStyleClass,
            boolean enabled) {

        // PUT THE BUTTON IN THE PARENT PANE IF THERE IS ONE
        if (parentPane != null) {
            parentPane.getChildren().add(node);
        }
        
        initNode(nodeId, node, nodeStyleClass, enabled);
    }
    
    private void initNode(Object nodeId,
            Node node,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan, 
            String nodeStyleClass,
            boolean enabled) {

        // PUT THE NODE IN THE PARENT PANE
        parent.add(node, col, row, colSpan, rowSpan);

        // AND INITIALIZE EVERYTHING ELSE
        initNode(nodeId, node, nodeStyleClass, enabled);
    }
    
    private void initNode(Object nodeId,
            Node node,
            String nodeStyleClass,
            boolean enabled) {
        // KEEP IT FOR LATER
        gui.addGUINode(nodeId, node);

        // SET THE STYLE
        node.getStyleClass().add(nodeStyleClass);

        // ENABLE/DISABLE
        node.setDisable(!enabled);
    }
    
    private RadioButton initRadioButton(ToggleGroup tg, boolean selected) {
        RadioButton button = new RadioButton();
                
        // PUT THE BUTTON INTO ITS PROPER TOGGLE GROUP
        button.setToggleGroup(tg);
        
        // IS IT SELECTED?
        button.setSelected(selected);
        
        return button;
    }
    
    public RadioButton buildRadioButton(Object nodeId,
                                            Pane parentPane,
                                            String styleClass,
                                            boolean enabled,
                                            ToggleGroup tg,
                                            boolean selected) {
        // NOW MAKE THE TOGGLE BUTTON
        RadioButton button = initRadioButton(tg, selected);

        // INITIALIZE THE OTHER SETTINGS
        this.initNode(nodeId, button, parentPane, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, button);

        // AND RETURN THE COMPLETED BUTTON
        return button;       
    }
    
    public RadioButton buildRadioButton(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled,
            ToggleGroup tg,
            boolean selected) {
        // NOW MAKE THE TOGGLE BUTTON
        RadioButton button = initRadioButton(tg, selected);

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, button, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initLabeledNode(nodeId, button);

        // AND RETURN THE COMPLETED BUTTON
        return button;
    }    

    public TextField buildTextField(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TEXT FIELD
        TextField textField = new TextField();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, textField, parentPane, styleClass, enabled);

        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initPromptNode(nodeId, textField);

        // AND RETURN THE COMPLETED BUTTON
        return textField;
    }
    public TextField buildTextField(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TEXT FIELD
        TextField textField = new TextField();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, textField, parent, col, row, colSpan, rowSpan, styleClass, enabled);
        
        // MAKE SURE THE LANGUAGE MANAGER HAS IT
        // SO THAT IT CAN CHANGE THE LANGUAGE AS NEEDED
        initPromptNode(nodeId, textField);

        // AND RETURN THE COMPLETED BUTTON
        return textField;
    }
    public TableView buildTableView(Object nodeId,
            Pane parentPane,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TABLE VIEW
        TableView tableView = new TableView();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, tableView, parentPane, styleClass, enabled);

        // AND RETURN THE COMPLETED BUTTON
        return tableView;
    }
    public TableView buildTableView(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TABLE VIEW
        TableView tableView = new TableView();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, tableView, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // AND RETURN THE COMPLETED BUTTON
        return tableView;
    }
    public TableColumn buildTableColumn(Object nodeId,
            TableView tableView,
            String styleClass) {
        TableColumn column = new TableColumn();
        tableView.getColumns().add(column);
        column.getStyleClass().add(styleClass);
        languageSettings.addLabeledControlProperty(nodeId.toString() + "_TEXT", column.textProperty());
        return column;
    }
    
    public ImageView buildImageView(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled, File file) throws MalformedURLException{
        String localUrl = file.toURI().toURL().toString();
        Image image = new Image(localUrl);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(33);
        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, imageView, parent, col, row, colSpan, rowSpan, styleClass, enabled);

        // AND RETURN THE COMPLETED BUTTON
        return imageView;
        
    }
    
    public TextArea buildTextArea(Object nodeId,
            HBox parent,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TEXT FIELD
        TextArea textArea = new TextArea();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, textArea, parent, styleClass, enabled);
        
        // AND RETURN THE COMPLETED BUTTON
        return textArea;
        }
        public TextArea buildTextArea(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TEXT FIELD
        TextArea textArea = new TextArea();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, textArea, parent, col, row, colSpan, rowSpan, styleClass, enabled);
        
        // AND RETURN THE COMPLETED BUTTON
        return textArea;
        }
        public DatePicker buildDatePicker(Object nodeId,
            GridPane parent,
            int col, int row, int colSpan, int rowSpan,
            String styleClass,
            boolean enabled) {
        // NOW MAKE THE TEXT FIELD
        DatePicker datePicker = new DatePicker();

        // INITIALIZE THE OTHER SETTINGS
        initNode(nodeId, datePicker, parent, col, row, colSpan, rowSpan, styleClass, enabled);
        
        // AND RETURN THE COMPLETED BUTTON
        return datePicker;
        }

}