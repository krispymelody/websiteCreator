package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.CSGData;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author Kris Pan
 */
public class AddTA_Transaction implements jTPS_Transaction {
    CSGData data;
    TeachingAssistantPrototype ta;
    
    public AddTA_Transaction(CSGData initData, TeachingAssistantPrototype initTA) {
        data = initData;
        ta = initTA;
    }

    @Override
    public void doTransaction() {
        data.addTA(ta);        
    }

    @Override
    public void undoTransaction() {
        data.removeTA(ta);
    }
}
