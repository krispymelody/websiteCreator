package oh;

/**
 * This class provides the properties that are needed to be loaded for
 * setting up To Do List Maker workspace controls including language-dependent
 * text.
 * 
 * @author Kris Pan
 * @version 1.0
 */
public enum CSGPropertyType {

    /* THESE ARE THE NODES IN OUR APP */
    // FOR SIMPLE OK/CANCEL DIALOG BOXES
    OH_OK_PROMPT,
    OH_CANCEL_PROMPT,

    // THESE ARE FOR TEXT PARTICULAR TO THE APP'S WORKSPACE CONTROLS
    OH_LEFT_PANE,
    OH_TAS_HEADER_PANE,
    OH_TAS_HEADER_LABEL,
    OH_GRAD_UNDERGRAD_TAS_PANE,
    OH_ALL_RADIO_BUTTON,
    OH_GRAD_RADIO_BUTTON,
    OH_UNDERGRAD_RADIO_BUTTON,
    OH_TAS_HEADER_TEXT_FIELD,
    OH_TAS_TABLE_VIEW,
    OH_NAME_TABLE_COLUMN,
    OH_EMAIL_TABLE_COLUMN,
    OH_SLOTS_TABLE_COLUMN,
    OH_TYPE_TABLE_COLUMN,

    OH_ADD_TA_PANE,
    OH_NAME_TEXT_FIELD,
    OH_EMAIL_TEXT_FIELD,
    OH_ADD_TA_BUTTON,

    SITE_BANNER,
    SITE_BANNER_SUBJECT,
    SITE_BANNER_SUBJECT_COMBO,
    SITE_BANNER_SUBJECT_OPTIONS,
    SITE_BANNER_NUMBER,
    SITE_BANNER_NUMBER_COMBO,
    SITE_BANNER_NUMBER_OPTIONS,
    SITE_BANNER_SEMESTER,
    SITE_BANNER_SEMESTER_COMBO,
    SITE_BANNER_SEMESTER_OPTIONS,
    SITE_BANNER_YEAR,
    SITE_BANNER_YEAR_COMBO,
    SITE_BANNER_YEAR_OPTIONS,
    SITE_BANNER_TITLE,
    SITE_BANNER_TEXT_FIELD,
    SITE_BANNER_EXPORT_DIR,
    SITE_BANNER_DIR,
    EXPORT_DIR_1,
    EXPORT_DIR_2,
    EXPORT_DIR_3,
    EXPORT_DIR_4,
    EXPORT_DIR_5,
    EXPORT_DIR_6,
    
    SITE_PAGES,
    SITE_PAGES_HOME_CHECK,
    SITE_PAGES_HOME,
    SITE_PAGES_SYLLABUS_CHECK,
    SITE_PAGES_SYLLABUS,
    SITE_PAGES_SCHEDULE_CHECK,
    SITE_PAGES_SCHEDULE,
    SITE_PAGES_HW_CHECK,
    SITE_PAGES_HW,
    
    SITE_STYLE,
    SITE_STYLE_FAVICON,
    SITE_STYLE_FAVICON_IMAGE,
    SITE_STYLE_NAVBAR,
    SITE_STYLE_NAVBAR_IMAGE,
    SITE_STYLE_RIGHT_FOOTER,
    SITE_STYLE_RIGHT_FOOTER_IMAGE,
    SITE_STYLE_LEFT_FOOTER,
    SITE_STYLE_LEFT_FOOTER_IMAGE,
    SITE_STYLE_SHEET,
    SITE_STYLE_NOTE,
    SITE_STYLE_COMBO,
    DEFAULT_STYLE_COMBO,
    SITE_INSTRUCTOR,
    SITE_INSTRUCTOR_NAME,
    SITE_INSTRUCTOR_NAME_INPUT,
    SITE_INSTRUCTOR_ROOM,
    SITE_INSTRUCTOR_ROOM_INPUT,
    SITE_INSTRUCTOR_EMAIL,
    SITE_INSTRUCTOR_EMAIL_INPUT,
    SITE_INSTRUCTOR_HP,
    SITE_INSTRUCTOR_HP_INPUT,
    SITE_INSTRUCTOR_BUTTON,
    SITE_INSTRUCTOR_OH,
    SITE_TEXT_AREA,
    SITE_INSTRUCTOR_HBOX,
    
    SYLLABUS_DESCRIPTION_BUTTON,
    SYLLABUS_DESCRIPTION,
    SYLLABUS_DESCRIPTION_INPUT,
    SYLLABUS_TOPICS_BUTTON,
    SYLLABUS_TOPICS,
    SYLLABUS_TOPICS_INPUT,
    SYLLABUS_PREREQ_BUTTON,
    SYLLABUS_PREREQ,
    SYLLABUS_PREREQ_INPUT,
    SYLLABUS_OUTCOMES_BUTTON,
    SYLLABUS_OUTCOMES,
    SYLLABUS_OUTCOMES_INPUT,
    SYLLABUS_TEXTBOOKS_BUTTON,
    SYLLABUS_TEXTBOOKS,
    SYLLABUS_TEXTBOOKS_INPUT,
    SYLLABUS_GRADED_COMP_BUTTON,
    SYLLABUS_GRADED_COMP,
    SYLLABUS_GRADED_COMP_INPUT,
    SYLLABUS_GRADING_NOTE_BUTTON,
    SYLLABUS_GRADING_NOTE,
    SYLLABUS_GRADING_NOTE_INPUT,
    SYLLABUS_ACADEMIC_DISHONESTY_BUTTON,
    SYLLABUS_ACADEMIC_DISHONESTY,
    SYLLABUS_ACADEMIC_DISHONESTY_INPUT,
    SYLLABUS_SPECIAL_ASSIST_BUTTON,
    SYLLABUS_SPECIAL_ASSIST,
    SYLLABUS_SPECIAL_ASSIST_INPUT,
    
    MEETING_LECTURES_ADD_BUTTON,
    MEETING_LECTURES_DELETE_BUTTON,
    MEETING_LECTURES,
    MEETING_LECTURES_TABLE,
    MEETING_LECTURES_SECTION,
    MEETING_LECTURES_DAYS,
    MEETING_LECTURES_TIME,
    MEETING_LECTURES_ROOM,
    MEETING_RECITATIONS_ADD_BUTTON,
    MEETING_RECITATIONS_DELETE_BUTTON,
    MEETING_RECITATIONS,
    MEETING_RECITATIONS_TABLE,
    MEETING_RECITATIONS_SECTION,
    MEETING_RECITATIONS_DAYTIME,
    MEETING_RECITATIONS_ROOM,
    MEETING_RECITATIONS_TA1,
    MEETING_RECITATIONS_TA2,
    MEETING_LABS_ADD_BUTTON,
    MEETING_LABS_DELETE_BUTTON,
    MEETING_LABS,
    MEETING_LABS_TABLE,
    MEETING_LABS_SECTION,
    MEETING_LABS_DAYTIME,
    MEETING_LABS_ROOM,
    MEETING_LABS_TA1,
    MEETING_LABS_TA2,
    
    SCHEDULE_BOUNDARIES,
    SCHEDULE_BOUNDARIES_STARTING,
    SCHEDULE_BOUNDARIES_STARTING_DATE,
    SCHEDULE_BOUNDARIES_ENDING,
    SCHEDULE_BOUNDARIES_ENDING_DATE,
    SCHEDULE_ITEMS_DELETE_BUTTON,
    SCHEDULE_ITEMS,
    SCHEDULE_ITEMS_TABLE,
    SCHEDULE_ITEMS_TYPE,
    SCHEDULE_ITEMS_DATE,
    SCHEDULE_ITEMS_TITLE,
    SCHEDULE_ITEMS_TOPIC,
    SCHEDULE_ADD_EDIT,
    SCHEDULE_ADD_EDIT_TYPE,
    SCHEDULE_ADD_EDIT_DATE,
    SCHEDULE_ADD_EDIT_DATE_PICK,
    SCHEDULE_ADD_EDIT_TITLE,
    SCHEDULE_ADD_EDIT_TITLE_INPUT,
    SCHEDULE_ADD_EDIT_TOPIC,
    SCHEDULE_ADD_EDIT_TOPIC_INPUT,
    SCHEDULE_ADD_EDIT_LINK,
    SCHEDULE_ADD_EDIT_LINK_INPUT,
    SCHEDULE_ADD_BUTTON,
    SCHEDULE_ADD_EDIT_CLEAR_BUTTON,
    SCHEDULE_TYPE_COMBO,
    SCHEDULE_TYPE_OPTIONS,
    
    OH_RIGHT_PANE,
    OH_OFFICE_HOURS_HEADER_PANE,
    OH_OFFICE_HOURS_HEADER_LABEL,
    OH_OFFICE_HOURS_TABLE_VIEW,
    OH_START_TIME_TABLE_COLUMN,
    OH_END_TIME_TABLE_COLUMN,
    OH_MONDAY_TABLE_COLUMN,
    OH_TUESDAY_TABLE_COLUMN,
    OH_WEDNESDAY_TABLE_COLUMN,
    OH_THURSDAY_TABLE_COLUMN,
    OH_FRIDAY_TABLE_COLUMN,
    OH_DAYS_OF_WEEK,
    OH_FOOLPROOF_SETTINGS,
    
    OH_TAS_DELETE_BUTTON,
    OH_OFFICE_HOURS_START_LABEL,
    TIME_OPTIONS,
    START_TIME_FILTER,
    END_TIME_FILTER,
    DEFAULT_START_TIME,
    DEFAULT_END_TIME,
    OH_OFFICE_HOURS_END_LABEL,
    
    // FOR THE EDIT DIALOG
    OH_TA_EDIT_DIALOG,
    OH_TA_DIALOG_GRID_PANE,
    OH_TA_DIALOG_HEADER_LABEL, 
    OH_TA_DIALOG_NAME_LABEL,
    OH_TA_DIALOG_NAME_TEXT_FIELD,
    OH_TA_DIALOG_EMAIL_LABEL,
    OH_TA_DIALOG_EMAIL_TEXT_FIELD,
    OH_TA_DIALOG_TYPE_LABEL,
    OH_TA_DIALOG_TYPE_BOX,
    OH_TA_DIALOG_GRAD_RADIO_BUTTON,
    OH_TA_DIALOG_UNDERGRAD_RADIO_BUTTON,
    OH_TA_DIALOG_OK_BOX,
    OH_TA_DIALOG_OK_BUTTON, 
    OH_TA_DIALOG_CANCEL_BUTTON, 
    
    // THESE ARE FOR ERROR MESSAGES PARTICULAR TO THE APP
    OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT,
    
}