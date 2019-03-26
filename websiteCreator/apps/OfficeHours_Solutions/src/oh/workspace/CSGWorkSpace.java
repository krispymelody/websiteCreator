package oh.workspace;

import djf.components.AppWorkspaceComponent;
import djf.modules.AppFoolproofModule;
import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import properties_manager.PropertiesManager;
import oh.CSGApp;
import oh.CSGPropertyType;
import static oh.CSGPropertyType.*;
import oh.data.LectureData;
import oh.data.MTrecitationLab;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.workspace.controllers.CSGController;
import oh.workspace.dialogs.TADialog;
import oh.workspace.foolproof.CSGFoolproofDesign;
import static oh.workspace.style.OHStyle.*;
import static oh.data.CSGData.faviconPath;
import static oh.data.CSGData.navbarPath;
import static oh.data.CSGData.leftFooterPath;
import static oh.data.CSGData.rightFooterPath;


/**
 *
 * @author Kris Pan
 */
public class CSGWorkSpace extends AppWorkspaceComponent {
           static int b;
           static String oldBannerTitleValue;
           static String oldInstructorNameValue;
           static String oldInstructorRoomValue;
           static String oldInstructorEmailValue;
           static String oldInstructorHPValue;
           static String oldSiteTextValue;
           static String oldDescriptionValue;
           static String oldTopicsValue;
           static String oldPrereqValue;
           static String oldOutcomesValue;
           static String oldTextbookValue;
           static String oldGradedCompValue;
           static String oldGradingNoteValue;
           static String oldAcademicDisValue;
           static String oldSpecialAssistValue;
           static String oldComboBoxValue;

    public CSGWorkSpace(CSGApp app) {
        super(app);

        // LAYOUT THE APP
        initLayout();

        // INIT THE EVENT HANDLERS
        initControllers();

        // 
        initFoolproofDesign();

        // INIT DIALOGS
        initDialogs();
        
    }

    private void initDialogs() {
        TADialog taDialog = new TADialog((CSGApp) app);
        app.getGUIModule().addDialog(OH_TA_EDIT_DIALOG, taDialog);
    }

    // THIS HELPER METHOD INITIALIZES ALL THE CONTROLS IN THE WORKSPACE
    private void initLayout() {
        // FIRST LOAD THE FONT FAMILIES FOR THE COMBO BOX
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // THIS WILL BUILD ALL OF OUR JavaFX COMPONENTS FOR US
        AppNodesBuilder csBuilder = app.getGUIModule().getNodesBuilder();
        
        workspace = new BorderPane();

        // AND PUT EVERYTHING IN THE WORKSPACE
        //((BorderPane) workspace).setCenter(sPane);
        
        
        TabPane tabPane = new TabPane();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        //tabPane.setTabMinWidth(screenBounds.getWidth()/5-16);
        tabPane.tabMinWidthProperty().bind(workspace.widthProperty().divide(5).subtract(16));
        tabPane.setTabMinHeight(33);
        Tab siteTab = new Tab();
        siteTab.setClosable(false);
        Tab syllabusTab = new Tab();
        syllabusTab.setClosable(false);
        Tab meetingTab = new Tab();
        meetingTab.setClosable(false);
        Tab ohTab = new Tab();
        ohTab.setClosable(false);
        Tab scheduleTab = new Tab();  
        scheduleTab.setClosable(false);
        siteTab.setText("Site");
        syllabusTab.setText("Syllabus");
        meetingTab.setText("Meeting Times");
        ohTab.setText("Office Hours");
        scheduleTab.setText("Schedule");
        
        tabPane.getTabs().add(siteTab);
        tabPane.getTabs().add(syllabusTab);
        tabPane.getTabs().add(meetingTab);
        tabPane.getTabs().add(ohTab);
        tabPane.getTabs().add(scheduleTab);
        ((BorderPane)workspace).setCenter(tabPane);
        
        //SITE TAB
        //scroll pane
        ScrollPane siteScroll = new ScrollPane();
        
        siteTab.setContent(siteScroll);
        siteScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        siteScroll.setFitToWidth(ENABLED);
        siteScroll.setStyle("-fx-background: #ffb775;");
        
        VBox siteVBox = new VBox();
        siteScroll.setContent(siteVBox);
  
        //Banner Pane
        GridPane bannerGrid = new GridPane();
        bannerGrid.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: black");
        bannerGrid.setHgap(20); //horizontal gap in pixels => that's what you are asking for
        bannerGrid.setVgap(10); //vertical gap in pixels
        bannerGrid.setPadding(new Insets(5, 5, 5, 5));
        siteVBox.setMargin(bannerGrid, new Insets(5, 0, 5, 5));
        csBuilder.buildLabel(SITE_BANNER, bannerGrid,1,1,1,1, CLASS_OH_HEADER_LABEL, ENABLED);
        csBuilder.buildLabel(SITE_BANNER_SUBJECT,bannerGrid,1,2,1,1, CLASS_OH_LABEL, ENABLED);
        ComboBox subjectCombo = csBuilder.buildComboBox(SITE_BANNER_SUBJECT_COMBO,bannerGrid,2,2,1,1,CLASS_COMBO_BOX,ENABLED,SITE_BANNER_SUBJECT_OPTIONS,"CSE"); 
        subjectCombo.setEditable(ENABLED);
//         try {
//        BufferedReader subjectbr = new BufferedReader(new FileReader("work/combobox/subjectcombo.txt"));
//        StringBuilder subjectsb = new StringBuilder();
//        String line = subjectbr.readLine();
//        while (line != null) {
//            //Add Item
//            subjectCombo.getItems().add(line);
//            subjectsb.append(line);
//            line = subjectbr.readLine();
//        }
//        subjectbr.close();
//        } catch (Exception e) {        
//        }
        csBuilder.buildLabel(SITE_BANNER_NUMBER,bannerGrid,3,2,1,1, CLASS_OH_LABEL, ENABLED);
        ComboBox numberCombo = csBuilder.buildComboBox(SITE_BANNER_NUMBER_COMBO,bannerGrid,4,2,1,1,CLASS_COMBO_BOX,ENABLED,SITE_BANNER_NUMBER_OPTIONS,"219");
        numberCombo.setEditable(ENABLED); 
        csBuilder.buildLabel(SITE_BANNER_SEMESTER,bannerGrid,1,3,1,1, CLASS_OH_LABEL, ENABLED);
        ComboBox semesterCombo = csBuilder.buildComboBox(SITE_BANNER_SEMESTER_COMBO,bannerGrid,2,3,1,1,CLASS_COMBO_BOX,ENABLED,SITE_BANNER_SEMESTER_OPTIONS,"Spring");     
        csBuilder.buildLabel(SITE_BANNER_YEAR,bannerGrid,3,3,1,1, CLASS_OH_LABEL, ENABLED);
        ComboBox yearCombo = csBuilder.buildComboBox(SITE_BANNER_YEAR_COMBO,bannerGrid,4,3,1,1,CLASS_COMBO_BOX,ENABLED,"","2018");   
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int year2 = year +1;
        String yearStr = Integer.toString(year);
        String year2Str = Integer.toString(year2);
        yearCombo.getItems().addAll(yearStr, year2Str);
        csBuilder.buildLabel(SITE_BANNER_TITLE,bannerGrid,1,4,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildTextField(SITE_BANNER_TEXT_FIELD, bannerGrid,2 ,4 ,3 ,1 , "", ENABLED);
        csBuilder.buildLabel(SITE_BANNER_EXPORT_DIR,bannerGrid,1,5,1,1, CLASS_OH_LABEL, ENABLED);
        HBox bannerH = csBuilder.buildHBox("",bannerGrid,2,5,1,1,"",ENABLED);
        csBuilder.buildLabel(EXPORT_DIR_1, bannerH, "", ENABLED).setText(".\\export\\");    
        Label subjectLabel = csBuilder.buildLabel(EXPORT_DIR_2,bannerH,"", ENABLED);
        subjectCombo.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {     
            subjectLabel.textProperty().bind(Bindings.format(newValue+"_"));
            String subjectText = subjectCombo.getValue().toString();
                if(!subjectCombo.getItems().contains(subjectText)){
                subjectCombo.getItems().add(subjectText);
//                try{
//                BufferedReader subjectbr = new BufferedReader(new FileReader("work/combobox/subjectcombo.txt"));
//                subjectbr
//                }
//                catch(Exception e){
//                    
//                }
                }                
        });
        Label numberLabel = csBuilder.buildLabel(EXPORT_DIR_3,bannerH,"", ENABLED);
        numberCombo.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {     
            numberLabel.textProperty().bind(Bindings.format(newValue+"_"));
            String numberText = numberCombo.getValue().toString();
            if(!numberCombo.getItems().contains(numberText)){
                numberCombo.getItems().add(numberText);
                }
        });
        Label semesterLabel = csBuilder.buildLabel(EXPORT_DIR_4,bannerH,"", ENABLED);
        semesterCombo.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {     
            semesterLabel.textProperty().bind(Bindings.format(newValue+"_"));
        });
        Label yearLabel = csBuilder.buildLabel(EXPORT_DIR_5,bannerH,"", ENABLED);
        yearCombo.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {     
            yearLabel.textProperty().bind(Bindings.format(newValue+""));
        });
        csBuilder.buildLabel(EXPORT_DIR_6, bannerH, "", ENABLED).setText("\\public_html");    
    
        
        //Pages Pane
        GridPane pagesGrid = new GridPane();
        pagesGrid.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: black");
        pagesGrid.setHgap(20); 
        pagesGrid.setVgap(10); 
        pagesGrid.setPadding(new Insets(5, 5, 5, 5));
        csBuilder.buildLabel(SITE_PAGES, pagesGrid,1,1,1,1, CLASS_OH_HEADER_LABEL, ENABLED);
        siteVBox.setMargin(pagesGrid, new Insets(5, 0, 5, 5));
        csBuilder.buildCheckBox(SITE_PAGES_HOME_CHECK, pagesGrid, 3,1,1,1,"",ENABLED);
        csBuilder.buildLabel(SITE_PAGES_HOME,pagesGrid,4,1,1,1, "", ENABLED);
        csBuilder.buildCheckBox(SITE_PAGES_SYLLABUS_CHECK, pagesGrid, 6,1,1,1,"",ENABLED);
        csBuilder.buildLabel(SITE_PAGES_SYLLABUS,pagesGrid,7,1,1,1, "", ENABLED);
        csBuilder.buildCheckBox(SITE_PAGES_SCHEDULE_CHECK, pagesGrid, 9,1,1,1,"",ENABLED);
        csBuilder.buildLabel(SITE_PAGES_SCHEDULE,pagesGrid,10,1,1,1, "", ENABLED);
        csBuilder.buildCheckBox(SITE_PAGES_HW_CHECK, pagesGrid, 12,1,1,1,"",ENABLED);
        csBuilder.buildLabel(SITE_PAGES_HW,pagesGrid,13,1,1,1, "", ENABLED);

        //Style Pane
        GridPane styleGrid = new GridPane();
        styleGrid.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: black");
        styleGrid.setHgap(20); 
        styleGrid.setVgap(10); 
        styleGrid.setPadding(new Insets(5, 5, 5, 5));
        siteVBox.setMargin(styleGrid, new Insets(5, 0, 5, 5));
        csBuilder.buildLabel(SITE_STYLE, styleGrid,1,1,1,1, CLASS_OH_HEADER_LABEL, ENABLED);
        Button faviconButton = (Button)csBuilder.buildTextButton(SITE_STYLE_FAVICON, styleGrid,1,2,1,1 ,CLASS_TEXT_BUTTON, ENABLED);
        File favicon = new File("images/plain_shield.jpg");
        faviconPath = "images/plain_shield.jpg";
        try {
            csBuilder.buildImageView(SITE_STYLE_FAVICON_IMAGE, styleGrid,2,2,1,1 ,"", ENABLED,favicon);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CSGWorkSpace.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        csBuilder.buildTextButton(SITE_STYLE_NAVBAR, styleGrid,1,3,1,1 ,CLASS_TEXT_BUTTON, ENABLED);
        File navbar = new File("images/SBUDarkRedShieldLogo.png");
        navbarPath = "images/SBUDarkRedShieldLogo.png";
        try {
            csBuilder.buildImageView(SITE_STYLE_NAVBAR_IMAGE, styleGrid,2,3,1,1 ,"", ENABLED,navbar);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CSGWorkSpace.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        csBuilder.buildTextButton(SITE_STYLE_LEFT_FOOTER, styleGrid,1,4,1,1 ,CLASS_TEXT_BUTTON, ENABLED);
        File leftFooter = new File("images/SBUWhiteShieldLogo.jpg");
        leftFooterPath = "images/SBUWhiteShieldLogo.jpg";
        try {
            csBuilder.buildImageView(SITE_STYLE_LEFT_FOOTER_IMAGE, styleGrid,2,4,1,1 ,"", ENABLED,leftFooter);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CSGWorkSpace.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        csBuilder.buildTextButton(SITE_STYLE_RIGHT_FOOTER, styleGrid,1,5,1,1 ,CLASS_TEXT_BUTTON, ENABLED);
        File rightFooter = new File("images/SBUCSLogo.png");
        rightFooterPath = "images/SBUCSLogo.png";
        try {
            csBuilder.buildImageView(SITE_STYLE_RIGHT_FOOTER_IMAGE, styleGrid,2,5,1,1 ,"", ENABLED,rightFooter);
        } catch (MalformedURLException ex) {
            Logger.getLogger(CSGWorkSpace.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        csBuilder.buildLabel(SITE_STYLE_SHEET, styleGrid,1,6,1,1, CLASS_OH_LABEL, ENABLED);  
        ComboBox styleCombo = csBuilder.buildComboBox(SITE_STYLE_COMBO,styleGrid,2,6,1,1,CLASS_COMBO_BOX,ENABLED,"",DEFAULT_STYLE_COMBO);
        styleCombo.setOnMouseClicked(e->{
            styleCombo.getItems().clear();
            File dir = new File("./work/css/");
            File[] files = dir.listFiles((File dir1, String filename) -> filename.endsWith(".css"));
            String[] fileName = new String[files.length];
            for (File file : files) {
            String fileTemp = file.getName();
            styleCombo.getItems().add(fileTemp);        
        }
        });

        csBuilder.buildLabel(SITE_STYLE_NOTE, styleGrid,1,7,2,1, CLASS_OH_LABEL, ENABLED);  

        //Insturctor Grid
        GridPane instructorGrid = new GridPane();
        instructorGrid.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: black");
        instructorGrid.setHgap(10); 
        instructorGrid.setVgap(10); 
        instructorGrid.setPadding(new Insets(5, 5, 5, 5));
        siteVBox.setMargin(instructorGrid, new Insets(5, 0, 5, 5));
        csBuilder.buildLabel(SITE_INSTRUCTOR, instructorGrid,1,1,1,1, CLASS_OH_HEADER_LABEL, ENABLED);
        csBuilder.buildLabel(SITE_INSTRUCTOR_NAME, instructorGrid,1,2,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildTextField(SITE_INSTRUCTOR_NAME_INPUT, instructorGrid,2,2,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildLabel(SITE_INSTRUCTOR_ROOM, instructorGrid,3,2,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildTextField(SITE_INSTRUCTOR_ROOM_INPUT, instructorGrid,4,2,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildLabel(SITE_INSTRUCTOR_EMAIL, instructorGrid,1,3,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildTextField(SITE_INSTRUCTOR_EMAIL_INPUT, instructorGrid,2,3,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildLabel(SITE_INSTRUCTOR_HP, instructorGrid,3,3,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildTextField(SITE_INSTRUCTOR_HP_INPUT, instructorGrid,4,3,1,1, CLASS_OH_LABEL, ENABLED);
        Button instructorButton = csBuilder.buildExpandButton(SITE_INSTRUCTOR_BUTTON, instructorGrid, 1,4,1,1,"", ENABLED);
        csBuilder.buildLabel(SITE_INSTRUCTOR_OH, instructorGrid,2,4,1,1, CLASS_OH_LABEL, ENABLED);
        TextArea instructorTextArea = csBuilder.buildTextArea(SITE_TEXT_AREA, instructorGrid,1,5,4,4, "", ENABLED);
        instructorTextArea.setVisible(false);
        instructorTextArea.setManaged(false);
        instructorButton.setOnAction(e ->{
        if (instructorButton.getText().equals("+")){
            instructorButton.setText("-");
            instructorTextArea.setVisible(true);
            instructorTextArea.setManaged(true);            
        }
        else{
            instructorButton.setText("+");
            instructorTextArea.setVisible(false);
            instructorTextArea.setManaged(false);
        }
    });
        
        //Add all GridPanes to VBOX
        ObservableList list = siteVBox.getChildren(); 
        list.addAll(bannerGrid, pagesGrid, styleGrid, instructorGrid); 

        //Syllabus Pane
        ScrollPane sylScroll = new ScrollPane();
        syllabusTab.setContent(sylScroll);
        sylScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        sylScroll.setFitToWidth(ENABLED);
        
        sylScroll.setStyle("-fx-background: #ffb775;");

        VBox sylVBox = new VBox();
        sylScroll.setContent(sylVBox);
        sylVBox.minWidthProperty().bind(workspace.widthProperty().divide(5).subtract(16));

        
        GridPane descriptionGrid = setupGridPane();
        sylVBox.setMargin(descriptionGrid, new Insets(5, 5, 5, 5));
        Button descriptionButton = csBuilder.buildExpandButton(SYLLABUS_DESCRIPTION_BUTTON, descriptionGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SYLLABUS_DESCRIPTION, descriptionGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        setupTextArea(csBuilder,descriptionButton,descriptionGrid,SYLLABUS_DESCRIPTION_INPUT);

        GridPane topicsGrid = setupGridPane();
        sylVBox.setMargin(topicsGrid, new Insets(5, 5, 5, 5));
        Button topicsButton = csBuilder.buildExpandButton(SYLLABUS_TOPICS_BUTTON, topicsGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SYLLABUS_TOPICS, topicsGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        setupTextArea(csBuilder,topicsButton,topicsGrid,SYLLABUS_TOPICS_INPUT);
        
        GridPane prereqGrid = setupGridPane();
        sylVBox.setMargin(prereqGrid, new Insets(5, 5, 5, 5));
        Button prereqButton = csBuilder.buildExpandButton(SYLLABUS_PREREQ_BUTTON, prereqGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SYLLABUS_PREREQ, prereqGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        setupTextArea(csBuilder,prereqButton,prereqGrid,SYLLABUS_PREREQ_INPUT);
        
        GridPane outcomesGrid = setupGridPane();
        sylVBox.setMargin(outcomesGrid, new Insets(5, 5, 5, 5));
        Button outcomesButton = csBuilder.buildExpandButton(SYLLABUS_OUTCOMES_BUTTON, outcomesGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SYLLABUS_OUTCOMES, outcomesGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        setupTextArea(csBuilder,outcomesButton,outcomesGrid,SYLLABUS_OUTCOMES_INPUT);
        
        GridPane textbooksGrid = setupGridPane();
        sylVBox.setMargin(textbooksGrid, new Insets(5, 5, 5, 5));
        Button textbooksButton = csBuilder.buildExpandButton(SYLLABUS_TEXTBOOKS_BUTTON, textbooksGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SYLLABUS_TEXTBOOKS, textbooksGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        setupTextArea(csBuilder,textbooksButton,textbooksGrid,SYLLABUS_TEXTBOOKS_INPUT);
        
        GridPane gradedCompGrid = setupGridPane();
        sylVBox.setMargin(gradedCompGrid, new Insets(5, 5, 5, 5));
        Button gradedCompButton = csBuilder.buildExpandButton(SYLLABUS_GRADED_COMP_BUTTON, gradedCompGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SYLLABUS_GRADED_COMP, gradedCompGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        setupTextArea(csBuilder,gradedCompButton,gradedCompGrid,SYLLABUS_GRADED_COMP_INPUT);
        
        GridPane gradingNoteGrid = setupGridPane();
        sylVBox.setMargin(gradingNoteGrid, new Insets(5, 5, 5, 5));
        Button gradingNoteButton = csBuilder.buildExpandButton(SYLLABUS_GRADING_NOTE_BUTTON, gradingNoteGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SYLLABUS_GRADING_NOTE, gradingNoteGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        setupTextArea(csBuilder,gradingNoteButton,gradingNoteGrid,SYLLABUS_GRADING_NOTE_INPUT);
        
        GridPane academicDishonestyGrid = setupGridPane();
        sylVBox.setMargin(academicDishonestyGrid, new Insets(5, 5, 5, 5));
        Button academicDishonestyButton = csBuilder.buildExpandButton(SYLLABUS_ACADEMIC_DISHONESTY_BUTTON, academicDishonestyGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SYLLABUS_ACADEMIC_DISHONESTY, academicDishonestyGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        setupTextArea(csBuilder,academicDishonestyButton,academicDishonestyGrid,SYLLABUS_ACADEMIC_DISHONESTY_INPUT);
        
        GridPane specialAssistGrid = setupGridPane();
        sylVBox.setMargin(specialAssistGrid, new Insets(5, 5, 5, 5));
        Button specialAssistButton = csBuilder.buildExpandButton(SYLLABUS_SPECIAL_ASSIST_BUTTON, specialAssistGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SYLLABUS_SPECIAL_ASSIST, specialAssistGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        setupTextArea(csBuilder,specialAssistButton,specialAssistGrid,SYLLABUS_SPECIAL_ASSIST_INPUT);
        

        sylVBox.getChildren().addAll(descriptionGrid,topicsGrid,prereqGrid,outcomesGrid,
                textbooksGrid,gradedCompGrid,gradingNoteGrid,academicDishonestyGrid,specialAssistGrid);
        
        //Meeting Times Tab
        ScrollPane meetingScroll = new ScrollPane();
        meetingTab.setContent(meetingScroll);
        meetingScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        meetingScroll.setFitToWidth(ENABLED);
        meetingScroll.setStyle("-fx-background: #ffb775;");
        
        VBox meetingVBox = new VBox();
        meetingScroll.setContent(meetingVBox);
        meetingVBox.minWidthProperty().bind(workspace.widthProperty().subtract(16));

        GridPane lecturesGrid = setupGridPane();
        meetingVBox.setMargin(lecturesGrid, new Insets(5, 5, 5, 5));
        Button lecturesAddButton = csBuilder.buildExpandButton(MEETING_LECTURES_ADD_BUTTON, lecturesGrid, 1,1,1,1,"", ENABLED);
        Button lecturesDeleteButton = csBuilder.buildDeleteButton(MEETING_LECTURES_DELETE_BUTTON, lecturesGrid, 2,1,1,1,"", ENABLED);
        csBuilder.buildLabel(MEETING_LECTURES, lecturesGrid,3,1,1,1, CLASS_OH_LABEL, ENABLED);
        TableView<Object> lectureTable = csBuilder.buildTableView(MEETING_LECTURES_TABLE, lecturesGrid,1,2,10,10, CLASS_OH_TABLE_VIEW, ENABLED);
        lectureTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        lectureTable.setEditable(ENABLED);
        lectureTable.getSelectionModel().setCellSelectionEnabled(ENABLED);
        
        TableColumn lectureSectionColumn = csBuilder.buildTableColumn(MEETING_LECTURES_SECTION, lectureTable, CLASS_OH_COLUMN);
        TableColumn lectureDaysColumn = csBuilder.buildTableColumn(MEETING_LECTURES_DAYS, lectureTable, CLASS_OH_COLUMN);
        TableColumn lectureTimeColumn = csBuilder.buildTableColumn(MEETING_LECTURES_TIME, lectureTable, CLASS_OH_COLUMN);
        TableColumn lectureRoomColumn = csBuilder.buildTableColumn(MEETING_LECTURES_ROOM, lectureTable, CLASS_OH_COLUMN);
        lectureTable.minWidthProperty().bind(workspace.widthProperty().subtract(50));
        lectureTable.setMaxHeight(200);
        lectureSectionColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 6.0));
        lectureDaysColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(1.0 / 6.0));
        lectureTimeColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0 / 6.0));
        lectureRoomColumn.prefWidthProperty().bind(lectureTable.widthProperty().multiply(2.0 / 6.0));
        lectureColumnListener(lectureSectionColumn,1);
        lectureColumnListener(lectureDaysColumn,2);
        lectureColumnListener(lectureTimeColumn,3);
        lectureColumnListener(lectureRoomColumn,4);
        lectureSectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        lectureDaysColumn.setCellValueFactory(new PropertyValueFactory<String,String>("days"));
        lectureTimeColumn.setCellValueFactory(new PropertyValueFactory<String,String>("time"));
        lectureRoomColumn.setCellValueFactory(new PropertyValueFactory<String,String>("room"));


        
        GridPane recitationsGrid = setupGridPane();
        meetingVBox.setMargin(recitationsGrid, new Insets(5, 5, 5, 5));
        Button recitationsAddButton = csBuilder.buildExpandButton(MEETING_RECITATIONS_ADD_BUTTON, recitationsGrid, 1,1,1,1,"", ENABLED);
        Button recitationsDeleteButton = csBuilder.buildDeleteButton(MEETING_RECITATIONS_DELETE_BUTTON, recitationsGrid, 2,1,1,1,"", ENABLED);
        csBuilder.buildLabel(MEETING_RECITATIONS, recitationsGrid,3,1,1,1, CLASS_OH_LABEL, ENABLED);
        TableView<MTrecitationLab> recitationTable = csBuilder.buildTableView(MEETING_RECITATIONS_TABLE, recitationsGrid,1,2,10,10, CLASS_OH_TABLE_VIEW, ENABLED);
        recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        recitationTable.setEditable(ENABLED);
        recitationTable.getSelectionModel().setCellSelectionEnabled(ENABLED);

        TableColumn recitationSectionColumn = csBuilder.buildTableColumn(MEETING_RECITATIONS_SECTION, recitationTable, CLASS_OH_COLUMN);
        recitationColumnListener(recitationSectionColumn,1);
        TableColumn recitationDayTimeColumn = csBuilder.buildTableColumn(MEETING_RECITATIONS_DAYTIME, recitationTable, CLASS_OH_COLUMN);
        recitationColumnListener(recitationDayTimeColumn,2);
        TableColumn recitationRoomColumn = csBuilder.buildTableColumn(MEETING_RECITATIONS_ROOM, recitationTable, CLASS_OH_COLUMN);
        recitationColumnListener(recitationRoomColumn,3);
        TableColumn recitationTA1Column = csBuilder.buildTableColumn(MEETING_RECITATIONS_TA1, recitationTable, CLASS_OH_COLUMN);
        recitationColumnListener(recitationTA1Column,4);        
        TableColumn recitationTA2Column = csBuilder.buildTableColumn(MEETING_RECITATIONS_TA2, recitationTable, CLASS_OH_COLUMN);
        recitationColumnListener(recitationTA2Column,5);        

        recitationTable.minWidthProperty().bind(workspace.widthProperty().subtract(50));
        recitationTable.setMaxHeight(200);
        recitationSectionColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 8.0));
        recitationDayTimeColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(2.0 / 8.0));
        recitationRoomColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 8.0));
        recitationTA1Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(2.0 / 8.0));
        recitationTA2Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(2.0 / 8.0));
        recitationSectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        recitationDayTimeColumn.setCellValueFactory(new PropertyValueFactory<String,String>("dayTime"));
        recitationRoomColumn.setCellValueFactory(new PropertyValueFactory<String,String>("room"));
        recitationTA1Column.setCellValueFactory(new PropertyValueFactory<String,String>("ta1"));
        recitationTA2Column.setCellValueFactory(new PropertyValueFactory<String,String>("ta2"));

        
        GridPane labsGrid = setupGridPane();
        meetingVBox.setMargin(labsGrid, new Insets(5, 5, 5, 5));
        Button labsAddButton = csBuilder.buildExpandButton(MEETING_LABS_ADD_BUTTON, labsGrid, 1,1,1,1,"", ENABLED);
        Button labsDeleteButton = csBuilder.buildDeleteButton(MEETING_LABS_DELETE_BUTTON, labsGrid, 2,1,1,1,"", ENABLED);
        csBuilder.buildLabel(MEETING_LABS, labsGrid,3,1,1,1, CLASS_OH_LABEL, ENABLED);
        TableView<MTrecitationLab> labTable = csBuilder.buildTableView(MEETING_LABS_TABLE, labsGrid,1,2,10,10, CLASS_OH_TABLE_VIEW, ENABLED);
        labTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        labTable.setEditable(ENABLED);
        labTable.getSelectionModel().setCellSelectionEnabled(ENABLED);        
        TableColumn labSectionColumn = csBuilder.buildTableColumn(MEETING_LABS_SECTION, labTable, CLASS_OH_COLUMN);        
        TableColumn labDayTimeColumn = csBuilder.buildTableColumn(MEETING_LABS_DAYTIME, labTable, CLASS_OH_COLUMN);
        TableColumn labRoomColumn = csBuilder.buildTableColumn(MEETING_LABS_ROOM, labTable, CLASS_OH_COLUMN);
        TableColumn labTA1Column = csBuilder.buildTableColumn(MEETING_LABS_TA1, labTable, CLASS_OH_COLUMN);
        TableColumn labTA2Column = csBuilder.buildTableColumn(MEETING_LABS_TA2, labTable, CLASS_OH_COLUMN);
        labColumnListener(labSectionColumn,1);
        labColumnListener(labDayTimeColumn,2);
        labColumnListener(labRoomColumn,3);
        labColumnListener(labTA1Column,4);
        labColumnListener(labTA2Column,5);
        labSectionColumn.setCellValueFactory(new PropertyValueFactory<String, String>("section"));
        labDayTimeColumn.setCellValueFactory(new PropertyValueFactory<String,String>("dayTime"));
        labRoomColumn.setCellValueFactory(new PropertyValueFactory<String,String>("room"));
        labTA1Column.setCellValueFactory(new PropertyValueFactory<String,String>("ta1"));
        labTA2Column.setCellValueFactory(new PropertyValueFactory<String,String>("ta2"));
        
        
        labTable.minWidthProperty().bind(workspace.widthProperty().subtract(50));
        labTable.setMaxHeight(200);
        labSectionColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 8.0));
        labDayTimeColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(2.0 / 8.0));
        labRoomColumn.prefWidthProperty().bind(recitationTable.widthProperty().multiply(1.0 / 8.0));
        labTA1Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(2.0 / 8.0));
        labTA2Column.prefWidthProperty().bind(recitationTable.widthProperty().multiply(2.0 / 8.0));
              
        meetingVBox.getChildren().addAll(lecturesGrid, recitationsGrid, labsGrid);
        
        
             
        //OFFICE HOURS TAB
        
        // INIT THE HEADER ON THE LEFT
        VBox OHVbox = new VBox();
        VBox OHTopPane = csBuilder.buildVBox(OH_LEFT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox tasHeaderBox = csBuilder.buildHBox(OH_TAS_HEADER_PANE, OHTopPane, CLASS_OH_BOX, ENABLED);
        tasHeaderBox.setSpacing(15);
        csBuilder.buildDeleteButton(OH_TAS_DELETE_BUTTON, tasHeaderBox, "", ENABLED);
        csBuilder.buildLabel(CSGPropertyType.OH_TAS_HEADER_LABEL, tasHeaderBox, CLASS_OH_HEADER_LABEL, ENABLED);
        ToggleGroup tg = new ToggleGroup();
        csBuilder.buildRadioButton(OH_ALL_RADIO_BUTTON, tasHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, true);
        csBuilder.buildRadioButton(OH_GRAD_RADIO_BUTTON, tasHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);
        csBuilder.buildRadioButton(OH_UNDERGRAD_RADIO_BUTTON, tasHeaderBox, CLASS_OH_RADIO_BUTTON, ENABLED, tg, false);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        TableView<TeachingAssistantPrototype> taTable = csBuilder.buildTableView(OH_TAS_TABLE_VIEW, OHTopPane, CLASS_OH_TABLE_VIEW, ENABLED);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn nameColumn = csBuilder.buildTableColumn(OH_NAME_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn emailColumn = csBuilder.buildTableColumn(OH_EMAIL_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn slotsColumn = csBuilder.buildTableColumn(OH_SLOTS_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        TableColumn typeColumn = csBuilder.buildTableColumn(OH_TYPE_TABLE_COLUMN, taTable, CLASS_OH_COLUMN);
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<String, String>("email"));
        slotsColumn.setCellValueFactory(new PropertyValueFactory<String, String>("slots"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        nameColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        emailColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(2.0 / 5.0));
        slotsColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));
        typeColumn.prefWidthProperty().bind(taTable.widthProperty().multiply(1.0 / 5.0));

        // ADD BOX FOR ADDING A TA
        HBox taBox = csBuilder.buildHBox(OH_ADD_TA_PANE, OHTopPane, CLASS_OH_PANE, ENABLED);
        taBox.setSpacing(25);

        csBuilder.buildTextField(OH_NAME_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        csBuilder.buildTextField(OH_EMAIL_TEXT_FIELD, taBox, CLASS_OH_TEXT_FIELD, ENABLED);
        csBuilder.buildTextButton(OH_ADD_TA_BUTTON, taBox, CLASS_OH_BUTTON, !ENABLED);

        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(taTable, Priority.ALWAYS);

        // INIT THE HEADER ON THE RIGHT
        VBox rightPane = csBuilder.buildVBox(OH_RIGHT_PANE, null, CLASS_OH_PANE, ENABLED);
        HBox officeHoursHeaderBox = csBuilder.buildHBox(OH_OFFICE_HOURS_HEADER_PANE, rightPane, CLASS_OH_PANE, ENABLED);
        officeHoursHeaderBox.setSpacing(50);
        csBuilder.buildLabel(OH_OFFICE_HOURS_HEADER_LABEL, officeHoursHeaderBox, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildLabel(OH_OFFICE_HOURS_START_LABEL, officeHoursHeaderBox, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildComboBox(START_TIME_FILTER,TIME_OPTIONS,DEFAULT_START_TIME,officeHoursHeaderBox,"",ENABLED);
        csBuilder.buildLabel(OH_OFFICE_HOURS_END_LABEL, officeHoursHeaderBox, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildComboBox(END_TIME_FILTER,TIME_OPTIONS,DEFAULT_END_TIME,officeHoursHeaderBox,"",ENABLED);

        // SETUP THE OFFICE HOURS TABLE
        TableView<TimeSlot> officeHoursTable = csBuilder.buildTableView(OH_OFFICE_HOURS_TABLE_VIEW, rightPane, CLASS_OH_OFFICE_HOURS_TABLE_VIEW, ENABLED);
        officeHoursTable.setPrefHeight(600);
        setupOfficeHoursColumn(OH_START_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_COLUMN, "startTime");
        setupOfficeHoursColumn(OH_END_TIME_TABLE_COLUMN, officeHoursTable, CLASS_OH_COLUMN, "endTime");
        setupOfficeHoursColumn(OH_MONDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_COLUMN, "monday");
        setupOfficeHoursColumn(OH_TUESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_COLUMN, "tuesday");
        setupOfficeHoursColumn(OH_WEDNESDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_COLUMN, "wednesday");
        setupOfficeHoursColumn(OH_THURSDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_COLUMN, "thursday");
        setupOfficeHoursColumn(OH_FRIDAY_TABLE_COLUMN, officeHoursTable, CLASS_OH_COLUMN, "friday");
        
        // MAKE SURE IT'S THE TABLE THAT ALWAYS GROWS IN THE LEFT PANE
        VBox.setVgrow(officeHoursTable, Priority.ALWAYS);

        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        OHVbox.getChildren().addAll(OHTopPane, rightPane);
        ohTab.setContent(OHVbox);    
            
        
        //Schedule Tab
        ScrollPane scheduleScroll = new ScrollPane();
        scheduleTab.setContent(scheduleScroll);
        scheduleScroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
        scheduleScroll.setFitToWidth(ENABLED);
        scheduleScroll.setStyle("-fx-background: #ffb775;");
        
        VBox scheduleVBox = new VBox();
        scheduleScroll.setContent(scheduleVBox);
        scheduleVBox.minWidthProperty().bind(workspace.widthProperty().subtract(16));

        
        GridPane boundariesGrid = setupGridPane();
        meetingVBox.setMargin(boundariesGrid, new Insets(5, 5, 5, 5));
        csBuilder.buildLabel(SCHEDULE_BOUNDARIES, boundariesGrid,1,1,2,1, CLASS_OH_HEADER_LABEL, ENABLED);
        csBuilder.buildLabel(SCHEDULE_BOUNDARIES_STARTING, boundariesGrid,1,2,1,1, CLASS_OH_LABEL, ENABLED);
        DatePicker mondayDatePicker = (DatePicker)csBuilder.buildDatePicker(SCHEDULE_BOUNDARIES_STARTING_DATE, boundariesGrid,2,2,1,1, "", ENABLED);
        mondayDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.TUESDAY ||
                    date.getDayOfWeek() == DayOfWeek.WEDNESDAY ||
                    date.getDayOfWeek() == DayOfWeek.THURSDAY ||
                    date.getDayOfWeek() == DayOfWeek.FRIDAY ||
                    date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });
        mondayDatePicker.setEditable(false);
        csBuilder.buildLabel(SCHEDULE_BOUNDARIES_ENDING, boundariesGrid,3,2,1,1, CLASS_OH_LABEL, ENABLED);
        DatePicker fridayDatePicker = (DatePicker)csBuilder.buildDatePicker(SCHEDULE_BOUNDARIES_ENDING_DATE, boundariesGrid,4,2,1,1, "", ENABLED);
        fridayDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.getDayOfWeek() == DayOfWeek.TUESDAY ||
                    date.getDayOfWeek() == DayOfWeek.WEDNESDAY ||
                    date.getDayOfWeek() == DayOfWeek.THURSDAY ||
                    date.getDayOfWeek() == DayOfWeek.MONDAY ||
                    date.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    date.getDayOfWeek() == DayOfWeek.SUNDAY);
            }
        });
        GridPane scheduleItemGrid = setupGridPane();
        meetingVBox.setMargin(scheduleItemGrid, new Insets(5, 5, 5, 5));
        Button scheduleDeleteButton = csBuilder.buildDeleteButton(SCHEDULE_ITEMS_DELETE_BUTTON, scheduleItemGrid, 1,1,1,1,"", ENABLED);
        csBuilder.buildLabel(SCHEDULE_ITEMS, scheduleItemGrid,2,1,1,1, CLASS_OH_LABEL, ENABLED);
        TableView<Object> scheduleItemTable = csBuilder.buildTableView(SCHEDULE_ITEMS_TABLE, scheduleItemGrid,1,2,10,10, CLASS_OH_TABLE_VIEW, ENABLED);
        scheduleItemTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TableColumn scheduleTypeColumn = csBuilder.buildTableColumn(SCHEDULE_ITEMS_TYPE, scheduleItemTable, CLASS_OH_COLUMN);
        TableColumn scheduleDateColumn = csBuilder.buildTableColumn(SCHEDULE_ITEMS_DATE, scheduleItemTable, CLASS_OH_COLUMN);
        TableColumn scheduleTitleColumn = csBuilder.buildTableColumn(SCHEDULE_ITEMS_TITLE, scheduleItemTable, CLASS_OH_COLUMN);
        TableColumn scheduleTopicColumn = csBuilder.buildTableColumn(SCHEDULE_ITEMS_TOPIC, scheduleItemTable, CLASS_OH_COLUMN);
        scheduleItemTable.minWidthProperty().bind(workspace.widthProperty().subtract(50));
        scheduleItemTable.setMaxHeight(200);
        scheduleTypeColumn.prefWidthProperty().bind(scheduleItemTable.widthProperty().multiply(1.0 / 5.0));
        scheduleDateColumn.prefWidthProperty().bind(scheduleItemTable.widthProperty().multiply(1.0 / 5.0));
        scheduleTitleColumn.prefWidthProperty().bind(scheduleItemTable.widthProperty().multiply(1.0 / 5.0));
        scheduleTopicColumn.prefWidthProperty().bind(scheduleItemTable.widthProperty().multiply(2.0 / 5.0));
        scheduleTypeColumn.setCellValueFactory(new PropertyValueFactory<String, String>("type"));
        scheduleDateColumn.setCellValueFactory(new PropertyValueFactory<String, String>("date"));
        scheduleTitleColumn.setCellValueFactory(new PropertyValueFactory<String, String>("title"));
        scheduleTopicColumn.setCellValueFactory(new PropertyValueFactory<String, String>("topic"));

        
        GridPane scheduleAddEditGrid = setupGridPane();
        meetingVBox.setMargin(scheduleAddEditGrid, new Insets(5, 5, 5, 5));
        csBuilder.buildLabel(SCHEDULE_ADD_EDIT, scheduleAddEditGrid,1,1,1,1, CLASS_OH_HEADER_LABEL, ENABLED);
        csBuilder.buildLabel(SCHEDULE_ADD_EDIT_TYPE, scheduleAddEditGrid,1,2,1,1, CLASS_OH_LABEL, ENABLED);
        ComboBox scheduleTypeCombo = csBuilder.buildComboBox(SCHEDULE_TYPE_COMBO,scheduleAddEditGrid,2,2,1,1,CLASS_COMBO_BOX,ENABLED,SCHEDULE_TYPE_OPTIONS,"Options"); 
        scheduleTypeCombo.setEditable(ENABLED);
        scheduleTypeCombo.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {     
            numberLabel.textProperty().bind(Bindings.format(newValue+"_"));
            String text = scheduleTypeCombo.getValue().toString();
            if(!scheduleTypeCombo.getItems().contains(text)){
                scheduleTypeCombo.getItems().add(text);
                }
        });        
        csBuilder.buildLabel(SCHEDULE_ADD_EDIT_DATE, scheduleAddEditGrid,1,3,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildDatePicker(SCHEDULE_ADD_EDIT_DATE_PICK, scheduleAddEditGrid,2,3,1,1, "", ENABLED);
        csBuilder.buildLabel(SCHEDULE_ADD_EDIT_TITLE, scheduleAddEditGrid,1,4,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildTextField(SCHEDULE_ADD_EDIT_TITLE_INPUT, scheduleAddEditGrid,2,4,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildLabel(SCHEDULE_ADD_EDIT_TOPIC, scheduleAddEditGrid,1,5,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildTextField(SCHEDULE_ADD_EDIT_TOPIC_INPUT, scheduleAddEditGrid,2,5,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildLabel(SCHEDULE_ADD_EDIT_LINK, scheduleAddEditGrid,1,6,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildTextField(SCHEDULE_ADD_EDIT_LINK_INPUT, scheduleAddEditGrid,2,6,1,1, CLASS_OH_LABEL, ENABLED);
        csBuilder.buildTextButton(SCHEDULE_ADD_BUTTON, scheduleAddEditGrid,1,7,1,1 ,CLASS_TEXT_BUTTON, ENABLED);
        csBuilder.buildTextButton(SCHEDULE_ADD_EDIT_CLEAR_BUTTON, scheduleAddEditGrid,2,7,1,1 ,CLASS_TEXT_BUTTON, ENABLED);

        scheduleVBox.getChildren().addAll(boundariesGrid,scheduleItemGrid,scheduleAddEditGrid);

        
        
      
    }

    private void lectureColumnListener(TableColumn column, int columnIndex){
        CSGController controller = new CSGController((CSGApp) app);
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(        
            new EventHandler<CellEditEvent<LectureData, String>>() {
            @Override
            public void handle(CellEditEvent<LectureData, String> e) {
                controller.processEditLecture(e.getNewValue(), columnIndex);
            }            
            });
    }
    

    private void recitationColumnListener(TableColumn column, int columnIndex){
        CSGController controller = new CSGController((CSGApp) app);
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(        
            new EventHandler<CellEditEvent<MTrecitationLab, String>>() {
            @Override
            public void handle(CellEditEvent<MTrecitationLab, String> e) {
                controller.processEditRecitation(e.getNewValue(), columnIndex);
            }            
            });
    }
    private void labColumnListener(TableColumn column, int columnIndex){
        CSGController controller = new CSGController((CSGApp) app);
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(        
            new EventHandler<CellEditEvent<MTrecitationLab, String>>() {
            @Override
            public void handle(CellEditEvent<MTrecitationLab, String> e) {
                controller.processEditLab(e.getNewValue(), columnIndex);
            }            
            });
    }
    
    
    private GridPane setupGridPane(){
        GridPane gridpane = new GridPane();
        gridpane.setStyle("-fx-background-color: #f2f2f2;-fx-border-color: black");
        gridpane.setHgap(10); 
        gridpane.setVgap(10); 
        gridpane.setPadding(new Insets(5, 5, 5, 5));
        return gridpane;
    }
    
    private void setupTextArea(AppNodesBuilder csBuilder, Button instructorButton, GridPane instructorGrid, Object SITE_TEXT_AREA){
        TextArea textArea = csBuilder.buildTextArea(SITE_TEXT_AREA, instructorGrid,1,2,2,1, "", ENABLED);
        textArea.setPrefWidth(1500);
        textArea.setVisible(false);
        textArea.setManaged(false);
        instructorButton.setOnAction(e ->{
        if (instructorButton.getText().equals("+")){
            instructorButton.setText("-");
            textArea.setVisible(true);
            textArea.setManaged(true);            
        }
        else{
            instructorButton.setText("+");
            textArea.setVisible(false);
            textArea.setManaged(false);
        }
    });
        
        
    }
    private void setupOfficeHoursColumn(Object columnId, TableView tableView, String styleClass, String columnDataProperty) {
        AppNodesBuilder builder = app.getGUIModule().getNodesBuilder();
        TableColumn<TeachingAssistantPrototype, String> column = builder.buildTableColumn(columnId, tableView, styleClass);
        column.setCellValueFactory(new PropertyValueFactory<TeachingAssistantPrototype, String>(columnDataProperty));
        column.prefWidthProperty().bind(tableView.widthProperty().multiply(1.0 / 7.0));
        column.setCellFactory(col -> {
            return new TableCell<TeachingAssistantPrototype, String>() {
                @Override
                protected void updateItem(String text, boolean empty) {
                    super.updateItem(text, empty);
                    if (text == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // CHECK TO SEE IF text CONTAINS THE NAME OF
                        // THE CURRENTLY SELECTED TA
                        setText(text);
                        TableView<TeachingAssistantPrototype> tasTableView = (TableView) app.getGUIModule().getGUINode(OH_TAS_TABLE_VIEW);
                        TeachingAssistantPrototype selectedTA = tasTableView.getSelectionModel().getSelectedItem();
                        if (selectedTA == null) {
                            setStyle("");
                        } else if (text.contains(selectedTA.getName())) {
                            setStyle("-fx-background-color: lightgreen");
                        } else {
                            setStyle("");
                        }
                    }
                }
            };
        });
    }
    
    public  void comboBoxListener(ComboBox box){
                CSGController controller = new CSGController((CSGApp) app);

                box.focusedProperty().addListener(e ->{
            if (box.isFocused()){
                oldComboBoxValue = box.getValue().toString();

            }
             if (!box.isFocused()) {
                    String newValue = box.getValue().toString();
                   if(!oldComboBoxValue.equals(newValue))
                    {
                        controller.processComboBox(box, oldComboBoxValue, newValue);
                    }
             }
        });
    }

    public void initControllers() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        CSGController controller = new CSGController((CSGApp) app);
        AppGUIModule gui = app.getGUIModule();

        ComboBox subjectCombo = (ComboBox) gui.getGUINode(SITE_BANNER_SUBJECT_COMBO);
        ComboBox numberCombo = (ComboBox) gui.getGUINode(SITE_BANNER_NUMBER_COMBO);
        ComboBox semesterCombo = (ComboBox) gui.getGUINode(SITE_BANNER_SEMESTER_COMBO);
        ComboBox yearCombo = (ComboBox) gui.getGUINode(SITE_BANNER_YEAR_COMBO);      
        ImageView favicon = (ImageView) gui.getGUINode(SITE_STYLE_FAVICON_IMAGE);
        ImageView navbar = (ImageView) gui.getGUINode(SITE_STYLE_NAVBAR_IMAGE);
        ImageView leftFooter = (ImageView) gui.getGUINode(SITE_STYLE_LEFT_FOOTER_IMAGE);
        ImageView rightFooter = (ImageView) gui.getGUINode(SITE_STYLE_RIGHT_FOOTER_IMAGE);
        ComboBox styleCombo = (ComboBox) gui.getGUINode(SITE_STYLE_COMBO);
        TextArea instructorTextArea = (TextArea) gui.getGUINode(SITE_TEXT_AREA);
        TextField bannerTitleTextField = (TextField) gui.getGUINode(SITE_BANNER_TEXT_FIELD);
        TextField instructorNameTextField = (TextField) gui.getGUINode(SITE_INSTRUCTOR_NAME_INPUT);
        TextField instructorRoomTextField = (TextField) gui.getGUINode(SITE_INSTRUCTOR_ROOM_INPUT);
        TextField instructorEmailTextField = (TextField) gui.getGUINode(SITE_INSTRUCTOR_EMAIL_INPUT);
        TextField instructorHPTextField = (TextField) gui.getGUINode(SITE_INSTRUCTOR_HP_INPUT);
        TextArea descriptionTextArea = (TextArea) gui.getGUINode(SYLLABUS_DESCRIPTION_INPUT);
        TextArea topicsTextArea = (TextArea) gui.getGUINode(SYLLABUS_TOPICS_INPUT);
        TextArea prereqTextArea = (TextArea) gui.getGUINode(SYLLABUS_PREREQ_INPUT);
        TextArea outcomesTextArea = (TextArea) gui.getGUINode(SYLLABUS_OUTCOMES_INPUT);
        TextArea textbooksTextArea = (TextArea) gui.getGUINode(SYLLABUS_TEXTBOOKS_INPUT);
        TextArea gradedCompTextArea = (TextArea) gui.getGUINode(SYLLABUS_GRADED_COMP_INPUT);
        TextArea gradingNoteTextArea = (TextArea) gui.getGUINode(SYLLABUS_GRADING_NOTE_INPUT);
        TextArea academicDisTextArea = (TextArea) gui.getGUINode(SYLLABUS_ACADEMIC_DISHONESTY_INPUT);
        TextArea specialAssistTextArea = (TextArea) gui.getGUINode(SYLLABUS_SPECIAL_ASSIST_INPUT);
        ComboBox startTimeCombo = (ComboBox) gui.getGUINode(START_TIME_FILTER);
        ComboBox endTimeCombo = (ComboBox) gui.getGUINode(END_TIME_FILTER);
        CheckBox homeCheck = (CheckBox) gui.getGUINode(SITE_PAGES_HOME_CHECK);
        CheckBox syllabusCheck = (CheckBox) gui.getGUINode(SITE_PAGES_SYLLABUS_CHECK);
        CheckBox scheduleCheck = (CheckBox) gui.getGUINode(SITE_PAGES_SCHEDULE_CHECK);        
        CheckBox hwCheck = (CheckBox) gui.getGUINode(SITE_PAGES_HW_CHECK);
        
         
        
        comboBoxListener(subjectCombo);
        comboBoxListener(numberCombo);
        comboBoxListener(semesterCombo);
        comboBoxListener(yearCombo);
        comboBoxListener(styleCombo);
        comboBoxListener(startTimeCombo);
        comboBoxListener(endTimeCombo);
        
        homeCheck.setOnMouseClicked(e ->{
            controller.processCheckBox(homeCheck, homeCheck.isSelected());
        });
        syllabusCheck.setOnMouseClicked(e ->{
            controller.processCheckBox(syllabusCheck, syllabusCheck.isSelected());
        });
        scheduleCheck.setOnMouseClicked(e ->{
            controller.processCheckBox(scheduleCheck, scheduleCheck.isSelected());
        });
        hwCheck.setOnMouseClicked(e ->{
            controller.processCheckBox(hwCheck, hwCheck.isSelected());
        });
        
        
        ((TextField) gui.getGUINode(SITE_BANNER_TEXT_FIELD)).focusedProperty().addListener(e -> {
            if (bannerTitleTextField.isFocused()){
                oldBannerTitleValue = bannerTitleTextField.getText();               
            }
            if (!bannerTitleTextField.isFocused()) {
                String newValue = bannerTitleTextField.getText();
                controller.processWriteTextField(bannerTitleTextField, oldBannerTitleValue, newValue);
            }
        });                
        ((Button) gui.getGUINode(SITE_STYLE_FAVICON)).setOnAction(e -> {
            controller.processLogoButton(favicon,1);
        });
        ((Button) gui.getGUINode(SITE_STYLE_NAVBAR)).setOnAction(e -> {
            controller.processLogoButton(navbar,2);
        });
        ((Button) gui.getGUINode(SITE_STYLE_LEFT_FOOTER)).setOnAction(e -> {
            controller.processLogoButton(leftFooter,3);
        });
        ((Button) gui.getGUINode(SITE_STYLE_RIGHT_FOOTER)).setOnAction(e -> {
            controller.processLogoButton(rightFooter,4);
        });
        ((TextField) gui.getGUINode(SITE_INSTRUCTOR_NAME_INPUT)).focusedProperty().addListener(e -> {
            if (instructorNameTextField.isFocused()){
                oldInstructorNameValue = instructorNameTextField.getText();               
            }
            if (!instructorNameTextField.isFocused()) {
                String newValue = instructorNameTextField.getText();
                controller.processWriteTextField(instructorNameTextField, oldBannerTitleValue, newValue);
            }
        });   
        ((TextField) gui.getGUINode(SITE_INSTRUCTOR_ROOM_INPUT)).focusedProperty().addListener(e -> {
            if (instructorRoomTextField.isFocused()){
                oldInstructorRoomValue = instructorRoomTextField.getText();               
            }
            if (!instructorRoomTextField.isFocused()) {
                String newValue = instructorRoomTextField.getText();
                controller.processWriteTextField(instructorRoomTextField, oldInstructorRoomValue, newValue);
            }
        });   
        ((TextField) gui.getGUINode(SITE_INSTRUCTOR_EMAIL_INPUT)).focusedProperty().addListener(e -> {
            if (instructorEmailTextField.isFocused()){
                oldInstructorEmailValue = instructorEmailTextField.getText();               
            }
            if (!instructorEmailTextField.isFocused()) {
                String newValue = instructorEmailTextField.getText();
                controller.processWriteTextField(instructorEmailTextField, oldInstructorEmailValue, newValue);
            }
        });   
        ((TextField) gui.getGUINode(SITE_INSTRUCTOR_HP_INPUT)).focusedProperty().addListener(e -> {
            if (instructorHPTextField.isFocused()){
                oldInstructorHPValue = instructorHPTextField.getText();               
            }
            if (!instructorHPTextField.isFocused()) {
                String newValue = instructorHPTextField.getText();
                controller.processWriteTextField(instructorHPTextField, oldInstructorHPValue, newValue);
            }
        });           
        
        ((TextArea) gui.getGUINode(SITE_TEXT_AREA)).focusedProperty().addListener(e -> {
            if (instructorTextArea.isFocused()){
                oldSiteTextValue = instructorTextArea.getText();               
            }
            if (!instructorTextArea.isFocused()) {
                String newValue = instructorTextArea.getText();
                controller.processWriteTextArea(instructorTextArea, oldSiteTextValue, newValue);
            }
        });
        ((TextArea) gui.getGUINode(SYLLABUS_DESCRIPTION_INPUT)).focusedProperty().addListener(e -> {
            if (descriptionTextArea.isFocused()){
                oldDescriptionValue = descriptionTextArea.getText();               
            }
            if (!descriptionTextArea.isFocused()) {
                String newValue = descriptionTextArea.getText();
                controller.processWriteTextArea(descriptionTextArea, oldDescriptionValue, newValue);
            }
        });
        ((TextArea) gui.getGUINode(SYLLABUS_TOPICS_INPUT)).focusedProperty().addListener(e -> {
            if (topicsTextArea.isFocused()){
                oldTopicsValue = topicsTextArea.getText();               
            }
            if (!topicsTextArea.isFocused()) {
                String newValue = topicsTextArea.getText();
                controller.processWriteTextArea(topicsTextArea, oldTopicsValue, newValue);
            }
        });
        ((TextArea) gui.getGUINode(SYLLABUS_PREREQ_INPUT)).focusedProperty().addListener(e -> {
            if (prereqTextArea.isFocused()){
                oldPrereqValue = prereqTextArea.getText();               
            }
            if (!prereqTextArea.isFocused()) {
                String newValue = prereqTextArea.getText();
                controller.processWriteTextArea(prereqTextArea, oldPrereqValue, newValue);
            }
        });
        ((TextArea) gui.getGUINode(SYLLABUS_OUTCOMES_INPUT)).focusedProperty().addListener(e -> {
            if (outcomesTextArea.isFocused()){
                oldOutcomesValue = outcomesTextArea.getText();               
            }
            if (!outcomesTextArea.isFocused()) {
                String newValue = outcomesTextArea.getText();
                controller.processWriteTextArea(outcomesTextArea, oldOutcomesValue, newValue);
            }
        });
        ((TextArea) gui.getGUINode(SYLLABUS_TEXTBOOKS_INPUT)).focusedProperty().addListener(e -> {
            if (textbooksTextArea.isFocused()){
                oldTextbookValue = textbooksTextArea.getText();               
            }
            if (!textbooksTextArea.isFocused()) {
                String newValue = textbooksTextArea.getText();
                controller.processWriteTextArea(textbooksTextArea, oldTextbookValue, newValue);
            }
        });
        ((TextArea) gui.getGUINode(SYLLABUS_GRADED_COMP_INPUT)).focusedProperty().addListener(e -> {
            if (gradedCompTextArea.isFocused()){
                oldGradedCompValue = gradedCompTextArea.getText();               
            }
            if (!gradedCompTextArea.isFocused()) {
                String newValue = gradedCompTextArea.getText();
                controller.processWriteTextArea(gradedCompTextArea, oldGradedCompValue, newValue);
            }
        });
        ((TextArea) gui.getGUINode(SYLLABUS_GRADING_NOTE_INPUT)).focusedProperty().addListener(e -> {
            if (gradingNoteTextArea.isFocused()){
                oldGradingNoteValue = gradingNoteTextArea.getText();               
            }
            if (!gradingNoteTextArea.isFocused()) {
                String newValue = gradingNoteTextArea.getText();
                controller.processWriteTextArea(gradingNoteTextArea, oldGradingNoteValue, newValue);
            }
        });        
        ((TextArea) gui.getGUINode(SYLLABUS_ACADEMIC_DISHONESTY_INPUT)).focusedProperty().addListener(e -> {
            if (academicDisTextArea.isFocused()){
                oldAcademicDisValue = academicDisTextArea.getText();               
            }
            if (!academicDisTextArea.isFocused()) {
                String newValue = academicDisTextArea.getText();
                controller.processWriteTextArea(academicDisTextArea, oldAcademicDisValue, newValue);
            }
        }); 
        ((TextArea) gui.getGUINode(SYLLABUS_SPECIAL_ASSIST_INPUT)).focusedProperty().addListener(e -> {
            if (specialAssistTextArea.isFocused()){
                oldSpecialAssistValue = specialAssistTextArea.getText();               
            }
            if (!specialAssistTextArea.isFocused()) {
                String newValue = specialAssistTextArea.getText();
                controller.processWriteTextArea(specialAssistTextArea, oldSpecialAssistValue, newValue);
            }
        });         
        
        //Meeting Times Tab
        ((Button) gui.getGUINode(MEETING_LECTURES_ADD_BUTTON)).setOnAction(e -> {
            controller.processAddLecture();
        });
        ((Button) gui.getGUINode(MEETING_LECTURES_DELETE_BUTTON)).setOnAction(e -> {
            controller.processRemoveLecture();
        });
        
        ((Button) gui.getGUINode(MEETING_RECITATIONS_ADD_BUTTON)).setOnAction(e -> {
            controller.processAddRecitation();
        });
        ((Button) gui.getGUINode(MEETING_RECITATIONS_DELETE_BUTTON)).setOnAction(e -> {
            controller.processRemoveRecitation();
        });
        ((Button) gui.getGUINode(MEETING_LABS_ADD_BUTTON)).setOnAction(e -> {
            controller.processAddLab();
        });
        ((Button) gui.getGUINode(MEETING_LABS_DELETE_BUTTON)).setOnAction(e -> {
            controller.processRemoveLab();
        });
        
        // FOOLPROOF DESIGN STUFF
        TextField nameTextField = ((TextField) gui.getGUINode(OH_NAME_TEXT_FIELD));
        TextField emailTextField = ((TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD));

        nameTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        emailTextField.textProperty().addListener(e -> {
            controller.processTypeTA();
        });
        
        // FIRE THE ADD EVENT ACTION
        nameTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.processAddTA();
        });
        ((Button) gui.getGUINode(OH_ADD_TA_BUTTON)).setOnAction(e -> {
            controller.processAddTA();
        });
   
        ((Button) gui.getGUINode(OH_TAS_DELETE_BUTTON)).setOnAction(e -> {
            controller.processRemoveTA();
        });
        
        
        ((ComboBox) gui.getGUINode(START_TIME_FILTER)).setOnAction(e -> {
            controller.processTimeFilter();
        });
        
        ((ComboBox) gui.getGUINode(END_TIME_FILTER)).setOnAction(e -> {
            controller.processTimeFilter();
        });
        
        TableView officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.getSelectionModel().setCellSelectionEnabled(true);
        officeHoursTableView.setOnMouseClicked(e -> {
            controller.processToggleOfficeHours();
        });

        // DON'T LET ANYONE SORT THE TABLES
        TableView tasTableView = (TableView) gui.getGUINode(OH_TAS_TABLE_VIEW);
        for (int i = 0; i < officeHoursTableView.getColumns().size(); i++) {
            ((TableColumn) officeHoursTableView.getColumns().get(i)).setSortable(false);
        }
        for (int i = 0; i < tasTableView.getColumns().size(); i++) {
            ((TableColumn) tasTableView.getColumns().get(i)).setSortable(false);
        }

        tasTableView.setOnMouseClicked(e -> {
            app.getFoolproofModule().updateAll();
            if (e.getClickCount() == 2) {
                controller.processEditTA();
            }
            controller.processSelectTA();
        });

        RadioButton allRadio = (RadioButton) gui.getGUINode(OH_ALL_RADIO_BUTTON);
        allRadio.setOnAction(e -> {
            controller.processSelectAllTAs();
        });
        RadioButton gradRadio = (RadioButton) gui.getGUINode(OH_GRAD_RADIO_BUTTON);
        gradRadio.setOnAction(e -> {
            controller.processSelectGradTAs();
        });
        RadioButton undergradRadio = (RadioButton) gui.getGUINode(OH_UNDERGRAD_RADIO_BUTTON);
        undergradRadio.setOnAction(e -> {
            controller.processSelectUndergradTAs();
        });
        
        // FOOL PROOF SCHEDULE ITEM
        DatePicker mondayPicker = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_STARTING_DATE);
        mondayPicker.setOnAction(e -> {
            controller.processLimitEndDate();
        });
        
        DatePicker fridayPicker = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_ENDING_DATE);
        fridayPicker.setOnAction(e -> {
            controller.processLimitStartDate();
        });
        
        // SCHEDULE ITEM BUTTONS
        ((Button) gui.getGUINode(SCHEDULE_ITEMS_DELETE_BUTTON)).setOnAction(e ->{
            controller.processRemoveSchedule();
        });
        
        Button scheduleAddButton = (Button) gui.getGUINode(SCHEDULE_ADD_BUTTON);
        ((Button) gui.getGUINode(SCHEDULE_ADD_BUTTON)).setOnAction(e -> {
            if(scheduleAddButton.getText().equals("Add") || scheduleAddButton.getText().equals("Addoo"))
                controller.processAddSchedule();
            else{
                controller.processEditSchedule();
            }
        });
        
        
        TableView scheduleTableView = (TableView) gui.getGUINode(SCHEDULE_ITEMS_TABLE);
        scheduleTableView.setOnMouseClicked(e -> {
            controller.processScheduleButtonUpdate();
        });
        
    }

    public void initFoolproofDesign() {
        AppGUIModule gui = app.getGUIModule();
        AppFoolproofModule foolproofSettings = app.getFoolproofModule();
        foolproofSettings.registerModeSettings(OH_FOOLPROOF_SETTINGS,
                new CSGFoolproofDesign((CSGApp) app));
    }

    @Override
    public void processWorkspaceKeyEvent(KeyEvent ke) {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }

    @Override
    public void showNewDialog() {
        // WE AREN'T USING THIS FOR THIS APPLICATION
    }
}
