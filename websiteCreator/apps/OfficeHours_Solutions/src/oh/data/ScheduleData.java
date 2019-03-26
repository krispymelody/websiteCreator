package oh.data;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Kris Pan
 */
public class ScheduleData{
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty type;
    private final StringProperty date;
    private final StringProperty title;
    private final StringProperty topic;
    private final StringProperty link;
    
    /**
     * Constructor initializes both the TA name and email.
     */
    public ScheduleData(String initType, String initDate, String initTitle, 
            String initTopic, String initLink) {
        type = new SimpleStringProperty(initType);
        date = new SimpleStringProperty(initDate);
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
        link = new SimpleStringProperty(initLink);
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    public String getType() {
        return type.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getTopic() {
        return topic.get();
    }
    
    public String getLink(){
        return link.get();
    }

    public void setType(String initType) {
        type.set(initType);
    }

    public void setDate(String initDate) {
        date.set(initDate);
    }

    public void setTitle(String initTitle) {
        title.set(initTitle);
    }

    public void setTopic(String initTopic) {
        topic.set(initTopic);
    }
    
    public void setLink(String initLink){
        link.set(initLink);
    }

}