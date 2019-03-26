package oh.transactions;

import javafx.scene.control.TextArea;
import jtps.jTPS_Transaction;
import oh.data.CSGData;

/**
 *
 * @author Kris Pan
 */
public class WriteTextArea_Transaction implements jTPS_Transaction {
    CSGData data;
    TextArea text;
    String oldValue;
    String newValue;
    
    public WriteTextArea_Transaction(CSGData initData, TextArea initText, String initOldValue,
            String initNewValue) {
        data = initData;
        text = initText;
        oldValue = initOldValue;
        newValue = initNewValue;
    }

    @Override
    public void doTransaction() {
        data.setTextArea(text, newValue);        
    }
    
    @Override
    public void undoTransaction() {
        data.setTextArea(text, oldValue); 
    }
}
