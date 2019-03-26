package oh.transactions;

import javafx.scene.control.TextField;
import jtps.jTPS_Transaction;
import oh.data.CSGData;

/**
 *
 * @author Kris Pan
 */
public class WriteTextField_Transaction implements jTPS_Transaction {
    CSGData data;
    TextField text;
    String oldValue;
    String newValue;
    
    public WriteTextField_Transaction(CSGData initData, TextField initText, String initOldValue,
            String initNewValue) {
        data = initData;
        text = initText;
        oldValue = initOldValue;
        newValue = initNewValue;
    }

    @Override
    public void doTransaction() {
        data.setTextField(text, newValue);        
    }
    
    @Override
    public void undoTransaction() {
        data.setTextField(text, oldValue); 
    }
}
