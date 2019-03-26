package oh.data;

import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import djf.modules.AppGUIModule;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.json.JsonArray;
import javax.json.JsonValue;
import oh.CSGApp;
import static oh.CSGPropertyType.EXPORT_DIR_1;
import static oh.CSGPropertyType.EXPORT_DIR_2;
import static oh.CSGPropertyType.MEETING_LABS_TABLE;
import static oh.CSGPropertyType.MEETING_LECTURES_TABLE;
import static oh.CSGPropertyType.MEETING_RECITATIONS_TABLE;
import static oh.CSGPropertyType.OH_ALL_RADIO_BUTTON;
import static oh.CSGPropertyType.OH_GRAD_RADIO_BUTTON;
import static oh.CSGPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static oh.CSGPropertyType.OH_TAS_TABLE_VIEW;
import static oh.CSGPropertyType.SCHEDULE_ITEMS_TABLE;
import static oh.CSGPropertyType.SITE_BANNER_NUMBER_COMBO;
import static oh.CSGPropertyType.SITE_BANNER_SEMESTER_COMBO;
import static oh.CSGPropertyType.SITE_BANNER_SUBJECT_COMBO;
import static oh.CSGPropertyType.SITE_BANNER_TEXT_FIELD;
import static oh.CSGPropertyType.SITE_BANNER_YEAR_COMBO;
import static oh.CSGPropertyType.SITE_INSTRUCTOR_EMAIL_INPUT;
import static oh.CSGPropertyType.SITE_INSTRUCTOR_HP_INPUT;
import static oh.CSGPropertyType.SITE_INSTRUCTOR_NAME_INPUT;
import static oh.CSGPropertyType.SITE_INSTRUCTOR_ROOM_INPUT;
import static oh.CSGPropertyType.SITE_PAGES_HOME_CHECK;
import static oh.CSGPropertyType.SITE_PAGES_HW_CHECK;
import static oh.CSGPropertyType.SITE_PAGES_SCHEDULE_CHECK;
import static oh.CSGPropertyType.SITE_PAGES_SYLLABUS_CHECK;
import static oh.CSGPropertyType.SITE_STYLE_FAVICON_IMAGE;
import static oh.CSGPropertyType.SITE_STYLE_LEFT_FOOTER_IMAGE;
import static oh.CSGPropertyType.SITE_STYLE_NAVBAR_IMAGE;
import static oh.CSGPropertyType.SITE_STYLE_RIGHT_FOOTER_IMAGE;
import static oh.CSGPropertyType.SITE_TEXT_AREA;
import static oh.CSGPropertyType.SYLLABUS_ACADEMIC_DISHONESTY_INPUT;
import static oh.CSGPropertyType.SYLLABUS_DESCRIPTION_INPUT;
import static oh.CSGPropertyType.SYLLABUS_GRADED_COMP_INPUT;
import static oh.CSGPropertyType.SYLLABUS_GRADING_NOTE_INPUT;
import static oh.CSGPropertyType.SYLLABUS_OUTCOMES_INPUT;
import static oh.CSGPropertyType.SYLLABUS_PREREQ_INPUT;
import static oh.CSGPropertyType.SYLLABUS_SPECIAL_ASSIST_INPUT;
import static oh.CSGPropertyType.SYLLABUS_TEXTBOOKS_INPUT;
import static oh.CSGPropertyType.SYLLABUS_TOPICS_INPUT;
import oh.data.TimeSlot.DayOfWeek;
import javafx.scene.control.Label;
import static oh.CSGPropertyType.EXPORT_DIR_3;
import static oh.CSGPropertyType.EXPORT_DIR_4;
import static oh.CSGPropertyType.EXPORT_DIR_5;
import static oh.CSGPropertyType.EXPORT_DIR_6;
import static oh.CSGPropertyType.SCHEDULE_BOUNDARIES_ENDING_DATE;
import static oh.CSGPropertyType.SCHEDULE_BOUNDARIES_STARTING_DATE;

/**
 * This is the data component for TAManagerApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 * 
 * @author Kris Pan
 */
public class CSGData implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CSGApp app;
    
    ObservableList<LectureData> lectures;
    ObservableList<MTrecitationLab> recitations;
    ObservableList<MTrecitationLab> labs;
    
    // THESE ARE ALL THE TEACHING ASSISTANTS
    HashMap<TAType, ArrayList<TeachingAssistantPrototype>> allTAs;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistantPrototype> teachingAssistants;
    ObservableList<TimeSlot> officeHours; 
    ArrayList<TimeSlot> allOfficeHours;

    
    ObservableList<ScheduleData> schedules;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 9;
    public static final int MAX_END_HOUR = 20;
    
    public static String faviconPath;
    public static String navbarPath;
    public static String leftFooterPath;
    public static String rightFooterPath;
    
    public String instructorText = "";
    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     * 
     * @param initApp The application this data manager belongs to. 
     */
    public CSGData(CSGApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        AppGUIModule gui = app.getGUIModule();
                
        
        // SETUP THE DATA STRUCTURES
        TableView<LectureData> lectureTableView = (TableView)gui.getGUINode(MEETING_LECTURES_TABLE);
        lectures = lectureTableView.getItems();        
        TableView<MTrecitationLab> recitationTableView = (TableView)gui.getGUINode(MEETING_RECITATIONS_TABLE);
        recitations = recitationTableView.getItems();
        TableView<MTrecitationLab> labTableView = (TableView)gui.getGUINode(MEETING_LABS_TABLE);
        labs = labTableView.getItems();                              
        
        
        
        
        allTAs = new HashMap();
        allTAs.put(TAType.Graduate, new ArrayList());
        allTAs.put(TAType.Undergraduate, new ArrayList());
        
        allOfficeHours = new ArrayList<TimeSlot>();
        

        // GET THE LIST OF TAs FOR THE LEFT TABLE
        TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        teachingAssistants = taTableView.getItems();

        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        
        resetOfficeHours();
        
        // SCHEDULE TAB
        TableView<ScheduleData> scheduleTableView = (TableView)gui.getGUINode(SCHEDULE_ITEMS_TABLE);
        schedules = scheduleTableView.getItems();        
    }
    
    // ACCESSOR METHODS

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }
    
    // PRIVATE HELPER METHODS
    
    private void sortTAs() {
        Collections.sort(teachingAssistants);
    }
    
    private void resetOfficeHours() {
        //THIS WILL STORE OUR OFFICE HOURS
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView)gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHours = officeHoursTableView.getItems(); 
        officeHours.clear();
        for (int i = startHour; i <= endHour; i++) {
            TimeSlot timeSlot = new TimeSlot(   this.getTimeString(i, true),
                                                this.getTimeString(i, false));
            officeHours.add(timeSlot);
            
            TimeSlot halfTimeSlot = new TimeSlot(   this.getTimeString(i, false),
                                                    this.getTimeString(i+1, true));
            officeHours.add(halfTimeSlot);
        }
    }
    
        public void filterOHTime(int startTime, int endTime) {
        //THIS WILL STORE OUR OFFICE HOURS
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView)gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHours = officeHoursTableView.getItems(); 
        
        Iterator<TimeSlot> tsIterator = officeHours.iterator();
        if(allOfficeHours.isEmpty()){
            while (tsIterator.hasNext()) {
                TimeSlot ts = tsIterator.next();
                allOfficeHours.add(ts);
                }
            }
        else{
            Iterator<TimeSlot> allTSIterator = allOfficeHours.iterator();
            officeHours.clear();
            while (allTSIterator.hasNext()){
                TimeSlot ts = allTSIterator.next();
                officeHours.add(ts);
            }

        }
        officeHours.clear();
        Iterator<TimeSlot> allTSIterator = allOfficeHours.iterator();
            while (allTSIterator.hasNext()){
                TimeSlot ts = allTSIterator.next();
                int initTime = getIntfromTimeString(ts.getStartTime());
                if(initTime >= startTime && initTime < endTime){
                    officeHours.add(ts);
                }
                
            }
            

    }
    
        public int getIntfromTimeString(String endTime){
        int endTimeInt;
        if(endTime.length() ==6){
            endTimeInt = Integer.parseInt(endTime.substring(0, 1));
            if(endTime.contains("pm")){
                endTimeInt += 12;
            }
            else{
            }
        }
        else{
            endTimeInt = Integer.parseInt(endTime.substring(0, 2));
            if(endTime.contains("pm")){
                endTimeInt += 12;
            }
        }
        return endTimeInt;
        }
        
    
    private String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    // METHODS TO OVERRIDE
        
    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void reset() {
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        teachingAssistants.clear();
        
        for (TimeSlot timeSlot : officeHours) {
            timeSlot.reset();
        }
        lectures.clear();
        recitations.clear();
        labs.clear();
        allOfficeHours.clear();
        schedules.clear();
        AppGUIModule gui = app.getGUIModule();

        TextArea instructorTextArea = (TextArea) gui.getGUINode(SITE_TEXT_AREA);
        instructorTextArea.setText("");
        TextField bannerTitleTextField = (TextField) gui.getGUINode(SITE_BANNER_TEXT_FIELD);
        bannerTitleTextField.setText("");
        TextField instructorNameTextField = (TextField) gui.getGUINode(SITE_INSTRUCTOR_NAME_INPUT);
        instructorNameTextField.setText("");
        TextField instructorRoomTextField = (TextField) gui.getGUINode(SITE_INSTRUCTOR_ROOM_INPUT);
        instructorRoomTextField.setText("");
        TextField instructorEmailTextField = (TextField) gui.getGUINode(SITE_INSTRUCTOR_EMAIL_INPUT);
        instructorEmailTextField.setText("");
        TextField instructorHPTextField = (TextField) gui.getGUINode(SITE_INSTRUCTOR_HP_INPUT);
        instructorHPTextField.setText("");
        TextArea descriptionTextArea = (TextArea) gui.getGUINode(SYLLABUS_DESCRIPTION_INPUT);
        descriptionTextArea.setText("");
        TextArea topicsTextArea = (TextArea) gui.getGUINode(SYLLABUS_TOPICS_INPUT);
        topicsTextArea.setText("");
        TextArea prereqTextArea = (TextArea) gui.getGUINode(SYLLABUS_PREREQ_INPUT);
        prereqTextArea.setText("");
        TextArea outcomesTextArea = (TextArea) gui.getGUINode(SYLLABUS_OUTCOMES_INPUT);
        outcomesTextArea.setText("");
        TextArea textbooksTextArea = (TextArea) gui.getGUINode(SYLLABUS_TEXTBOOKS_INPUT);
        textbooksTextArea.setText("");
        TextArea gradedCompTextArea = (TextArea) gui.getGUINode(SYLLABUS_GRADED_COMP_INPUT);
        gradedCompTextArea.setText("");
        TextArea gradingNoteTextArea = (TextArea) gui.getGUINode(SYLLABUS_GRADING_NOTE_INPUT);
        gradingNoteTextArea.setText("");
        TextArea academicDisTextArea = (TextArea) gui.getGUINode(SYLLABUS_ACADEMIC_DISHONESTY_INPUT);
        academicDisTextArea.setText("");
        TextArea specialAssistTextArea = (TextArea) gui.getGUINode(SYLLABUS_SPECIAL_ASSIST_INPUT);
        specialAssistTextArea.setText("");
        CheckBox homeCheck = (CheckBox) gui.getGUINode(SITE_PAGES_HOME_CHECK);
        homeCheck.setSelected(false);
        CheckBox syllabusCheck = (CheckBox) gui.getGUINode(SITE_PAGES_SYLLABUS_CHECK);
        syllabusCheck.setSelected(false);
        CheckBox scheduleCheck = (CheckBox) gui.getGUINode(SITE_PAGES_SCHEDULE_CHECK); 
        scheduleCheck.setSelected(false);
        CheckBox hwCheck = (CheckBox) gui.getGUINode(SITE_PAGES_HW_CHECK);
        hwCheck.setSelected(false);
        DatePicker startDate = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_STARTING_DATE);
        startDate.setValue(null);
        DatePicker endDate = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_ENDING_DATE);
        endDate.setValue(null);
        
    
    }
    
    // SERVICE METHODS
    public void resetLecture(){
        AppGUIModule gui = app.getGUIModule();        
        TableView<LectureData> table = (TableView)gui.getGUINode(MEETING_LECTURES_TABLE);
        table.refresh();
    }    
    public void resetRecitation(){
        AppGUIModule gui = app.getGUIModule();        
        TableView<MTrecitationLab> table = (TableView)gui.getGUINode(MEETING_RECITATIONS_TABLE);
        table.refresh();
    }
    public void resetLab(){
        AppGUIModule gui = app.getGUIModule();        
        TableView<MTrecitationLab> table = (TableView)gui.getGUINode(MEETING_LABS_TABLE);
        table.refresh();
    }    
    
    public void resetSchedule(){
        AppGUIModule gui = app.getGUIModule();        
        TableView<ScheduleData> table = (TableView)gui.getGUINode(SCHEDULE_ITEMS_TABLE);
        table.refresh();
    }    
    
    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if (initStartHour <= initEndHour) {
            // THESE ARE VALID HOURS SO KEEP THEM
            // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
            startHour = initStartHour;
            endHour = initEndHour;
        }
        resetOfficeHours();
    }
    
        public void filterHours(int startTime, int endTime) {
        if (startTime <= endTime) {
            // THESE ARE VALID HOURS SO KEEP THEM
            // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
            startHour = startTime;
            endHour = endTime;
            
            
        }
        resetOfficeHours();
    }

    public void addLecture(LectureData lecture){
        lectures.add(lecture);
    }    
    
    public void removeLecture(LectureData lecture){
        lectures.remove(lecture);
    }    
        
    public void addRecitation(MTrecitationLab rec){
        recitations.add(rec);
    }    
    
    public void removeRecitation(MTrecitationLab rec){
        recitations.remove(rec);
    }
    
    public void addLab(MTrecitationLab rec){
        labs.add(rec);
    }    
    
    public void removeLab(MTrecitationLab rec){
        labs.remove(rec);
    }                
        
    public void addTA(TeachingAssistantPrototype ta) {
        if (!hasTA(ta)) {
            TAType taType = TAType.valueOf(ta.getType());
            ArrayList<TeachingAssistantPrototype> tas = allTAs.get(taType);
            tas.add(ta);
            this.updateTAs();
        }
    }

    public void addTA(TeachingAssistantPrototype ta, HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours) {
        addTA(ta);
        for (TimeSlot timeSlot : officeHours.keySet()) {
            ArrayList<DayOfWeek> days = officeHours.get(timeSlot);
            for (DayOfWeek dow : days) {
                timeSlot.addTA(dow, ta);
            }
        }
    }
    
    public void removeTA(TeachingAssistantPrototype ta) {
        // REMOVE THE TA FROM THE LIST OF TAs
        TAType taType = TAType.valueOf(ta.getType());
        allTAs.get(taType).remove(ta);
        
        // REMOVE THE TA FROM ALL OF THEIR OFFICE HOURS
        for (TimeSlot timeSlot : officeHours) {
            timeSlot.removeTA(ta);
        }
        
        // AND REFRESH THE TABLES
        this.updateTAs();
    }

    public void removeTA(TeachingAssistantPrototype ta, HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours) {
        removeTA(ta);
        for (TimeSlot timeSlot : officeHours.keySet()) {
            ArrayList<DayOfWeek> days = officeHours.get(timeSlot);
            for (DayOfWeek dow : days) {
                timeSlot.removeTA(dow, ta);
            }
        }    
    }
    
    public void addSchedule(ScheduleData s){
        schedules.add(s);
    }
    
    public void removeSchedule(ScheduleData s){
        schedules.remove(s);
    }
    
    public DayOfWeek getColumnDayOfWeek(int columnNumber) {
        return TimeSlot.DayOfWeek.values()[columnNumber-2];
    }

    public TeachingAssistantPrototype getTAWithName(String name) {
        Iterator<TeachingAssistantPrototype> taIterator = teachingAssistants.iterator();
        while (taIterator.hasNext()) {
            TeachingAssistantPrototype ta = taIterator.next();
            if (ta.getName().equals(name))
                return ta;
        }
        return null;
    }

    public TeachingAssistantPrototype getTAWithEmail(String email) {
        Iterator<TeachingAssistantPrototype> taIterator = teachingAssistants.iterator();
        while (taIterator.hasNext()) {
            TeachingAssistantPrototype ta = taIterator.next();
            if (ta.getEmail().equals(email))
                return ta;
        }
        return null;
    }

    public TimeSlot getTimeSlot(String startTime) {
        Iterator<TimeSlot> timeSlotsIterator = officeHours.iterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            String timeSlotStartTime = timeSlot.getStartTime().replace(":", "_");
            if (timeSlotStartTime.equals(startTime))
                return timeSlot;
        }
        return null;
    }

    public TAType getSelectedType() {
        RadioButton allRadio = (RadioButton)app.getGUIModule().getGUINode(OH_ALL_RADIO_BUTTON);
        if (allRadio.isSelected())
            return TAType.All;
        RadioButton gradRadio = (RadioButton)app.getGUIModule().getGUINode(OH_GRAD_RADIO_BUTTON);
        if (gradRadio.isSelected())
            return TAType.Graduate;
        else
            return TAType.Undergraduate;
    }

    public TeachingAssistantPrototype getSelectedTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TeachingAssistantPrototype> tasTable = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        return tasTable.getSelectionModel().getSelectedItem();
    }

    public LectureData getSelectedLecture(){
        AppGUIModule gui = app.getGUIModule();
        TableView<LectureData> lecTable = (TableView)gui.getGUINode(MEETING_LECTURES_TABLE);
        return lecTable.getSelectionModel().getSelectedItem();
    }
    
    public MTrecitationLab getSelectedRecitation(){
        AppGUIModule gui = app.getGUIModule();
        TableView<MTrecitationLab> recitTable = (TableView)gui.getGUINode(MEETING_RECITATIONS_TABLE);
        return recitTable.getSelectionModel().getSelectedItem();
    }
    
    public MTrecitationLab getSelectedLab(){
        AppGUIModule gui = app.getGUIModule();
        TableView<MTrecitationLab> labTable = (TableView)gui.getGUINode(MEETING_LABS_TABLE);
        return labTable.getSelectionModel().getSelectedItem();
    }
       
    public HashMap<TimeSlot, ArrayList<DayOfWeek>> getTATimeSlots(TeachingAssistantPrototype ta) {
        HashMap<TimeSlot, ArrayList<DayOfWeek>> timeSlots = new HashMap();
        for (TimeSlot timeSlot : officeHours) {
            if (timeSlot.hasTA(ta)) {
                ArrayList<DayOfWeek> daysForTA = timeSlot.getDaysForTA(ta);
                timeSlots.put(timeSlot, daysForTA);
            }
        }
        return timeSlots;
    }
    
    public ScheduleData getSelectedSchedule(){
        AppGUIModule gui = app.getGUIModule();
        TableView<ScheduleData> table = (TableView)gui.getGUINode(SCHEDULE_ITEMS_TABLE);
        return table.getSelectionModel().getSelectedItem();
    }
    
    private boolean hasTA(TeachingAssistantPrototype testTA) {
        return allTAs.get(TAType.Graduate).contains(testTA)
                ||
                allTAs.get(TAType.Undergraduate).contains(testTA);
    }
    
    public boolean isLectureSelected() {
        AppGUIModule gui = app.getGUIModule();
        TableView table = (TableView)gui.getGUINode(MEETING_LECTURES_TABLE);
        return table.getSelectionModel().getSelectedItem() != null;
    }
    
    public boolean isRecitationSelected() {
        AppGUIModule gui = app.getGUIModule();
        TableView table = (TableView)gui.getGUINode(MEETING_RECITATIONS_TABLE);
        return table.getSelectionModel().getSelectedItem() != null;
    }
        
    public boolean isLabSelected() {
        AppGUIModule gui = app.getGUIModule();
        TableView table = (TableView)gui.getGUINode(MEETING_LABS_TABLE);
        return table.getSelectionModel().getSelectedItem() != null;
    }    
    
    public boolean isTASelected() {
        AppGUIModule gui = app.getGUIModule();
        TableView tasTable = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
        return tasTable.getSelectionModel().getSelectedItem() != null;
    }

    public boolean isScheduleSelected() {
        AppGUIModule gui = app.getGUIModule();
        TableView table = (TableView)gui.getGUINode(SCHEDULE_ITEMS_TABLE);
        return table.getSelectionModel().getSelectedItem() != null;
    }    
    
    

    public boolean isLegalNewTA(String name, String email) {
        if ((name.trim().length() > 0)
                && (email.trim().length() > 0)) {
            // MAKE SURE NO TA ALREADY HAS THE SAME NAME
            TAType type = this.getSelectedType();
            TeachingAssistantPrototype testTA = new TeachingAssistantPrototype(name, email, type);
            if (this.teachingAssistants.contains(testTA))
                return false;
            if (this.isLegalNewEmail(email)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isLegalNewName(String testName) {
        if (testName.trim().length() > 0) {
            for (TeachingAssistantPrototype testTA : this.teachingAssistants) {
                if (testTA.getName().equals(testName))
                    return false;
            }
            return true;
        }
        return false;
    }
    
    public boolean isLegalNewEmail(String email) {
        Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile(
                "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if (matcher.find()) {
            for (TeachingAssistantPrototype ta : this.teachingAssistants) {
                if (ta.getEmail().equals(email.trim()))
                    return false;
            }
            return true;
        }
        else return false;
    }
    
    public boolean isDayOfWeekColumn(int columnNumber) {
        return columnNumber >= 2;
    }
    
    public boolean isTATypeSelected() {
        AppGUIModule gui = app.getGUIModule();
        RadioButton allRadioButton = (RadioButton)gui.getGUINode(OH_ALL_RADIO_BUTTON);
        return !allRadioButton.isSelected();
    }
    
    public boolean isValidTAEdit(TeachingAssistantPrototype taToEdit, String name, String email) {
        if (!taToEdit.getName().equals(name)) {
            if (!this.isLegalNewName(name))
                return false;
        }
        if (!taToEdit.getEmail().equals(email)) {
            if (!this.isLegalNewEmail(email))
                return false;
        }
        return true;
    }

    public boolean isValidNameEdit(TeachingAssistantPrototype taToEdit, String name) {
        if (!taToEdit.getName().equals(name)) {
            if (!this.isLegalNewName(name))
                return false;
        }
        return true;
    }

    public boolean isValidEmailEdit(TeachingAssistantPrototype taToEdit, String email) {
        if (!taToEdit.getEmail().equals(email)) {
            if (!this.isLegalNewEmail(email))
                return false;
        }
        return true;
    }    

    public void updateTAs() {
        TAType type = getSelectedType();
        selectTAs(type);
    }
    
    public void selectTAs(TAType type) {
        teachingAssistants.clear();
        Iterator<TeachingAssistantPrototype> tasIt = this.teachingAssistantsIterator();
        while (tasIt.hasNext()) {
            TeachingAssistantPrototype ta = tasIt.next();
            if (type.equals(TAType.All)) {
                teachingAssistants.add(ta);
            }
            else if (ta.getType().equals(type.toString())) {
                teachingAssistants.add(ta);
            }
        }
        
        // SORT THEM BY NAME
        sortTAs();

        // CLEAR ALL THE OFFICE HOURS
        Iterator<TimeSlot> officeHoursIt = officeHours.iterator();
        while (officeHoursIt.hasNext()) {
            TimeSlot timeSlot = officeHoursIt.next();
            timeSlot.filter(type);
        }
        
        app.getFoolproofModule().updateAll();
    }
    
    public Iterator<TimeSlot> officeHoursIterator() {
        return officeHours.iterator();
    }

    public Iterator<TeachingAssistantPrototype> teachingAssistantsIterator() {
        return new AllTAsIterator();
    }
    
    public Iterator<LectureData> lecturesIterator() {
        return lectures.iterator();
    }

    public Iterator<MTrecitationLab> recitationsIterator() {
        return recitations.iterator();
    }
    
    public Iterator<MTrecitationLab> labsIterator() {
        return labs.iterator();
    }
    
    public Iterator<ScheduleData> schedulesIterator() {
        return schedules.iterator();
    }
    
    private class AllTAsIterator implements Iterator {
        Iterator gradIt = allTAs.get(TAType.Graduate).iterator();
        Iterator undergradIt = allTAs.get(TAType.Undergraduate).iterator();

        public AllTAsIterator() {}
        
        @Override
        public boolean hasNext() {
            if (gradIt.hasNext() || undergradIt.hasNext())
                return true;
            else
                return false;                
        }

        @Override
        public Object next() {
            if (gradIt.hasNext())
                return gradIt.next();
            else if (undergradIt.hasNext())
                return undergradIt.next();
            else
                return null;
        }
    }
    
    public void loadBanner(String subjectText, String numberText, 
        String semesterText, String yearText, String bannerTitle){
        ComboBox subject = (ComboBox) app.getGUIModule().getGUINode(SITE_BANNER_SUBJECT_COMBO);
        subject.getSelectionModel().select(subjectText);
        if(!subject.getItems().contains(subjectText)){
        subject.getItems().add(subjectText);
        }
        ComboBox number = (ComboBox) app.getGUIModule().getGUINode(SITE_BANNER_NUMBER_COMBO);
        number.getSelectionModel().select(numberText);
        if(!number.getItems().contains(numberText)){
        number.getItems().add(numberText);
        }
        ComboBox semester = (ComboBox) app.getGUIModule().getGUINode(SITE_BANNER_SEMESTER_COMBO);
        semester.getSelectionModel().select(semesterText);
        if(!semester.getItems().contains(semesterText)){
        semester.getItems().add(semesterText);
        }
        ComboBox year = (ComboBox) app.getGUIModule().getGUINode(SITE_BANNER_YEAR_COMBO);
        year.getSelectionModel().select(yearText);
        if(!year.getItems().contains(yearText)){
        year.getItems().add(yearText);
        }
        TextField title = (TextField) app.getGUIModule().getGUINode(SITE_BANNER_TEXT_FIELD);
        title.setText(bannerTitle);
        
    }
    public String getSiteSubject(){
        ComboBox subject = (ComboBox) app.getGUIModule().getGUINode(SITE_BANNER_SUBJECT_COMBO);
        return subject.getValue().toString();
    }
    public String getSiteNumber(){
        ComboBox number = (ComboBox) app.getGUIModule().getGUINode(SITE_BANNER_NUMBER_COMBO);
        return number.getValue().toString();
    }
    public String getSiteSemester(){
        ComboBox semester = (ComboBox) app.getGUIModule().getGUINode(SITE_BANNER_SEMESTER_COMBO);
        return semester.getValue().toString();
    }
    public String getSiteYear(){
        ComboBox year = (ComboBox) app.getGUIModule().getGUINode(SITE_BANNER_YEAR_COMBO);
        return year.getValue().toString();
    }
    public String getSiteTitle(){
        TextField title = (TextField) app.getGUIModule().getGUINode(SITE_BANNER_TEXT_FIELD);
        return title.getText();
    }      
    
    public String getFaviconPath(){
           return faviconPath;
    }
    public String getNavbarPath(){
           return navbarPath;
    }
    public String getLeftFooterPath(){
           return leftFooterPath;
    }
    public String getRightFooterPath(){
           return rightFooterPath;
    }
    
    public String getInstructorName(){
        TextField text = (TextField) app.getGUIModule().getGUINode(SITE_INSTRUCTOR_NAME_INPUT);
        return text.getText();
    }
    public String getInstructorRoom(){
        TextField text = (TextField) app.getGUIModule().getGUINode(SITE_INSTRUCTOR_ROOM_INPUT);
        return text.getText();
    }
    public String getInstructorEmail(){
        TextField text = (TextField) app.getGUIModule().getGUINode(SITE_INSTRUCTOR_EMAIL_INPUT);
        return text.getText();
    }
    public String getInstructorHP(){
        TextField text = (TextField) app.getGUIModule().getGUINode(SITE_INSTRUCTOR_HP_INPUT);
        return text.getText();
    }
    
    public String getInstructorOH(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SITE_TEXT_AREA);
        return text.getText();
    }
    
    public void loadInstructor(String nameText, String roomText, String emailText, String hpText,
            String hoursString){
        TextField name = (TextField) app.getGUIModule().getGUINode(SITE_INSTRUCTOR_NAME_INPUT);
        name.setText(nameText);
        TextField room = (TextField) app.getGUIModule().getGUINode(SITE_INSTRUCTOR_ROOM_INPUT);
        room.setText(roomText);
        TextField email = (TextField) app.getGUIModule().getGUINode(SITE_INSTRUCTOR_EMAIL_INPUT);
        email.setText(emailText);
        TextField hp = (TextField) app.getGUIModule().getGUINode(SITE_INSTRUCTOR_HP_INPUT);
        hp.setText(hpText);        
        TextArea hours = (TextArea) app.getGUIModule().getGUINode(SITE_TEXT_AREA);
        hours.setText(hoursString);

    }
    
    public void loadLogos(String faviconJson, String navbarJson,
        String leftFooterJson, String rightFooterJson){
        ImageView favicon = (ImageView) app.getGUIModule().getGUINode(SITE_STYLE_FAVICON_IMAGE);        
        Image favImage = new Image("file:"+faviconJson);
        favicon.setImage(favImage);
        ImageView navbar = (ImageView) app.getGUIModule().getGUINode(SITE_STYLE_NAVBAR_IMAGE);        
        Image navbarImage = new Image("file:"+navbarJson);
        navbar.setImage(navbarImage);
        ImageView leftFooter = (ImageView) app.getGUIModule().getGUINode(SITE_STYLE_LEFT_FOOTER_IMAGE);        
        Image leftImage = new Image("file:"+leftFooterJson);
        leftFooter.setImage(leftImage);
        ImageView rightFooter = (ImageView) app.getGUIModule().getGUINode(SITE_STYLE_RIGHT_FOOTER_IMAGE);        
        Image rightImage = new Image("file:"+rightFooterJson);
        rightFooter.setImage(rightImage);
    }
    public void setTextArea(TextArea text, String value){
        text.textProperty().set(value);
    }
    
    public void setTextField(TextField text, String value){
        text.setText(value);
    }
    
    public Boolean homeCheckBoxChecker(){
        CheckBox home = (CheckBox) app.getGUIModule().getGUINode(SITE_PAGES_HOME_CHECK);        
        return home.isSelected();
    }
    public Boolean syllabusCheckBoxChecker(){
        CheckBox syllabus = (CheckBox) app.getGUIModule().getGUINode(SITE_PAGES_SYLLABUS_CHECK);
        return syllabus.isSelected();
    }
    public Boolean scheduleCheckBoxChecker(){
        CheckBox schedule = (CheckBox) app.getGUIModule().getGUINode(SITE_PAGES_SCHEDULE_CHECK);        
        return schedule.isSelected();
    }
    public Boolean HWCheckBoxChecker(){
        CheckBox hw = (CheckBox) app.getGUIModule().getGUINode(SITE_PAGES_HW_CHECK);        
        return hw.isSelected();
    }    
    
    public void loadCheckBox(Boolean homeB, Boolean syllabusB, Boolean scheduleB, Boolean HWB){
        CheckBox home = (CheckBox) app.getGUIModule().getGUINode(SITE_PAGES_HOME_CHECK);  
        home.setSelected(homeB);
        CheckBox syllabus = (CheckBox) app.getGUIModule().getGUINode(SITE_PAGES_SYLLABUS_CHECK);
        syllabus.setSelected(syllabusB);
        CheckBox schedule = (CheckBox) app.getGUIModule().getGUINode(SITE_PAGES_SCHEDULE_CHECK);        
        schedule.setSelected(scheduleB);
        CheckBox hw = (CheckBox) app.getGUIModule().getGUINode(SITE_PAGES_HW_CHECK);        
        hw.setSelected(HWB);
    }
    public String getSyllabusDescription(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_DESCRIPTION_INPUT);   
        return text.getText();
    }
    public String getSyllabusTopics(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_TOPICS_INPUT);   
        return text.getText();
    }
    public String getSyllabusPrereq(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_PREREQ_INPUT);   
        return text.getText();
    }
    public String getSyllabusOutcomes(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_OUTCOMES_INPUT);   
        return text.getText();
    }
    public String getSyllabusTextBooks(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_TEXTBOOKS_INPUT);   
        return text.getText();
    }
    public String getSyllabusGradedComp(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_GRADED_COMP_INPUT);   
        return text.getText();
    }
    public String getSyllabusGradingNote(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_GRADING_NOTE_INPUT);   
        return text.getText();
    }
    public String getSyllabusAcademicDis(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_ACADEMIC_DISHONESTY_INPUT);   
        return text.getText();
    }
    public String getSyllabusSpecialAssist(){
        TextArea text = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_SPECIAL_ASSIST_INPUT);   
        return text.getText();
    }    
    public void loadSyllabus(String a, String b, String c, String d, String e, String f,
            String g, String h, String i){
        TextArea description = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_DESCRIPTION_INPUT);
        description.setText(a);
        TextArea topics = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_TOPICS_INPUT);
        topics.setText(b);
        TextArea prereq = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_PREREQ_INPUT);
        prereq.setText(c);
        TextArea outcomes = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_OUTCOMES_INPUT);
        outcomes.setText(d);
        TextArea textbooks = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_TEXTBOOKS_INPUT);
        textbooks.setText(e);
        TextArea gradedComp = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_GRADED_COMP_INPUT);
        gradedComp.setText(f);
        TextArea gradingNote = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_GRADING_NOTE_INPUT);
        gradingNote.setText(g);
        TextArea Academic = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_ACADEMIC_DISHONESTY_INPUT);
        Academic.setText(h); 
        TextArea special = (TextArea) app.getGUIModule().getGUINode(SYLLABUS_SPECIAL_ASSIST_INPUT);
        special.setText(i);        
    }
            
    public String getExportDirText(){
        Label text1 = (Label) app.getGUIModule().getGUINode(EXPORT_DIR_1);
        Label text2 = (Label) app.getGUIModule().getGUINode(EXPORT_DIR_2);
        Label text3 = (Label) app.getGUIModule().getGUINode(EXPORT_DIR_3);
        Label text4 = (Label) app.getGUIModule().getGUINode(EXPORT_DIR_4);
        Label text5 = (Label) app.getGUIModule().getGUINode(EXPORT_DIR_5);
        Label text6 = (Label) app.getGUIModule().getGUINode(EXPORT_DIR_6);
        String dirText = text1.getText() + text2.getText() + text3.getText() +
                text4.getText() + text5.getText() + text6.getText();
        return dirText;
    }
    
  
}