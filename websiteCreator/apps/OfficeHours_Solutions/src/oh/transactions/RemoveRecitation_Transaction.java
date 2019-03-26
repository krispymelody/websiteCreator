package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.MTrecitationLab;
import oh.data.CSGData;

/**
 *
 * @author Kris Pan
 */
public class RemoveRecitation_Transaction implements jTPS_Transaction {
    CSGData data;
    MTrecitationLab recitation;
    
        public RemoveRecitation_Transaction(CSGData initData, MTrecitationLab initRec) {
        data = initData;
        recitation = initRec;
    }


    @Override
    public void doTransaction() {      
        data.removeRecitation(recitation);
    }

    @Override
    public void undoTransaction() {
        data.addRecitation(recitation);   
    }
}
