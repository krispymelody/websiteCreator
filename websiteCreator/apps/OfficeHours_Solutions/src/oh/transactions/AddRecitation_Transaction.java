package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.MTrecitationLab;
import oh.data.CSGData;

/**
 *
 * @author Kris Pan
 */
public class AddRecitation_Transaction implements jTPS_Transaction {
    CSGData data;
    MTrecitationLab recitation;
    
        public AddRecitation_Transaction(CSGData initData, MTrecitationLab initRec) {
        data = initData;
        recitation = initRec;
    }


    @Override
    public void doTransaction() {
        data.addRecitation(recitation);        
    }

    @Override
    public void undoTransaction() {
        data.removeRecitation(recitation);
    }
}
