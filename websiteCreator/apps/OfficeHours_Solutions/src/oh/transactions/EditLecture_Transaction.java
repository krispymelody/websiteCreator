package oh.transactions;


import djf.modules.AppGUIModule;
import javafx.scene.control.TableView;
import jtps.jTPS_Transaction;
import oh.CSGApp;
import oh.data.LectureData;
import oh.data.MTrecitationLab;
import oh.data.CSGData;

/**
 *
 * @author Kris Pan
 */
public class EditLecture_Transaction implements jTPS_Transaction {
    LectureData lecture;
    String oldSection;
    String oldDays;
    String oldTime;
    String oldRoom;
    String newValue;
    int columnIndex;
    CSGData data;

    
    public EditLecture_Transaction(CSGData initData, LectureData initLec, String initNewValue
    ,int initIndex) {
        data = initData;
        lecture = initLec;
        oldSection = initLec.getSection();
        oldDays = initLec.getDays();
        oldTime = initLec.getTime();
        oldRoom = initLec.getRoom();
        newValue= initNewValue;
        columnIndex = initIndex;
        
    }


    @Override
    public void doTransaction() {
        switch (columnIndex){
            case 1: columnIndex = 1;
            lecture.setSection(newValue);
            break;
            
            case 2: columnIndex = 2;
            lecture.setDays(newValue);
            break;
        
            case 3: columnIndex = 3;
            lecture.setTime(newValue);
            break;
            
            case 4: columnIndex = 4;
            lecture.setRoom(newValue);
            break;
            
        }
        data.resetLecture();
    }

    @Override
    public void undoTransaction() {
        switch (columnIndex){
            case 1: columnIndex = 1;
            lecture.setSection(oldSection);
            break;
                        
            case 2: columnIndex = 2;
            lecture.setDays(oldDays);
            break;
        
            case 3: columnIndex = 3;
            lecture.setTime(oldTime);
            break;
            
            case 4: columnIndex = 4;
            lecture.setRoom(oldRoom);
            break;
            
        }
        data.resetLecture();
    }

}
