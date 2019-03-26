package oh.transactions;

import jtps.jTPS_Transaction;
import oh.data.LectureData;
import oh.data.CSGData;

/**
 *
 * @author Kris Pan
 */
public class RemoveLecture_Transaction implements jTPS_Transaction {
    CSGData data;
    LectureData lecture;
    
        public RemoveLecture_Transaction(CSGData initData, LectureData initLecture) {
        data = initData;
        lecture = initLecture;
    }


    @Override
    public void doTransaction() {
        data.removeLecture(lecture);        
    }

    @Override
    public void undoTransaction() {
        data.addLecture(lecture);
    }
}
