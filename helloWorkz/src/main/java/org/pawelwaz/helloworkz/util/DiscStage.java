/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pawelwaz.helloworkz.util;

import javafx.stage.Stage;
import org.pawelwaz.helloworkz.controller.DiscussionWindowController;
import org.pawelwaz.helloworkz.controller.MessageWindowController;

/**
 *
 * @author A20111
 */
public class DiscStage extends Stage {
    
    private DiscussionWindowController controller;
    
    public DiscussionWindowController getController() {
        return this.controller;
    }
    
    public void setController(DiscussionWindowController controller) {
        this.controller = controller;
    }
    
}
