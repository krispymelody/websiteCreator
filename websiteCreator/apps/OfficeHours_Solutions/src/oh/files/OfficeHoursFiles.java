package oh.files;

import static djf.AppPropertyType.APP_PATH_EXPORT;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.modules.AppGUIModule;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.swing.JOptionPane;
import oh.CSGApp;
import static oh.CSGPropertyType.SCHEDULE_BOUNDARIES_ENDING_DATE;
import static oh.CSGPropertyType.SCHEDULE_BOUNDARIES_STARTING_DATE;
import static oh.CSGPropertyType.SITE_STYLE_COMBO;
import static oh.CSGPropertyType.START_TIME_FILTER;
import oh.data.LectureData;
import oh.data.MTrecitationLab;
import oh.data.CSGData;
import oh.data.ScheduleData;
import oh.data.TAType;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;
import org.apache.commons.io.FileUtils;
import properties_manager.PropertiesManager;


/**
 * This class serves as the file component for the TA
 * manager app. It provides all saving and loading 
 * services for the application.
 * 
 * @author Kris Pan
 */
public class OfficeHoursFiles implements AppFileComponent {
    // THIS IS THE APP ITSELF
    CSGApp app;
    
    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_GRAD_TAS = "grad_tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TYPE = "type";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_START_TIME = "time";
    static final String JSON_DAY_OF_WEEK = "day";
    static final String JSON_MONDAY = "monday";
    static final String JSON_TUESDAY = "tuesday";
    static final String JSON_WEDNESDAY = "wednesday";
    static final String JSON_THURSDAY = "thursday";
    static final String JSON_FRIDAY = "friday";
    static final String JSON_SITE_SUBJECT = "subject"; 
    static final String JSON_SITE_NUMBER = "number";
    static final String JSON_SITE_SEMESTER = "semester";
    static final String JSON_SITE_YEAR = "year";
    static final String JSON_SITE_TITLE = "title";
    static final String JSON_SITE_LOGOS = "logos";
    static final String JSON_SITE_HREF = "href";    
    static final String JSON_SITE_PATH = "src";
    static final String JSON_SITE_FAVICON = "favicon";
    static final String JSON_SITE_NAVBAR = "navbar";
    static final String JSON_SITE_BOTTOM_LEFT = "bottom_left";
    static final String JSON_SITE_BOTTOM_RIGHT = "bottom_right";
    static final String JSON_ROOM = "room";
    static final String JSON_HOME_PAGE = "home_page";
    static final String JSON_HOURS = "hours";
    static final String JSON_INSTRUCTOR = "instructor";
    static final String JSON_LINK = "link";
    static final String JSON_PAGES = "pages";
    static final String JSON_DESCRIPTION = "description";
    static final String JSON_TOPICS = "topics";
    static final String JSON_PREREQ = "prerequisites";
    static final String JSON_OUTCOMES = "outcomes";
    static final String JSON_TEXTBOOKS = "textbooks";
    static final String JSON_GRADED_COMP = "gradedComp";
    static final String JSON_GRADING_NOTE = "gradingNote";
    static final String JSON_ACADEMIC_DIS = "academicDishonesty";
    static final String JSON_SPECIAL_ASSIST = "specialAssistance";
    static final String JSON_SYLLABUS = "syllabus";
    static final String JSON_SECTION = "section";
    static final String JSON_DAYS = "days";
    static final String JSON_TIME = "time";
    static final String JSON_DAYTIME = "day_time";
    static final String JSON_LOCATION = "location";    
    static final String JSON_TA1 = "ta_1";
    static final String JSON_TA2 = "ta_2";
    static final String JSON_LECTURES = "lectures";
    static final String JSON_RECITATIONS = "recitations";
    static final String JSON_LABS = "labs";
    static final String JSON_SITE_TAB = "site_tab";
    static final String JSON_SYLLABUS_TAB = "syllabus_tab";
    static final String JSON_MT_TAB = "mt_tab";
    static final String JSON_OH_TAB = "oh_tab";
    static final String JSON_SCHEDULE_TAB = "schedule_tab";
    static final String JSON_MONTH = "month";
    static final String JSON_STARTING_MONDAY_MONTH = "startingMondayMonth";
    static final String JSON_STARTING_MONDAY_DAY = "startingMondayDay";
    static final String JSON_ENDING_FRIDAY_MONTH = "endingFridayMonth";        
    static final String JSON_ENDING_FRIDAY_DAY = "endingFridayDay";
    static final String JSON_DAY = "day";
    static final String JSON_TITLE = "title";
    static final String JSON_HOLIDAYS = "holidays";
    static final String JSON_REFERENCES = "references";
    static final String JSON_HWS = "hws";
    static final String JSON_TOPIC = "topic";
    static final String JSON_PHOTO = "photo";

    public OfficeHoursFiles(CSGApp initApp) {
        app = initApp;
    }
    
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
	// CLEAR THE OLD DATA OUT
        try{
	CSGData dataManager = (CSGData)data;
        dataManager.reset();

	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);

        // LOAD SITE TAB
	// LOAD BANNER PANE
	String subjectCombo = json.getJsonObject(JSON_SITE_TAB).getString(JSON_SITE_SUBJECT);
        String numberCombo = json.getJsonObject(JSON_SITE_TAB).getString(JSON_SITE_NUMBER);
        String semesterCombo = json.getJsonObject(JSON_SITE_TAB).getString(JSON_SITE_SEMESTER);
	String yearCombo = json.getJsonObject(JSON_SITE_TAB).getString(JSON_SITE_YEAR);
	String bannerTitle = json.getJsonObject(JSON_SITE_TAB).getString(JSON_SITE_TITLE);
        dataManager.loadBanner(subjectCombo,numberCombo,semesterCombo,yearCombo,bannerTitle);
        // LOAD LOGOS
        String faviconJsonPath = json.getJsonObject(JSON_SITE_TAB).getJsonObject(JSON_SITE_LOGOS).
                getJsonObject(JSON_SITE_FAVICON).getString(JSON_SITE_HREF);
        String navbarJsonPath = json.getJsonObject(JSON_SITE_TAB).getJsonObject(JSON_SITE_LOGOS).
                getJsonObject(JSON_SITE_NAVBAR).getString(JSON_SITE_PATH);
        String leftFooterJsonPath = json.getJsonObject(JSON_SITE_TAB).getJsonObject(JSON_SITE_LOGOS).
                getJsonObject(JSON_SITE_BOTTOM_LEFT).getString(JSON_SITE_PATH);
        String rightFooterJsonPath = json.getJsonObject(JSON_SITE_TAB).getJsonObject(JSON_SITE_LOGOS).
                getJsonObject(JSON_SITE_BOTTOM_RIGHT).getString(JSON_SITE_PATH);
      
        dataManager.loadLogos(faviconJsonPath,navbarJsonPath,
                leftFooterJsonPath,rightFooterJsonPath);
        // LOAD INSTRUCTOR
        String instructorName = json.getJsonObject(JSON_SITE_TAB).getJsonObject(JSON_INSTRUCTOR).
               getString(JSON_NAME);  
        String instructorRoom = json.getJsonObject(JSON_SITE_TAB).getJsonObject(JSON_INSTRUCTOR).
               getString(JSON_ROOM);
        String instructorEmail = json.getJsonObject(JSON_SITE_TAB).getJsonObject(JSON_INSTRUCTOR).
               getString(JSON_EMAIL);
        String instructorHP = json.getJsonObject(JSON_SITE_TAB).getJsonObject(JSON_INSTRUCTOR).
               getString(JSON_HOME_PAGE);
        

        JsonArray instructorHours = json.getJsonObject(JSON_SITE_TAB).getJsonObject(JSON_INSTRUCTOR).
                getJsonArray(JSON_HOURS);
        String instructorHoursString = instructorHours.toString();
        dataManager.loadInstructor(instructorName,instructorRoom,instructorEmail,
                instructorHP,instructorHoursString);
        
        // LOAD CHECKBOXES
        Boolean homeB = false;
        Boolean syllabusB = false;
        Boolean scheduleB = false;
        Boolean hwB = false;
        JsonArray checkBoxArray = json.getJsonObject(JSON_SITE_TAB).getJsonArray(JSON_PAGES);
        JsonObject a;
        for (int i = 0; i < checkBoxArray.size(); i++)
        {   
            a = checkBoxArray.getJsonObject(i);
            if(a.getString(JSON_NAME).equals("Home"))
            {
                homeB = true;
            }
            if(a.getString(JSON_NAME).equals("Syllabus"))
            {
                syllabusB = true;
            }
            if(a.getString(JSON_NAME).equals("Schedule"))
            {
                scheduleB = true;
            }
            if(a.getString(JSON_NAME).equals("HWs"))
            {
                hwB = true;
            }            
        }
        
        

        dataManager.loadCheckBox(homeB, syllabusB, scheduleB, hwB);

        // LOAD SYLLABUS TAB
        String description = json.getJsonObject(JSON_SYLLABUS_TAB)
                .getJsonObject(JSON_SYLLABUS).getString(JSON_DESCRIPTION);
        String topics = json.getJsonObject(JSON_SYLLABUS_TAB)
                .getJsonObject(JSON_SYLLABUS).getJsonArray(JSON_TOPICS).toString();
        String prereq = json.getJsonObject(JSON_SYLLABUS_TAB)
                .getJsonObject(JSON_SYLLABUS).getString(JSON_PREREQ);
        String outcomes = json.getJsonObject(JSON_SYLLABUS_TAB)
                .getJsonObject(JSON_SYLLABUS).getJsonArray(JSON_OUTCOMES).toString();
        String textbook = json.getJsonObject(JSON_SYLLABUS_TAB)
                .getJsonObject(JSON_SYLLABUS).getJsonArray(JSON_TEXTBOOKS).toString();
        String gradedComp = json.getJsonObject(JSON_SYLLABUS_TAB)
                .getJsonObject(JSON_SYLLABUS).getJsonArray(JSON_GRADED_COMP).toString();
        String gradingNote = json.getJsonObject(JSON_SYLLABUS_TAB)
                .getJsonObject(JSON_SYLLABUS).getString(JSON_GRADING_NOTE);
        String academic = json.getJsonObject(JSON_SYLLABUS_TAB)
                .getJsonObject(JSON_SYLLABUS).getString(JSON_ACADEMIC_DIS);
        String special = json.getJsonObject(JSON_SYLLABUS_TAB)
                .getJsonObject(JSON_SYLLABUS).getString(JSON_SPECIAL_ASSIST);
        
        dataManager.loadSyllabus(description, topics, prereq, outcomes, textbook, 
                gradedComp, gradingNote, academic, special);
        
        // LOAD MEETING TIMES
        JsonArray jsonLecturesArray = json.getJsonObject(JSON_MT_TAB).getJsonArray(JSON_LECTURES);
        for (int j = 0; j < jsonLecturesArray.size(); j++) {
            JsonObject jsonLecture = jsonLecturesArray.getJsonObject(j);
            String section = jsonLecture.getString(JSON_SECTION);
            String days = jsonLecture.getString(JSON_DAYS);
            String time = jsonLecture.getString(JSON_TIME);
            String room = jsonLecture.getString(JSON_ROOM);
            LectureData lecture = new LectureData(section, days, time, room);
            dataManager.addLecture(lecture);
            }
        JsonArray jsonLabsArray = json.getJsonObject(JSON_MT_TAB).getJsonArray(JSON_LABS);
        for (int k = 0; k < jsonLabsArray.size(); k++) {
            JsonObject jsonLab = jsonLabsArray.getJsonObject(k);
            String section = jsonLab.getString(JSON_SECTION);
            String dayTime = jsonLab.getString(JSON_DAYTIME);
            String location = jsonLab.getString(JSON_LOCATION);
            String ta1 = jsonLab.getString(JSON_TA1);
            String ta2 = jsonLab.getString(JSON_TA2);
            MTrecitationLab lab = new MTrecitationLab(section, dayTime, location, ta1, ta2);
            dataManager.addLab(lab);
            }
        
        JsonArray jsonRecitationsArray = json.getJsonObject(JSON_MT_TAB).getJsonArray(JSON_RECITATIONS);
        for (int l = 0; l < jsonRecitationsArray.size(); l++) {
            JsonObject jsonRecitation = jsonRecitationsArray.getJsonObject(l);
            String section = jsonRecitation.getString(JSON_SECTION);
            String dayTime = jsonRecitation.getString(JSON_DAYTIME);
            String location = jsonRecitation.getString(JSON_LOCATION);
            String ta1 = jsonRecitation.getString(JSON_TA1);
            String ta2 = jsonRecitation.getString(JSON_TA2);
            MTrecitationLab rec = new MTrecitationLab(section, dayTime, location, ta1, ta2);
            dataManager.addRecitation(rec);
            }
        
        
        // LOAD THE START AND END HOURS
        String startHour = json.getJsonObject(JSON_OH_TAB).getString(JSON_START_HOUR);
        String endHour = json.getJsonObject(JSON_OH_TAB).getString(JSON_END_HOUR);
        dataManager.initHours(startHour, endHour);
        
        // LOAD ALL THE GRAD TAs
        loadTAs(dataManager, json, JSON_GRAD_TAS);
        loadTAs(dataManager, json, JSON_UNDERGRAD_TAS);

        
        
        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = json.getJsonObject(JSON_OH_TAB).getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String startTime = jsonOfficeHours.getString(JSON_START_TIME);
            DayOfWeek dow = DayOfWeek.valueOf(jsonOfficeHours.getString(JSON_DAY_OF_WEEK));
            String name = jsonOfficeHours.getString(JSON_NAME);
            TeachingAssistantPrototype ta = dataManager.getTAWithName(name);
            TimeSlot timeSlot = dataManager.getTimeSlot(startTime);
            timeSlot.toggleTA(dow, ta);
            }
        
        // LOAD SCHEDULE TAB 
        String startMonth = json.getJsonObject(JSON_SCHEDULE_TAB).getString(JSON_STARTING_MONDAY_MONTH);
        String startDay = json.getJsonObject(JSON_SCHEDULE_TAB).getString(JSON_STARTING_MONDAY_DAY);
        String endMonth = json.getJsonObject(JSON_SCHEDULE_TAB).getString(JSON_ENDING_FRIDAY_MONTH);
        String endDay = json.getJsonObject(JSON_SCHEDULE_TAB).getString(JSON_ENDING_FRIDAY_DAY);
        
        LocalDate startDate = LocalDate.of(2018, Integer.parseInt(startMonth), Integer.parseInt(startDay));
        LocalDate endDate = LocalDate.of(2018, Integer.parseInt(endMonth), Integer.parseInt(endDay));
        
        AppGUIModule gui = app.getGUIModule();        
        DatePicker startDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_STARTING_DATE);
        DatePicker endDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_ENDING_DATE);
        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);
        
        JsonArray scheduleHolidayArray = json.getJsonObject(JSON_SCHEDULE_TAB).getJsonArray(JSON_HOLIDAYS);
        for (int m = 0; m < scheduleHolidayArray.size(); m++) {
            JsonObject object = scheduleHolidayArray.getJsonObject(m);
            String month = object.getString(JSON_MONTH);
            String day = object.getString(JSON_DAY);
            String title = object.getString(JSON_TITLE);
            String link = object.getString(JSON_LINK);
            LocalDate date = LocalDate.of(2018, Integer.parseInt(month), Integer.parseInt(day));
            ScheduleData newData = new ScheduleData("Holiday",date.toString(),title,"",link);
            dataManager.addSchedule(newData);
            }
        
        JsonArray scheduleLectureArray = json.getJsonObject(JSON_SCHEDULE_TAB).getJsonArray(JSON_LECTURES);
        for (int m = 0; m < scheduleLectureArray.size(); m++) {
            JsonObject object = scheduleLectureArray.getJsonObject(m);
            String month = object.getString(JSON_MONTH);
            String day = object.getString(JSON_DAY);
            String title = object.getString(JSON_TITLE);
            String topic = object.getString(JSON_TOPIC);
            String link = object.getString(JSON_LINK);
            LocalDate date = LocalDate.of(2018, Integer.parseInt(month), Integer.parseInt(day));
            ScheduleData newData = new ScheduleData("Lecture",date.toString(),title,topic,link);
            dataManager.addSchedule(newData);
            }
        
        JsonArray scheduleReferencesArray = json.getJsonObject(JSON_SCHEDULE_TAB).getJsonArray(JSON_REFERENCES);
        for (int m = 0; m < scheduleReferencesArray.size(); m++) {
            JsonObject object = scheduleReferencesArray.getJsonObject(m);
            String month = object.getString(JSON_MONTH);
            String day = object.getString(JSON_DAY);
            String title = object.getString(JSON_TITLE);
            String topic = object.getString(JSON_TOPIC);
            String link = object.getString(JSON_LINK);
            LocalDate date = LocalDate.of(2018, Integer.parseInt(month), Integer.parseInt(day));
            ScheduleData newData = new ScheduleData("Reference",date.toString(),title,topic,link);
            dataManager.addSchedule(newData);
            }
        
        JsonArray scheduleRecitationArray = json.getJsonObject(JSON_SCHEDULE_TAB).getJsonArray(JSON_RECITATIONS);
        for (int m = 0; m < scheduleRecitationArray.size(); m++) {
            JsonObject object = scheduleRecitationArray.getJsonObject(m);
            String month = object.getString(JSON_MONTH);
            String day = object.getString(JSON_DAY);
            String title = object.getString(JSON_TITLE);
            String topic = object.getString(JSON_TOPIC);
            String link = object.getString(JSON_LINK);
            LocalDate date = LocalDate.of(2018, Integer.parseInt(month), Integer.parseInt(day));
            ScheduleData newData = new ScheduleData("Recitation",date.toString(),title,topic,link);
            dataManager.addSchedule(newData);
            }
        
        JsonArray scheduleHWArray = json.getJsonObject(JSON_SCHEDULE_TAB).getJsonArray(JSON_HWS);
        for (int m = 0; m < scheduleHWArray.size(); m++) {
            JsonObject object = scheduleHWArray.getJsonObject(m);
            String month = object.getString(JSON_MONTH);
            String day = object.getString(JSON_DAY);
            String title = object.getString(JSON_TITLE);
            String topic = object.getString(JSON_TOPIC);
            String link = object.getString(JSON_LINK);
            LocalDate date = LocalDate.of(2018, Integer.parseInt(month), Integer.parseInt(day));
            ScheduleData newData = new ScheduleData("HW",date.toString(),title,topic,link);
            dataManager.addSchedule(newData);
            }
        
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Cannot Load: The file is not formatted correctly", 
                    "Error Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadTAs(CSGData data, JsonObject json, String tas) {
        JsonArray jsonTAArray = json.getJsonObject(JSON_OH_TAB).getJsonArray(tas);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            TAType type = TAType.valueOf(jsonTA.getString(JSON_TYPE));
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name, email, type);
            data.addTA(ta);
        }     
    }
      
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	// GET THE DATA
	CSGData dataManager = (CSGData)data;
        AppGUIModule gui = app.getGUIModule(); 
        // BUILD ARRAY FOR LOGOS
        JsonObject favicon = Json.createObjectBuilder()
                .add(JSON_SITE_HREF, dataManager.getFaviconPath().replace("\\", "/"))
                        .build();
        JsonObject navbar = Json.createObjectBuilder()
                .add(JSON_SITE_HREF, "http://www.stonybrook.edu")
                .add(JSON_SITE_PATH, dataManager.getNavbarPath().replace("\\", "/"))
                        .build();
        JsonObject leftFooter = Json.createObjectBuilder()
                .add(JSON_SITE_HREF, "http://www.stonybrook.edu")
                .add(JSON_SITE_PATH, dataManager.getLeftFooterPath().replace("\\", "/"))
                        .build();
        JsonObject rightFooter = Json.createObjectBuilder()
                .add(JSON_SITE_HREF, "http://www.stonybrook.edu")
                .add(JSON_SITE_PATH, dataManager.getRightFooterPath().replace("\\", "/"))
                        .build();
        JsonObject logos = Json.createObjectBuilder()
                .add(JSON_SITE_FAVICON, favicon)
                .add(JSON_SITE_NAVBAR, navbar)
                .add(JSON_SITE_BOTTOM_LEFT, leftFooter)
                .add(JSON_SITE_BOTTOM_RIGHT, rightFooter)
                .build();

        JsonReader instructorHoursReader = Json.createReader(new StringReader(dataManager.getInstructorOH()));
        JsonArray instructorHoursArray = instructorHoursReader.readArray();

        JsonObject instructor = Json.createObjectBuilder()
                .add(JSON_NAME, dataManager.getInstructorName())
                .add(JSON_ROOM, dataManager.getInstructorRoom())
                .add(JSON_EMAIL, dataManager.getInstructorEmail())
                .add(JSON_HOME_PAGE, dataManager.getInstructorHP())
                .add(JSON_HOURS,instructorHoursArray)              
                .build();
        
        JsonArrayBuilder pages = Json.createArrayBuilder();
        if(dataManager.homeCheckBoxChecker()){
                     JsonObject homeCheck = Json.createObjectBuilder()
                     .add(JSON_NAME, "Home")
                     .add(JSON_LINK, "index.html")
                     .build();
                     pages.add(homeCheck);
        }
        if(dataManager.syllabusCheckBoxChecker()){
                     JsonObject sylCheck = Json.createObjectBuilder()
                     .add(JSON_NAME, "Syllabus")
                     .add(JSON_LINK, "syllabus.html")
                     .build();
                     pages.add(sylCheck);
        }
        if(dataManager.scheduleCheckBoxChecker()){
                     JsonObject scheCheck = Json.createObjectBuilder()
                     .add(JSON_NAME, "Schedule")
                     .add(JSON_LINK, "schedule.html")
                     .build();
                     pages.add(scheCheck);
        }        
        if(dataManager.HWCheckBoxChecker()){
                     JsonObject hwCheck = Json.createObjectBuilder()
                     .add(JSON_NAME, "HWs")
                     .add(JSON_LINK, "hws.html")
                     .build();
                     pages.add(hwCheck);
        }
        
        // SYLLABUS TAB
        JsonReader topicArrayReader = Json.createReader(new StringReader(dataManager.getSyllabusTopics()));
        JsonArray topicsArray = topicArrayReader.readArray();
        
        JsonReader outcomeArrayBuilder = Json.createReader(new StringReader(dataManager.getSyllabusOutcomes()));
        JsonArray outcomeArray = outcomeArrayBuilder.readArray();        
        
        JsonReader sylTextBookReader = Json.createReader(new StringReader(dataManager.getSyllabusTextBooks()));
        JsonArray sylTextBookArray = sylTextBookReader.readArray();

        JsonReader sylGradedCompReader = Json.createReader(new StringReader(dataManager.getSyllabusGradedComp()));
        JsonArray sylGradedCompArray = sylGradedCompReader.readArray();
        
        JsonObject syllabus = Json.createObjectBuilder()
                .add(JSON_DESCRIPTION, dataManager.getSyllabusDescription())
                .add(JSON_TOPICS, topicsArray)
                .add(JSON_PREREQ, dataManager.getSyllabusPrereq())
                .add(JSON_OUTCOMES, outcomeArray)
                .add(JSON_TEXTBOOKS, sylTextBookArray)
                .add(JSON_GRADED_COMP, sylGradedCompArray)
                .add(JSON_GRADING_NOTE, dataManager.getSyllabusGradingNote())
                .add(JSON_ACADEMIC_DIS, dataManager.getSyllabusAcademicDis())
                .add(JSON_SPECIAL_ASSIST, dataManager.getSyllabusSpecialAssist())
                .build();
        
        // MT TAB
	JsonArrayBuilder lectureArrayBuilder = Json.createArrayBuilder();
	Iterator<LectureData> lectureIterator = dataManager.lecturesIterator();
        while (lectureIterator.hasNext()) {
            LectureData object = lectureIterator.next();
	    JsonObject lectureJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, object.getSection())
		    .add(JSON_DAYS, object.getDays())
                    .add(JSON_TIME, object.getTime())
                    .add(JSON_ROOM, object.getRoom())
                    .build();
            lectureArrayBuilder.add(lectureJson);
	}
        JsonArray lectureArray = lectureArrayBuilder.build();
        
	JsonArrayBuilder labArrayBuilder = Json.createArrayBuilder();
	Iterator<MTrecitationLab> labIterator = dataManager.labsIterator();
        while (labIterator.hasNext()) {
            MTrecitationLab object = labIterator.next();
	    JsonObject labJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, object.getSection())
		    .add(JSON_DAYTIME, object.getDayTime())
                    .add(JSON_LOCATION, object.getRoom())
                    .add(JSON_TA1, object.getTa1())
                    .add(JSON_TA2, object.getTa2())
                    .build();
            labArrayBuilder.add(labJson);
	}
        JsonArray labArray = labArrayBuilder.build();

        JsonArrayBuilder recitationArrayBuilder = Json.createArrayBuilder();
	Iterator<MTrecitationLab> recitationIterator = dataManager.recitationsIterator();
        while (recitationIterator.hasNext()) {
            MTrecitationLab object = recitationIterator.next();
	    JsonObject recitationJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, object.getSection())
		    .add(JSON_DAYTIME, object.getDayTime())
                    .add(JSON_LOCATION, object.getRoom())
                    .add(JSON_TA1, object.getTa1())
                    .add(JSON_TA2, object.getTa2())
                    .build();
            recitationArrayBuilder.add(recitationJson);
	}
        JsonArray recitationArray = recitationArrayBuilder.build();
        

        
	// OH TAB
        
        JsonObject instructorOH = Json.createObjectBuilder()
                .add(JSON_NAME, dataManager.getInstructorName())
                .add(JSON_ROOM, dataManager.getInstructorRoom())
                .add(JSON_EMAIL, dataManager.getInstructorEmail())
                .add(JSON_HOME_PAGE, dataManager.getInstructorHP())
                .add(JSON_HOURS,instructorHoursArray)              
                .build();
        
        
	JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJECTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
        
        // SCHEDULE TAB
        
    
        DatePicker startDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_STARTING_DATE);
        DatePicker endDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_ENDING_DATE);
        String mondayMonth = "" + startDatePicker.getValue().getMonthValue();
        String mondayDay = ""+ startDatePicker.getValue().getDayOfMonth();
        String fridayMonth = "" + endDatePicker.getValue().getMonthValue();
        String fridayDay = ""+ endDatePicker.getValue().getDayOfMonth(); 
        try{
        //ARRAY OF HOLIDAYS
        JsonArrayBuilder scheduleHolidayArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleHolidayIterator = dataManager.schedulesIterator();
        while (scheduleHolidayIterator.hasNext()) {
            ScheduleData object = scheduleHolidayIterator.next();
            if(object.getType().equals("Holiday")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject holidayJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleHolidayArrayBuilder.add(holidayJson);
            }
	}
        JsonArray scheduleHolidayArray = scheduleHolidayArrayBuilder.build();

        // ARRAY OF LECTURES
        
        JsonArrayBuilder scheduleLectureArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleLectureIterator = dataManager.schedulesIterator();
        while (scheduleLectureIterator.hasNext()) {
            ScheduleData object = scheduleLectureIterator.next();
            if(object.getType().equals("Lecture")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject lectureJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_TOPIC, object.getTopic())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleLectureArrayBuilder.add(lectureJson);
            }
	}
        JsonArray scheduleLectureArray = scheduleLectureArrayBuilder.build();
        
        // ARRAY OF REFERENCES
        JsonArrayBuilder scheduleReferenceArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleReferenceIterator = dataManager.schedulesIterator();
        while (scheduleReferenceIterator.hasNext()) {
            ScheduleData object = scheduleReferenceIterator.next();
            if(object.getType().equals("Reference")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject referenceJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_TOPIC, object.getTopic())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleReferenceArrayBuilder.add(referenceJson);
            }
	}
        JsonArray scheduleReferenceArray = scheduleReferenceArrayBuilder.build();
        
                
        // ARRAY OF RECITATIONS
        JsonArrayBuilder scheduleRecitationArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleRecitationIterator = dataManager.schedulesIterator();
        while (scheduleRecitationIterator.hasNext()) {
            ScheduleData object = scheduleRecitationIterator.next();
            if(object.getType().equals("Recitation")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject recitationJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_TOPIC, object.getTopic())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleRecitationArrayBuilder.add(recitationJson);
            }
	}
        JsonArray scheduleRecitationArray = scheduleRecitationArrayBuilder.build();
                
        // ARRAY OF HWS
        JsonArrayBuilder scheduleHwArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleHwIterator = dataManager.schedulesIterator();
        while (scheduleHwIterator.hasNext()) {
            ScheduleData object = scheduleHwIterator.next();
            if(object.getType().equals("HW")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject hwJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_TOPIC, object.getTopic())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleHwArrayBuilder.add(hwJson);
            }
	}
        JsonArray scheduleHwArray = scheduleHwArrayBuilder.build();

	// THEN PUT IT ALL TOGETHER IN A JsonObject

        JsonObject siteTabObject = Json.createObjectBuilder()
                .add(JSON_SITE_SUBJECT, "" + dataManager.getSiteSubject())
                .add(JSON_SITE_NUMBER, "" +dataManager.getSiteNumber())
                .add(JSON_SITE_SEMESTER, "" +dataManager.getSiteSemester())
                .add(JSON_SITE_YEAR, ""+dataManager.getSiteYear())
                .add(JSON_SITE_TITLE, ""+dataManager.getSiteTitle())
                .add(JSON_SITE_LOGOS, logos)
                .add(JSON_INSTRUCTOR, instructor)
                .add(JSON_PAGES, pages)
                    .build();
        
        JsonObject syllabusTabObject = Json.createObjectBuilder()
                .add(JSON_SYLLABUS, syllabus)
                .build();
        
        JsonObject mtTabObject = Json.createObjectBuilder()
                .add(JSON_LECTURES, lectureArray)
                .add(JSON_LABS, labArray)
                .add(JSON_RECITATIONS, recitationArray)
                .build();
        
        JsonObject ohTabObject = Json.createObjectBuilder()
                .add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_INSTRUCTOR, instructorOH)
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray)
                .build();
        
        JsonObject scheduleTabObject = Json.createObjectBuilder()
                .add(JSON_STARTING_MONDAY_MONTH, mondayMonth)
                .add(JSON_STARTING_MONDAY_DAY, mondayDay)
                .add(JSON_ENDING_FRIDAY_MONTH, fridayMonth)
                .add(JSON_ENDING_FRIDAY_DAY, fridayDay)
                .add(JSON_HOLIDAYS, scheduleHolidayArray)
                .add(JSON_LECTURES, scheduleLectureArray)
                .add(JSON_REFERENCES, scheduleReferenceArray)
                .add(JSON_RECITATIONS, scheduleRecitationArray)
                .add(JSON_HWS, scheduleHwArray)
                .build();
        
        
        // PUT EVERY TAB OBJECT IN HERE
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_SITE_TAB, siteTabObject)       
                .add(JSON_SYLLABUS_TAB, syllabusTabObject)
                .add(JSON_MT_TAB, mtTabObject)
                .add(JSON_OH_TAB, ohTabObject)
                .add(JSON_SCHEDULE_TAB, scheduleTabObject)
		.build();

	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Cannot Save: All ComboBox needs to be Checked, and"
                    + "Schedule Item should not have null values"
                    , "Error Message", JOptionPane.INFORMATION_MESSAGE);

        }
    }
    
    public void saveJsonFile(JsonObject dataManagerJSO, String filePath){
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        try{
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Cannot Export: All ComboBox needs to be Checked, and"
                    + "Schedule Item should not have null values"
                    , "Error Message", JOptionPane.INFORMATION_MESSAGE);

        }
    }
    
    // IMPORTING/EXPORTIHello WorldNG DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        
        CSGData dataManager = (CSGData)data;
        
        // CREATE FOLDLERS
        String dirText = dataManager.getExportDirText();
        File myfile = new File(dirText+"\\css\\dummy");        
        FileUtils.touch(myfile);
        FileUtils.deleteQuietly(myfile);
        myfile = new File(dirText+"\\images\\dummy");        
        FileUtils.touch(myfile);
        FileUtils.deleteQuietly(myfile);        
        
        File jsOG = new File(".\\work\\js");
        File jsCopy = new File(dirText);
        FileUtils.copyDirectoryToDirectory(jsOG, jsCopy);
        
        // RESET HTMLS
        File deleteHTML = new File(dirText+"\\index.html");
        FileUtils.deleteQuietly(deleteHTML);  
        deleteHTML = new File(dirText+"\\syllabus.html");
        FileUtils.deleteQuietly(deleteHTML);
        deleteHTML = new File(dirText+"\\schedule.html");
        FileUtils.deleteQuietly(deleteHTML); 
        deleteHTML = new File(dirText+"\\hws.html");
        FileUtils.deleteQuietly(deleteHTML);         

        
        // CREATE JSON FOR SITE TAB        
        // BUILD ARRAY FOR LOGOS
        JsonObject favicon = Json.createObjectBuilder()
                .add(JSON_SITE_HREF, "./images/SBUShieldFavicon.ico")
                        .build();
        JsonObject navbar = Json.createObjectBuilder()
                .add(JSON_SITE_HREF, "http://www.stonybrook.edu")
                .add(JSON_SITE_PATH, "./images/SBUDarkRedShieldLogo.png")
                        .build();
        JsonObject leftFooter = Json.createObjectBuilder()
                .add(JSON_SITE_HREF, "http://www.stonybrook.edu")
                .add(JSON_SITE_PATH, "./images/SBUWhiteShieldLogo.jpg")
                        .build();
        JsonObject rightFooter = Json.createObjectBuilder()
                .add(JSON_SITE_HREF, "http://www.stonybrook.edu")
                .add(JSON_SITE_PATH, "./images/SBUCSLogo.png")
        
                        .build();
        JsonObject logos = Json.createObjectBuilder()
                .add(JSON_SITE_FAVICON, favicon)
                .add(JSON_SITE_NAVBAR, navbar)
                .add(JSON_SITE_BOTTOM_LEFT, leftFooter)
                .add(JSON_SITE_BOTTOM_RIGHT, rightFooter)
                .build();

        JsonReader instructorHoursReader = Json.createReader(new StringReader(dataManager.getInstructorOH()));
        JsonArray instructorHoursArray = instructorHoursReader.readArray();

        JsonObject instructor = Json.createObjectBuilder()
                .add(JSON_NAME, dataManager.getInstructorName())
                .add(JSON_ROOM, dataManager.getInstructorRoom())
                .add(JSON_EMAIL, dataManager.getInstructorEmail())
                .add(JSON_HOME_PAGE, dataManager.getInstructorHP())
                .add(JSON_PHOTO, "./images/RichardMcKenna.jpg")
                .add(JSON_HOURS,instructorHoursArray)              
                .build();
        
        JsonArrayBuilder pages = Json.createArrayBuilder();
        if(dataManager.homeCheckBoxChecker()){
                     JsonObject homeCheck = Json.createObjectBuilder()
                     .add(JSON_NAME, "Home")
                     .add(JSON_LINK, "index.html")
                     .build();
                     pages.add(homeCheck);
        }
        if(dataManager.syllabusCheckBoxChecker()){
                     JsonObject sylCheck = Json.createObjectBuilder()
                     .add(JSON_NAME, "Syllabus")
                     .add(JSON_LINK, "syllabus.html")
                     .build();
                     pages.add(sylCheck);
        }
        if(dataManager.scheduleCheckBoxChecker()){
                     JsonObject scheCheck = Json.createObjectBuilder()
                     .add(JSON_NAME, "Schedule")
                     .add(JSON_LINK, "schedule.html")
                     .build();
                     pages.add(scheCheck);
        }        
        if(dataManager.HWCheckBoxChecker()){
                     JsonObject hwCheck = Json.createObjectBuilder()
                     .add(JSON_NAME, "HWs")
                     .add(JSON_LINK, "hws.html")
                     .build();
                     pages.add(hwCheck);
        }
        
        JsonObject siteTabObject = Json.createObjectBuilder()
                .add(JSON_SITE_SUBJECT, "" + dataManager.getSiteSubject())
                .add(JSON_SITE_NUMBER, "" +dataManager.getSiteNumber())
                .add(JSON_SITE_SEMESTER, "" +dataManager.getSiteSemester())
                .add(JSON_SITE_YEAR, ""+dataManager.getSiteYear())
                .add(JSON_SITE_TITLE, ""+dataManager.getSiteTitle())
                .add(JSON_SITE_LOGOS, logos)
                .add(JSON_INSTRUCTOR, instructor)
                .add(JSON_PAGES, pages)
                    .build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        saveJsonFile(siteTabObject, dirText+"\\js\\PageData.json");
        
        // EXPORT SYLLABUS TAB
        JsonReader topicArrayReader = Json.createReader(new StringReader(dataManager.getSyllabusTopics()));
        JsonArray topicsArray = topicArrayReader.readArray();
        
        JsonReader outcomeArrayBuilder = Json.createReader(new StringReader(dataManager.getSyllabusOutcomes()));
        JsonArray outcomeArray = outcomeArrayBuilder.readArray();        
        
        JsonReader sylTextBookReader = Json.createReader(new StringReader(dataManager.getSyllabusTextBooks()));
        JsonArray sylTextBookArray = sylTextBookReader.readArray();

        JsonReader sylGradedCompReader = Json.createReader(new StringReader(dataManager.getSyllabusGradedComp()));
        JsonArray sylGradedCompArray = sylGradedCompReader.readArray();
        
        JsonObject syllabus = Json.createObjectBuilder()
                .add(JSON_DESCRIPTION, dataManager.getSyllabusDescription())
                .add(JSON_TOPICS, topicsArray)
                .add(JSON_PREREQ, dataManager.getSyllabusPrereq())
                .add(JSON_OUTCOMES, outcomeArray)
                .add(JSON_TEXTBOOKS, sylTextBookArray)
                .add(JSON_GRADED_COMP, sylGradedCompArray)
                .add(JSON_GRADING_NOTE, dataManager.getSyllabusGradingNote())
                .add(JSON_ACADEMIC_DIS, dataManager.getSyllabusAcademicDis())
                .add(JSON_SPECIAL_ASSIST, dataManager.getSyllabusSpecialAssist())
                .build();

        
        saveJsonFile(syllabus, dirText+"\\js\\SyllabusData.json");
        
        // EXPORT MEETING TIME TAB
        
        JsonArrayBuilder lectureArrayBuilder = Json.createArrayBuilder();
	Iterator<LectureData> lectureIterator = dataManager.lecturesIterator();
        while (lectureIterator.hasNext()) {
            LectureData object = lectureIterator.next();
	    JsonObject lectureJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, object.getSection())
		    .add(JSON_DAYS, object.getDays())
                    .add(JSON_TIME, object.getTime())
                    .add(JSON_ROOM, object.getRoom())
                    .build();
            lectureArrayBuilder.add(lectureJson);
	}
        JsonArray lectureArray = lectureArrayBuilder.build();
        
	JsonArrayBuilder labArrayBuilder = Json.createArrayBuilder();
	Iterator<MTrecitationLab> labIterator = dataManager.labsIterator();
        while (labIterator.hasNext()) {
            MTrecitationLab object = labIterator.next();
	    JsonObject labJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, object.getSection())
		    .add(JSON_DAYTIME, object.getDayTime())
                    .add(JSON_LOCATION, object.getRoom())
                    .add(JSON_TA1, object.getTa1())
                    .add(JSON_TA2, object.getTa2())
                    .build();
            labArrayBuilder.add(labJson);
	}
        JsonArray labArray = labArrayBuilder.build();

        JsonArrayBuilder recitationArrayBuilder = Json.createArrayBuilder();
	Iterator<MTrecitationLab> recitationIterator = dataManager.recitationsIterator();
        while (recitationIterator.hasNext()) {
            MTrecitationLab object = recitationIterator.next();
	    JsonObject recitationJson = Json.createObjectBuilder()
		    .add(JSON_SECTION, object.getSection())
		    .add(JSON_DAYTIME, object.getDayTime())
                    .add(JSON_LOCATION, object.getRoom())
                    .add(JSON_TA1, object.getTa1())
                    .add(JSON_TA2, object.getTa2())
                    .build();
            recitationArrayBuilder.add(recitationJson);
	}
        JsonArray recitationArray = recitationArrayBuilder.build();
        
        JsonObject mtTabObject = Json.createObjectBuilder()
                .add(JSON_LECTURES, lectureArray)
                .add(JSON_LABS, labArray)
                .add(JSON_RECITATIONS, recitationArray)
                .build();
        
        saveJsonFile(mtTabObject, dirText+"\\js\\SectionsData.json");
        
        // EXPORT OFFICE HOURS TAB
        
                
        JsonObject instructorOH = Json.createObjectBuilder()
                .add(JSON_NAME, dataManager.getInstructorName())
                .add(JSON_ROOM, dataManager.getInstructorRoom())
                .add(JSON_EMAIL, dataManager.getInstructorEmail())
                .add(JSON_HOME_PAGE, dataManager.getInstructorHP())
                .add(JSON_PHOTO, "./images/RichardMcKenna.jpg")
                .add(JSON_HOURS,instructorHoursArray)              
                .build();
        
        
        JsonArrayBuilder gradTAsArrayBuilder = Json.createArrayBuilder();
        JsonArrayBuilder undergradTAsArrayBuilder = Json.createArrayBuilder();
	Iterator<TeachingAssistantPrototype> tasIterator = dataManager.teachingAssistantsIterator();
        while (tasIterator.hasNext()) {
            TeachingAssistantPrototype ta = tasIterator.next();
	    JsonObject taJson = Json.createObjectBuilder()
		    .add(JSON_NAME, ta.getName())
		    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_TYPE, ta.getType().toString()).build();
            if (ta.getType().equals(TAType.Graduate.toString()))
                gradTAsArrayBuilder.add(taJson);
            else
                undergradTAsArrayBuilder.add(taJson);
	}
        JsonArray gradTAsArray = gradTAsArrayBuilder.build();
	JsonArray undergradTAsArray = undergradTAsArrayBuilder.build();

	// NOW BUILD THE OFFICE HOURS JSON OBJECTS TO SAVE
	JsonArrayBuilder officeHoursArrayBuilder = Json.createArrayBuilder();
        Iterator<TimeSlot> timeSlotsIterator = dataManager.officeHoursIterator();
        while (timeSlotsIterator.hasNext()) {
            TimeSlot timeSlot = timeSlotsIterator.next();
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                DayOfWeek dow = DayOfWeek.values()[i];
                tasIterator = timeSlot.getTAsIterator(dow);
                while (tasIterator.hasNext()) {
                    TeachingAssistantPrototype ta = tasIterator.next();
                    JsonObject tsJson = Json.createObjectBuilder()
                        .add(JSON_START_TIME, timeSlot.getStartTime().replace(":", "_"))
                        .add(JSON_DAY_OF_WEEK, dow.toString())
                        .add(JSON_NAME, ta.getName()).build();
                    officeHoursArrayBuilder.add(tsJson);
                }
            }
	}
	JsonArray officeHoursArray = officeHoursArrayBuilder.build();
  
        JsonObject ohTabObject = Json.createObjectBuilder()
                .add(JSON_START_HOUR, "" + dataManager.getStartHour())
		.add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_INSTRUCTOR, instructorOH)
                .add(JSON_GRAD_TAS, gradTAsArray)
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, officeHoursArray)
                .build();
        
        saveJsonFile(ohTabObject, dirText+"\\js\\OfficeHoursData.json");
        
        // EXPORT SCHEDULE TAB
        
        AppGUIModule gui = app.getGUIModule();        
        DatePicker startDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_STARTING_DATE);
        DatePicker endDatePicker = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_ENDING_DATE);
        String mondayMonth = "" + startDatePicker.getValue().getMonthValue();
        String mondayDay = ""+ startDatePicker.getValue().getDayOfMonth();
        String fridayMonth = "" + endDatePicker.getValue().getMonthValue();
        String fridayDay = ""+ endDatePicker.getValue().getDayOfMonth(); 
        
        //ARRAY OF HOLIDAYS
        JsonArrayBuilder scheduleHolidayArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleHolidayIterator = dataManager.schedulesIterator();
        while (scheduleHolidayIterator.hasNext()) {
            ScheduleData object = scheduleHolidayIterator.next();
            if(object.getType().equals("Holiday")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject holidayJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleHolidayArrayBuilder.add(holidayJson);
            }
	}
        JsonArray scheduleHolidayArray = scheduleHolidayArrayBuilder.build();

        // ARRAY OF LECTURES
        
        JsonArrayBuilder scheduleLectureArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleLectureIterator = dataManager.schedulesIterator();
        while (scheduleLectureIterator.hasNext()) {
            ScheduleData object = scheduleLectureIterator.next();
            if(object.getType().equals("Lecture")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject lectureJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_TOPIC, object.getTopic())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleLectureArrayBuilder.add(lectureJson);
            }
	}
        JsonArray scheduleLectureArray = scheduleLectureArrayBuilder.build();
        
        // ARRAY OF REFERENCES
        JsonArrayBuilder scheduleReferenceArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleReferenceIterator = dataManager.schedulesIterator();
        while (scheduleReferenceIterator.hasNext()) {
            ScheduleData object = scheduleReferenceIterator.next();
            if(object.getType().equals("Reference")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject referenceJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_TOPIC, object.getTopic())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleReferenceArrayBuilder.add(referenceJson);
            }
	}
        JsonArray scheduleReferenceArray = scheduleReferenceArrayBuilder.build();
        
                
        // ARRAY OF RECITATIONS
        JsonArrayBuilder scheduleRecitationArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleRecitationIterator = dataManager.schedulesIterator();
        while (scheduleRecitationIterator.hasNext()) {
            ScheduleData object = scheduleRecitationIterator.next();
            if(object.getType().equals("Recitation")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject recitationJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_TOPIC, object.getTopic())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleRecitationArrayBuilder.add(recitationJson);
            }
	}
        JsonArray scheduleRecitationArray = scheduleRecitationArrayBuilder.build();
                
        // ARRAY OF HWS
        JsonArrayBuilder scheduleHwArrayBuilder = Json.createArrayBuilder();
	Iterator<ScheduleData> scheduleHwIterator = dataManager.schedulesIterator();
        while (scheduleHwIterator.hasNext()) {
            ScheduleData object = scheduleHwIterator.next();
            if(object.getType().equals("HW")){
                String date = object.getDate();
                LocalDate localDate = LocalDate.parse(date);
                JsonObject hwJson = Json.createObjectBuilder()
		    .add(JSON_MONTH, ""+localDate.getMonthValue())
		    .add(JSON_DAY, ""+localDate.getDayOfMonth())
                    .add(JSON_TITLE, object.getTitle())
                    .add(JSON_TOPIC, object.getTopic())
                    .add(JSON_LINK, object.getLink())
                    .build();
            scheduleHwArrayBuilder.add(hwJson);
            }
	}
        JsonArray scheduleHwArray = scheduleHwArrayBuilder.build();

        JsonObject scheduleTabObject = Json.createObjectBuilder()
                .add(JSON_STARTING_MONDAY_MONTH, mondayMonth)
                .add(JSON_STARTING_MONDAY_DAY, mondayDay)
                .add(JSON_ENDING_FRIDAY_MONTH, fridayMonth)
                .add(JSON_ENDING_FRIDAY_DAY, fridayDay)
                .add(JSON_HOLIDAYS, scheduleHolidayArray)
                .add(JSON_LECTURES, scheduleLectureArray)
                .add(JSON_REFERENCES, scheduleReferenceArray)
                .add(JSON_RECITATIONS, scheduleRecitationArray)
                .add(JSON_HWS, scheduleHwArray)
                .build();
        
        saveJsonFile(scheduleTabObject, dirText+"\\js\\ScheduleData.json");

        // COPY IMAGES
        
        File faviconOG = new File(dataManager.getFaviconPath().replace("\\", "/"));
        File faviconCopy = new File(dirText+"\\images\\SBUShieldFavicon.ico");
        FileUtils.copyFile(faviconOG, faviconCopy);
        
        File darkShieldOG = new File(dataManager.getNavbarPath().replace("\\", "/"));
        File darkShieldCopy = new File(dirText+"\\images\\SBUDarkRedShieldLogo.png");
        FileUtils.copyFile(darkShieldOG, darkShieldCopy);
        
        File whiteShieldOG = new File(dataManager.getLeftFooterPath().replace("\\", "/"));
        File whiteShieldCopy = new File(dirText+"\\images\\SBUWhiteShieldLogo.jpg");
        FileUtils.copyFile(whiteShieldOG, whiteShieldCopy);
        
        File logoOG = new File(dataManager.getRightFooterPath().replace("\\", "/"));
        File logoCopy = new File(dirText+"\\images\\SBUCSLogo.png");
        FileUtils.copyFile(logoOG, logoCopy);
        
        File mcKennaOG = new File(".\\images\\RichardMcKenna.jpg");
        File mcKennaCopy = new File(dirText+"\\images\\RichardMcKenna.jpg");
        FileUtils.copyFile(mcKennaOG, mcKennaCopy);
        
        // COPY CSS
                
        ComboBox styleCombo = (ComboBox) gui.getGUINode(SITE_STYLE_COMBO);
        String selectedCss = styleCombo.getValue().toString();
        File cssOG = new File(".\\work\\css\\"+selectedCss);
        File cssCopy = new File(dirText+"\\css\\sea_wolf.css");
        FileUtils.copyFile(cssOG, cssCopy);
        
        File homecssOG = new File(".\\work\\homecss\\course_homepage_layout.css");
        File homecssCopy = new File(dirText+"\\css\\course_homepage_layout.css");
        FileUtils.copyFile(homecssOG, homecssCopy);
        
        if(dataManager.homeCheckBoxChecker()){
            File OG = new File(".\\work\\html\\index.html");
            File Copy = new File(dirText+"\\index.html");
            FileUtils.copyFile(OG, Copy);
        }
        if(dataManager.syllabusCheckBoxChecker()){
           File OG = new File(".\\work\\html\\syllabus.html");
            File Copy = new File(dirText+"\\syllabus.html");
            FileUtils.copyFile(OG, Copy);
        }
        if(dataManager.scheduleCheckBoxChecker()){
           File OG = new File(".\\work\\html\\schedule.html");
            File Copy = new File(dirText+"\\schedule.html");
            FileUtils.copyFile(OG, Copy);
        }        
        if(dataManager.HWCheckBoxChecker()){
           File OG = new File(".\\work\\html\\hws.html");
            File Copy = new File(dirText+"\\hws.html");
            FileUtils.copyFile(OG, Copy);
        }
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        props.removeProperty(APP_PATH_EXPORT);
        props.addProperty(APP_PATH_EXPORT, dirText);
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}