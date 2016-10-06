package org.pawelwaz.helloworkz.util;

import javafx.stage.Stage;
import org.pawelwaz.helloworkz.controller.MessageWindowController;

/**
 *
 * @author pawelwaz
 */
public class MsgStage extends Stage {
    
    private MessageWindowController controller;
    
    public MessageWindowController getController() {
        return this.controller;
    }
    
    public void setController(MessageWindowController controller) {
        this.controller = controller;
    }
    
}
