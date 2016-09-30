package org.pawelwaz.helloworkz.util;

/**
 *
 * @author pawelwaz
 */
public class HelloForm {
    
    private FormField[] fields;
    
    public HelloForm(FormField[] fields) {
        this.fields = fields;
    }
    
    public boolean isValid() {
        for(int i = 0; i < this.fields.length; i++) {
            if(!fields[i].isValid()) {
                return false;
            }
        }
        return true;
    }
    
}
