package oh.transactions;

import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jtps.jTPS_Transaction;

/**
 *
 * @author Kris Pan
 */
public class ImageView_Transaction implements jTPS_Transaction {
    ImageView im;
    String oldPath, newPath;

    
    public ImageView_Transaction(ImageView image, String initOldPath,
            String initNewPath) {
        im = image;
        oldPath = initOldPath;
        newPath = initNewPath;

    }


    @Override
    public void doTransaction() {       
        Image insert = new Image("file:"+newPath);
        im.setImage(insert);
    }

    @Override
    public void undoTransaction() {
        Image insert = new Image("file:"+oldPath);
        im.setImage(insert);
        
    }
}