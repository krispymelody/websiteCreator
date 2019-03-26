package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CSGData;
import oh.data.ScheduleData;


/**
 *
 * @author Kris Pan
 */
public class EditSchedule_Transaction implements jTPS_Transaction {
    CSGData data;
    ScheduleData schedule;
    String type,date,title,topic,link;
    String oldType,oldDate,oldTitle,oldTopic,oldLink;
    
    
    public EditSchedule_Transaction(CSGData initData, ScheduleData initSchedule,
            String initType, String initDate, String initTitle, String initTopic, String initLink) {
        data = initData;
        schedule = initSchedule;
        type = initType;
        date = initDate;
        title = initTitle;
        topic = initTopic;
        link = initLink;
        oldType = initSchedule.getType();
        oldDate = initSchedule.getDate();
        oldTitle = initSchedule.getTitle();
        oldTopic = initSchedule.getTopic();
        oldLink = initSchedule.getLink();
    }

    @Override
    public void doTransaction() {
        schedule.setType(type);    
        schedule.setDate(date);
        schedule.setTitle(title);
        schedule.setTopic(topic);
        schedule.setLink(link);
        data.resetSchedule();
    }

    @Override
    public void undoTransaction() {
        schedule.setType(oldType);    
        schedule.setDate(oldDate);
        schedule.setTitle(oldTitle);
        schedule.setTopic(oldTopic);
        schedule.setLink(oldLink);
        data.resetSchedule();
    }
}
