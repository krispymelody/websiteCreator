package oh.workspace.controllers;

import djf.modules.AppGUIModule;
import static djf.modules.AppGUIModule.ENABLED;
import djf.ui.AppNodesBuilder;
import djf.ui.dialogs.AppDialogsFacade;
import java.io.File;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.JOptionPane;
import oh.CSGApp;
import static oh.CSGPropertyType.END_TIME_FILTER;
import static oh.CSGPropertyType.MEETING_RECITATIONS_SECTION;
import static oh.CSGPropertyType.MEETING_RECITATIONS_TABLE;
import static oh.CSGPropertyType.OH_EMAIL_TEXT_FIELD;
import static oh.CSGPropertyType.OH_FOOLPROOF_SETTINGS;
import static oh.CSGPropertyType.OH_NAME_TEXT_FIELD;
import static oh.CSGPropertyType.OH_NO_TA_SELECTED_CONTENT;
import static oh.CSGPropertyType.OH_NO_TA_SELECTED_TITLE;
import static oh.CSGPropertyType.OH_OFFICE_HOURS_TABLE_VIEW;
import static oh.CSGPropertyType.OH_TAS_TABLE_VIEW;
import static oh.CSGPropertyType.OH_TA_EDIT_DIALOG;
import static oh.CSGPropertyType.SCHEDULE_ADD_BUTTON;
import static oh.CSGPropertyType.SCHEDULE_ADD_EDIT_DATE_PICK;
import static oh.CSGPropertyType.SCHEDULE_ADD_EDIT_LINK_INPUT;
import static oh.CSGPropertyType.SCHEDULE_ADD_EDIT_TITLE_INPUT;
import static oh.CSGPropertyType.SCHEDULE_ADD_EDIT_TOPIC_INPUT;
import static oh.CSGPropertyType.SCHEDULE_BOUNDARIES_ENDING_DATE;
import static oh.CSGPropertyType.SCHEDULE_BOUNDARIES_STARTING_DATE;
import static oh.CSGPropertyType.SCHEDULE_TYPE_COMBO;
import static oh.CSGPropertyType.START_TIME_FILTER;
import oh.data.LectureData;
import oh.data.MTrecitationLab;
import oh.data.CSGData;
import oh.data.TAType;
import oh.data.TeachingAssistantPrototype;
import oh.data.TimeSlot;
import oh.data.TimeSlot.DayOfWeek;
import oh.transactions.AddTA_Transaction;
import oh.transactions.CutTA_Transaction;
import oh.transactions.EditTA_Transaction;
import oh.transactions.ToggleOfficeHours_Transaction;
import oh.workspace.CSGWorkSpace;
import oh.workspace.dialogs.TADialog;
import properties_manager.PropertiesManager;
import static oh.data.CSGData.faviconPath;
import static oh.data.CSGData.navbarPath;
import static oh.data.CSGData.leftFooterPath;
import static oh.data.CSGData.rightFooterPath;
import oh.data.ScheduleData;
import oh.transactions.AddLab_Transaction;
import oh.transactions.AddLecture_Transaction;
import oh.transactions.AddRecitation_Transaction;
import oh.transactions.AddSchedule_Transaction;
import oh.transactions.CheckBox_Transaction;
import oh.transactions.ComboBox_Transaction;
import oh.transactions.EditLab_Transaction;
import oh.transactions.EditLecture_Transaction;
import oh.transactions.EditRecitation_Transaction;
import oh.transactions.EditSchedule_Transaction;
import oh.transactions.ImageView_Transaction;
import oh.transactions.RemoveLab_Transaction;
import oh.transactions.RemoveLecture_Transaction;
import oh.transactions.RemoveRecitation_Transaction;
import oh.transactions.RemoveSchedule_Transaction;
import oh.transactions.WriteTextArea_Transaction;
import oh.transactions.WriteTextField_Transaction;

/**
 *
 * @author Kris Pan
 */
public class CSGController {
    CSGApp app;

    public CSGController(CSGApp initApp) {
        app = initApp;
    }
    
    public void processComboBox(ComboBox box, String oldValue, String newValue){
        ComboBox_Transaction transaction = new ComboBox_Transaction(box, oldValue, newValue);
        app.processTransaction(transaction);
    }
    
    public void processCheckBox(CheckBox box, Boolean isChecked){
        CheckBox_Transaction transaction = new CheckBox_Transaction(box, isChecked);
        app.processTransaction(transaction);
    }
    
    public void processLogoButton(ImageView im, int i){
        
        final FileChooser f = new FileChooser();
        File path = f.showOpenDialog(new Stage());
        if(path!=null){
        String newPath = ".\\images\\"+path.toString().substring(path.toString().lastIndexOf("\\")+1);
        String oldPath = "";
        if (i == 1){
            oldPath = faviconPath;
            faviconPath = newPath;
        }
        else if (i == 2){
            oldPath = navbarPath;
            navbarPath = newPath;
        }
        else if (i == 3){
            oldPath = leftFooterPath;
            leftFooterPath = newPath;
        }
        else if (i == 4){
            oldPath = rightFooterPath;
            rightFooterPath = newPath;
        }        
   
        ImageView_Transaction transaction = new ImageView_Transaction(im, oldPath, newPath);
        app.processTransaction(transaction);
            
        }
    }
    
    public void processWriteTextArea(TextArea text, String oldValue, String newValue){
        CSGData data = (CSGData) app.getDataComponent();
        WriteTextArea_Transaction writeAreaTransaction = new WriteTextArea_Transaction(data,text,oldValue,newValue);
        app.processTransaction(writeAreaTransaction);
        
        
    }
    public void processWriteTextField(TextField text, String oldValue, String newValue){
        CSGData data = (CSGData) app.getDataComponent();
        WriteTextField_Transaction writeFieldTransaction = new WriteTextField_Transaction(data,text,oldValue,newValue);
        app.processTransaction(writeFieldTransaction);

    }
    
    public void processAddTA() {
        AppGUIModule gui = app.getGUIModule();
        TextField nameTF = (TextField) gui.getGUINode(OH_NAME_TEXT_FIELD);
        String name = nameTF.getText();
        TextField emailTF = (TextField) gui.getGUINode(OH_EMAIL_TEXT_FIELD);
        String email = emailTF.getText();
        CSGData data = (CSGData) app.getDataComponent();
        TAType type = data.getSelectedType();
        if (data.isLegalNewTA(name, email)) {
            TeachingAssistantPrototype ta = new TeachingAssistantPrototype(name.trim(), email.trim(), type);
            AddTA_Transaction addTATransaction = new AddTA_Transaction(data, ta);
            app.processTransaction(addTATransaction);

            // NOW CLEAR THE TEXT FIELDS
            nameTF.setText("");
            emailTF.setText("");
            nameTF.requestFocus();
        }
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    public void processAddLecture(){     
        CSGData data = (CSGData) app.getDataComponent();
        LectureData lecture = new LectureData("?","?","?","?");
        AddLecture_Transaction transaction = new AddLecture_Transaction(data, lecture);
        app.processTransaction(transaction);
    }
    
    public void processEditLecture(String newValue, int columnIndex){
        CSGData data = (CSGData) app.getDataComponent();
        LectureData lecToEdit = data.getSelectedLecture();
        EditLecture_Transaction transaction = new EditLecture_Transaction(data,lecToEdit,
                newValue, columnIndex);
        app.processTransaction(transaction);    
    }
    
    public void processRemoveLecture(){
        CSGData data = (CSGData) app.getDataComponent();
        if(data.isLectureSelected()){
            LectureData lectureToRemove = data.getSelectedLecture();
            RemoveLecture_Transaction transaction = new RemoveLecture_Transaction(data, lectureToRemove);
            app.processTransaction(transaction); 
        }
    }
    
    
    public void processAddRecitation(){     
        CSGData data = (CSGData) app.getDataComponent();
        MTrecitationLab rec = new MTrecitationLab("?","?","?","?","?");
        AddRecitation_Transaction transaction = new AddRecitation_Transaction(data, rec);
        app.processTransaction(transaction);
    }
    
    public void processEditRecitation(String newValue, int columnIndex){
        CSGData data = (CSGData) app.getDataComponent();
        MTrecitationLab recitationToEdit = data.getSelectedRecitation();
        EditRecitation_Transaction transaction = new EditRecitation_Transaction(data,recitationToEdit,
                newValue, columnIndex);
        app.processTransaction(transaction);    
    }
    
    public void processRemoveRecitation(){
        CSGData data = (CSGData) app.getDataComponent();
        if(data.isRecitationSelected()){
            MTrecitationLab recitationToRemove = data.getSelectedRecitation();
            RemoveRecitation_Transaction transaction = new RemoveRecitation_Transaction(data, recitationToRemove);
            app.processTransaction(transaction); 
        }
    }
    
    public void processAddLab(){     
        CSGData data = (CSGData) app.getDataComponent();
        MTrecitationLab lab = new MTrecitationLab("?","?","?","?","?");
        AddLab_Transaction transaction = new AddLab_Transaction(data, lab);
        app.processTransaction(transaction);
    }
    public void processEditLab(String newValue, int columnIndex){
        CSGData data = (CSGData) app.getDataComponent();
        MTrecitationLab labToEdit = data.getSelectedLab();
        EditLab_Transaction transaction = new EditLab_Transaction(data,labToEdit,
                newValue, columnIndex);
        app.processTransaction(transaction);    
    }
    
    public void processRemoveLab(){
        CSGData data = (CSGData) app.getDataComponent();
        if(data.isLabSelected()){
            MTrecitationLab labToRemove = data.getSelectedLab();
            RemoveLab_Transaction transaction = new RemoveLab_Transaction(data, labToRemove);
            app.processTransaction(transaction); 
        }
    }
        
    

    public void processVerifyTA() {

    }

    public void processToggleOfficeHours() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        ObservableList<TablePosition> selectedCells = officeHoursTableView.getSelectionModel().getSelectedCells();
        if (selectedCells.size() > 0) {
            TablePosition cell = selectedCells.get(0);
            int cellColumnNumber = cell.getColumn();
            CSGData data = (CSGData)app.getDataComponent();
            if (data.isDayOfWeekColumn(cellColumnNumber)) {
                DayOfWeek dow = data.getColumnDayOfWeek(cellColumnNumber);
                TableView<TeachingAssistantPrototype> taTableView = (TableView)gui.getGUINode(OH_TAS_TABLE_VIEW);
                TeachingAssistantPrototype ta = taTableView.getSelectionModel().getSelectedItem();
                if (ta != null) {
                    TimeSlot timeSlot = officeHoursTableView.getSelectionModel().getSelectedItem();
                    ToggleOfficeHours_Transaction transaction = new ToggleOfficeHours_Transaction(data, timeSlot, dow, ta);
                    app.processTransaction(transaction);
                }
                else {
                    Stage window = app.getGUIModule().getWindow();
                    AppDialogsFacade.showMessageDialog(window, OH_NO_TA_SELECTED_TITLE, OH_NO_TA_SELECTED_CONTENT);
                }
            }
            int row = cell.getRow();
            cell.getTableView().refresh();
        }
    }

    public void processTimeFilter(){
        AppGUIModule gui = app.getGUIModule();
        CSGData data = (CSGData) app.getDataComponent();
        ComboBox startTime = (ComboBox) gui.getGUINode(START_TIME_FILTER);
        int startTimeInt;
        String startTimeString = startTime.getValue().toString();
        if(startTime.getValue().toString().length() ==6){
            startTimeInt = Integer.parseInt(startTime.getValue().toString().substring(0, 1));
            if(startTimeString.contains("pm")){
                startTimeInt += 12;
            }
            else{
            }
        }
        else{
            startTimeInt = Integer.parseInt(startTime.getValue().toString().substring(0, 2));
            if(startTimeString.contains("pm")){
                startTimeInt += 12;
            }
        }
               
        ComboBox endTime = (ComboBox) gui.getGUINode(END_TIME_FILTER);
        int endTimeInt;
        String endTimeString = endTime.getValue().toString();
        if(endTime.getValue().toString().length() ==6){
            endTimeInt = Integer.parseInt(endTime.getValue().toString().substring(0, 1));
            if(endTimeString.contains("pm")){
                endTimeInt += 12;
            }
            else{
            }
        }
        else{
            endTimeInt = Integer.parseInt(endTime.getValue().toString().substring(0, 2));
        }
        data.filterOHTime(startTimeInt, endTimeInt);
    }
    public void processTypeTA() {
        app.getFoolproofModule().updateControls(OH_FOOLPROOF_SETTINGS);
    }
    
    public void processRemoveTA(){
        CSGData data = (CSGData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype clipboardCutTA = data.getSelectedTA();
            TeachingAssistantPrototype clipboardCopiedTA = null;
            HashMap<TimeSlot, ArrayList<DayOfWeek>> officeHours = data.getTATimeSlots(clipboardCutTA);
            CutTA_Transaction transaction = new CutTA_Transaction((CSGApp)app, clipboardCutTA, officeHours);
            app.processTransaction(transaction);
        }
    }
    
    public void processEditTA() {
        CSGData data = (CSGData)app.getDataComponent();
        if (data.isTASelected()) {
            TeachingAssistantPrototype taToEdit = data.getSelectedTA();
            TADialog taDialog = (TADialog)app.getGUIModule().getDialog(OH_TA_EDIT_DIALOG);
            taDialog.showEditDialog(taToEdit);
            TeachingAssistantPrototype editTA = taDialog.getEditTA();
            if (editTA != null) {
                EditTA_Transaction transaction = new EditTA_Transaction(taToEdit, editTA.getName(), editTA.getEmail(), editTA.getType());
                app.processTransaction(transaction);
            }
        }
    }

    public void processSelectAllTAs() {
        CSGData data = (CSGData)app.getDataComponent();
        data.selectTAs(TAType.All);
    }

    public void processSelectGradTAs() {
        CSGData data = (CSGData)app.getDataComponent();
        data.selectTAs(TAType.Graduate);
    }

    public void processSelectUndergradTAs() {
        CSGData data = (CSGData)app.getDataComponent();
        data.selectTAs(TAType.Undergraduate);
    }

    public void processSelectTA() {
        AppGUIModule gui = app.getGUIModule();
        TableView<TimeSlot> officeHoursTableView = (TableView) gui.getGUINode(OH_OFFICE_HOURS_TABLE_VIEW);
        officeHoursTableView.refresh();
    }
    
    public void processLimitEndDate() {
        AppGUIModule gui = app.getGUIModule();
        DatePicker mondayPicker  = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_STARTING_DATE); 
        DatePicker fridayPicker  = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_ENDING_DATE);
        final Callback<DatePicker, DateCell> dayCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                           
                            if (item.isBefore(
                                    mondayPicker.getValue().plusDays(1))
                                ) {
                                    setDisable(true);
                            }   
                            else if (item.getDayOfWeek() == java.time.DayOfWeek.TUESDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.WEDNESDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.THURSDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.MONDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.SATURDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.SUNDAY){
                                    setDisable(true);

                            }
                    }
                };
            }
        };
        fridayPicker.setDayCellFactory(dayCellFactory);
        limitAddEditDate();
    }
    
    public void processLimitStartDate() {
        AppGUIModule gui = app.getGUIModule();
        DatePicker mondayPicker  = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_STARTING_DATE); 
        DatePicker fridayPicker  = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_ENDING_DATE);
        final Callback<DatePicker, DateCell> dayCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                           
                            if (item.isAfter(
                                    fridayPicker.getValue().minusDays(1))
                                ) {
                                    setDisable(true);
                            }   
                            else if (item.getDayOfWeek() == java.time.DayOfWeek.TUESDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.WEDNESDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.THURSDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.FRIDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.SATURDAY ||
                    item.getDayOfWeek() == java.time.DayOfWeek.SUNDAY){
                                    setDisable(true);

                            }
                    }
                };
            }
        };
        mondayPicker.setDayCellFactory(dayCellFactory);
        limitAddEditDate();
        
    }
    
    public void limitAddEditDate(){
        AppGUIModule gui = app.getGUIModule();
        DatePicker mondayPicker  = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_STARTING_DATE); 
        DatePicker fridayPicker  = (DatePicker) gui.getGUINode(SCHEDULE_BOUNDARIES_ENDING_DATE);
        DatePicker addEditDate  = (DatePicker) gui.getGUINode(SCHEDULE_ADD_EDIT_DATE_PICK);
                final Callback<DatePicker, DateCell> dayCellFactory = 
            new Callback<DatePicker, DateCell>() {
                @Override
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            super.updateItem(item, empty);
                           
                            if (item.isBefore(
                                    mondayPicker.getValue().plusDays(1))
                                ) {
                                    setDisable(true);
                            }   
                            else if (item.isAfter(
                                    fridayPicker.getValue().minusDays(1))
                                ) {
                                    setDisable(true);
                            }   
                    }
                };
            }
        };
        addEditDate.setDayCellFactory(dayCellFactory);
        
    }
    public void processAddSchedule() {
        try{
        AppGUIModule gui = app.getGUIModule();
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCHEDULE_TYPE_COMBO);
        String type = typeCombo.getValue().toString();
        DatePicker addEditDate  = (DatePicker) gui.getGUINode(SCHEDULE_ADD_EDIT_DATE_PICK);
        String days = addEditDate.getValue().toString();
        TextField titleField = (TextField) gui.getGUINode(SCHEDULE_ADD_EDIT_TITLE_INPUT);
        String title = titleField.getText();
        TextField topicField = (TextField) gui.getGUINode(SCHEDULE_ADD_EDIT_TOPIC_INPUT);
        String topic = topicField.getText();
        TextField linkField = (TextField) gui.getGUINode(SCHEDULE_ADD_EDIT_LINK_INPUT);
        String link = linkField.getText();
        CSGData data = (CSGData) app.getDataComponent();
        ScheduleData sc = new ScheduleData(type,days,title,topic,link);
        AddSchedule_Transaction transaction = new AddSchedule_Transaction(data, sc);
        app.processTransaction(transaction);  
        typeCombo.setValue("");
        addEditDate.setValue(null);
        titleField.setText(null);
        topicField.setText("");
        linkField.setText("");
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Cannot Add: Make sure Type and Date is selected", 
                "Error Message", JOptionPane.INFORMATION_MESSAGE);
        }
      
}   
    public void processScheduleButtonUpdate(){
        CSGData data = (CSGData) app.getDataComponent();
        AppGUIModule gui = app.getGUIModule();
        Button addButton = (Button) gui.getGUINode(SCHEDULE_ADD_BUTTON);
        if(data.isScheduleSelected() && addButton.getText().equals("Add")){
            Button button = (Button) gui.getGUINode(SCHEDULE_ADD_BUTTON);
            button.setText("Update");
        }
        if(data.isScheduleSelected() && addButton.getText().equals("Addoo")){
            Button button = (Button) gui.getGUINode(SCHEDULE_ADD_BUTTON);
            button.setText("Updateroo");
        }
    }
    
    public void processEditSchedule(){
        try{
        CSGData data = (CSGData) app.getDataComponent();
        ScheduleData scheduleToEdit = data.getSelectedSchedule();
        AppGUIModule gui = app.getGUIModule();
        Button addButton = (Button) gui.getGUINode(SCHEDULE_ADD_BUTTON);
        ComboBox typeCombo = (ComboBox) gui.getGUINode(SCHEDULE_TYPE_COMBO);
        String type = typeCombo.getValue().toString();
        DatePicker addEditDate  = (DatePicker) gui.getGUINode(SCHEDULE_ADD_EDIT_DATE_PICK);
        String days = addEditDate.getValue().toString();
        TextField titleField = (TextField) gui.getGUINode(SCHEDULE_ADD_EDIT_TITLE_INPUT);
        String title = titleField.getText();
        TextField topicField = (TextField) gui.getGUINode(SCHEDULE_ADD_EDIT_TOPIC_INPUT);
        String topic = topicField.getText();
        TextField linkField = (TextField) gui.getGUINode(SCHEDULE_ADD_EDIT_LINK_INPUT);
        String link = linkField.getText();
        EditSchedule_Transaction transaction = new EditSchedule_Transaction(data,scheduleToEdit,
                type,days,title,topic,link );
        app.processTransaction(transaction);  
        typeCombo.setValue("");
        addEditDate.setValue(null);
        titleField.setText(null);
        topicField.setText("");
        linkField.setText("");
        if(addButton.getText().equals("Update")){
            addButton.setText("Add");
                }
        else if(addButton.getText().equals("Updateroo")) 
            addButton.setText("Addoo");
                }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Cannot Update: Make sure Type and Date is selected", 
                "Error Message", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    
    public void processRemoveSchedule(){
        CSGData data = (CSGData) app.getDataComponent();      
        if(data.isScheduleSelected()){
            ScheduleData scheduleToRemove = data.getSelectedSchedule();
            RemoveSchedule_Transaction transaction = new RemoveSchedule_Transaction(data, scheduleToRemove);
            app.processTransaction(transaction); 
        }
    }
}