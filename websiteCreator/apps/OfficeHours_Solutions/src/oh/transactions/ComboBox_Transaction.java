package oh.transactions;

import javafx.scene.control.ComboBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author Kris Pan
 */
public class ComboBox_Transaction implements jTPS_Transaction {
    ComboBox box;
    String oldValue, newValue;

    
    public ComboBox_Transaction(ComboBox initBox, 
            String initOld, String initNew) {
        box = initBox;
        oldValue = initOld;
        newValue = initNew;

    }


    @Override
    public void doTransaction() {
        box.setValue(newValue);
    }

    @Override
    public void undoTransaction() {
        box.setValue(oldValue);

        
    }
}