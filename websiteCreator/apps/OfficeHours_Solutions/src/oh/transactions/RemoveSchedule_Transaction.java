package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CSGData;
import oh.data.ScheduleData;


/**
 *
 * @author Kris Pan
 */
public class RemoveSchedule_Transaction implements jTPS_Transaction {
    CSGData data;
    ScheduleData schedule;
    
    public RemoveSchedule_Transaction(CSGData initData, ScheduleData initSchedule) {
        data = initData;
        schedule = initSchedule;
    }

    @Override
    public void doTransaction() {
        data.removeSchedule(schedule);    
    }

    @Override
    public void undoTransaction() {
        data.addSchedule(schedule);
    }
}
