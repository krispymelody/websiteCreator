package oh.transactions;


import djf.modules.AppGUIModule;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;
import oh.CSGApp;
import oh.data.MTrecitationLab;
import oh.data.CSGData;

/**
 *
 * @author Kris Pan
 */
public class EditRecitation_Transaction implements jTPS_Transaction {
    MTrecitationLab recitation;
    String oldSection;
    String oldDayTime;
    String oldRoom;
    String oldTA1;
    String oldTA2;
    String newValue;
    int columnIndex;
    CSGData data;

    
    public EditRecitation_Transaction(CSGData initData, MTrecitationLab initRecit, String initNewValue
    ,int initIndex) {
        data = initData;
        recitation = initRecit;
        oldSection = initRecit.getSection();
        oldDayTime = initRecit.getDayTime();
        oldRoom = initRecit.getRoom();
        oldTA1 = initRecit.getTa1();
        oldTA2 = initRecit.getTa2();
        newValue= initNewValue;
        columnIndex = initIndex;
        
    }


    @Override
    public void doTransaction() {
        switch (columnIndex){
            case 1: columnIndex = 1;
            recitation.setSection(newValue);
            break;
            
            case 2: columnIndex = 2;
            recitation.setDayTime(newValue);
            break;
        
            case 3: columnIndex = 3;
            recitation.setRoom(newValue);
            break;
            
            case 4: columnIndex = 4;
            recitation.setTa1(newValue);
            break;
            
            case 5: columnIndex = 5;
            recitation.setTa2(newValue);
            break;
        }
        data.resetRecitation();
    }

    @Override
    public void undoTransaction() {
        switch (columnIndex){
            case 1: columnIndex = 1;
        recitation.setSection(oldSection);
            break;
                        
            case 2: columnIndex = 2;
            recitation.setDayTime(oldDayTime);
            break;
        
            case 3: columnIndex = 3;
            recitation.setRoom(oldRoom);
            break;
            
            case 4: columnIndex = 4;
            recitation.setTa1(oldTA1);
            break;
            
            case 5: columnIndex = 5;
            recitation.setTa2(oldTA2);
            break;
        }
        data.resetRecitation();
    }

}
