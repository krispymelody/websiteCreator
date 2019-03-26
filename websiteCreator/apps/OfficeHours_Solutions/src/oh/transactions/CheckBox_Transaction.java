package oh.transactions;

import javafx.scene.control.CheckBox;
import jtps.jTPS_Transaction;

/**
 *
 * @author Kris Pan
 */
public class CheckBox_Transaction implements jTPS_Transaction {
    CheckBox box;
    Boolean isChecked;

    
    public CheckBox_Transaction(CheckBox initBox, 
            Boolean initCheck) {
        box = initBox;
        isChecked = initCheck;

    }


    @Override
    public void doTransaction() {
        box.setSelected(isChecked);
    }

    @Override
    public void undoTransaction() {
        box.setSelected(!isChecked);
    }
}