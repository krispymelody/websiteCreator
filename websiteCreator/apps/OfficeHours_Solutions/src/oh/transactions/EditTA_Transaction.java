package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.TeachingAssistantPrototype;

/**
 *
 * @author Kris Pan
 */
public class EditTA_Transaction implements jTPS_Transaction {
    TeachingAssistantPrototype taToEdit;
    String oldName, newName;
    String oldEmail, newEmail;
    String oldType, newType;
    
    public EditTA_Transaction(TeachingAssistantPrototype initTAToEdit, 
            String name, String email, String type) {
        taToEdit = initTAToEdit;
        oldName = initTAToEdit.getName();
        oldEmail = initTAToEdit.getEmail();
        oldType = initTAToEdit.getType();
        newName = name;
        newEmail = email;
        newType = type;
    }


    @Override
    public void doTransaction() {
        taToEdit.setName(newName);
        taToEdit.setEmail(newEmail);
        taToEdit.setType(newType);
    }

    @Override
    public void undoTransaction() {
        taToEdit.setName(oldName);
        taToEdit.setEmail(oldEmail);
        taToEdit.setType(oldType);
    }
}