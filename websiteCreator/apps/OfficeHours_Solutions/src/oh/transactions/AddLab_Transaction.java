package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.MTrecitationLab;
import oh.data.CSGData;

/**
 *
 * @author Kris Pan
 */
public class AddLab_Transaction implements jTPS_Transaction {
    CSGData data;
    MTrecitationLab lab;
    
        public AddLab_Transaction(CSGData initData, MTrecitationLab initLab) {
        data = initData;
        lab = initLab;
    }


    @Override
    public void doTransaction() {
        data.addLab(lab);        
    }

    @Override
    public void undoTransaction() {
        data.removeLab(lab);
    }
}
