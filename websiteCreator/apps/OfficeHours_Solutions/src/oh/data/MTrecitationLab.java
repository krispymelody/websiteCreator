package oh.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Kris Pan
 */
public class MTrecitationLab{
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty section;
    private final StringProperty dayTime;
    private final StringProperty room;
    private final StringProperty ta1;
    private final StringProperty ta2;
    
    /**
     * Constructor initializes both the TA name and email.
     */
    public MTrecitationLab(String initSection, String initDayTime, String initRoom, 
            String initTA1, String initTA2) {
        section = new SimpleStringProperty(initSection);
        dayTime = new SimpleStringProperty(initDayTime);
        room = new SimpleStringProperty(initRoom);
        ta1 = new SimpleStringProperty(initTA1);
        ta2 = new SimpleStringProperty(initTA2);
    }


    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    public String getSection() {
        return section.get();
    }

    public void setSection(String initSection) {
        section.set(initSection);        
    }
    
    public String getDayTime() {
        return dayTime.get();
    }

    public void setDayTime(String initDayTime) {
        dayTime.set(initDayTime);
    }

    public String getRoom() {
        return room.get();
    }

    public void setRoom(String initRoom) {
        room.set(initRoom);
    }

    public String getTa1() {
        return ta1.get();
    }

    public void setTa1(String initTa1) {
        ta1.set(initTa1);
    }

    public String getTa2() {
        return ta2.get();
    }

    public void setTa2(String initTa2) {
        ta2.set(initTa2);
    }


}