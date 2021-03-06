package oh.transactions;

import jtps.jTPS_Transaction;
import oh.CSGApp;
import oh.data.CSGData;
import oh.data.TeachingAssistantPrototype;

public class PasteTA_Transaction implements jTPS_Transaction {
    CSGApp app;
    TeachingAssistantPrototype taToPaste;

    public PasteTA_Transaction(  CSGApp initApp, 
                                 TeachingAssistantPrototype initTAToPaste) {
        app = initApp;
        taToPaste = initTAToPaste;
    }

    @Override
    public void doTransaction() {
        CSGData data = (CSGData)app.getDataComponent();
        data.addTA(taToPaste);
    }

    @Override
    public void undoTransaction() {
        CSGData data = (CSGData)app.getDataComponent();
        data.removeTA(taToPaste);
    }   
}