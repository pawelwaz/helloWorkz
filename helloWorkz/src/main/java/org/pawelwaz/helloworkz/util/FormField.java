package org.pawelwaz.helloworkz.util;

/**
 *
 * @author pawelwaz
 */
public class FormField {
    
    private String name;
    private FieldType fieldType;
    private String content;
    private boolean obligatory;
    private int max;
    
    public FormField(String name, FieldType fieldType, String content, boolean obligatory) {
        this.name = name;
        this.fieldType = fieldType;
        this.content = content;
        this.obligatory = obligatory;
        switch(this.fieldType) {
            case PASSWORD: this.max = 16; break;
            case LOGIN: this.max = 16; break;
            case TINYFIELD: this.max = 16; break;
            case SHORTFIELD: this.max = 32; break;
            case MEDIUMFIELD: this.max = 64; break;
            case LONGFIELD: this.max = 128; break;
            default: this.max = 16;
        }
    }
    
    public boolean isValid() {
        if(this.obligatory && content.length() == 0) {
            HelloUI.showError("Pole '" + this.name + "' nie może być puste");
            return false;
        }
        else if(this.content.length() > this.max) {
            HelloUI.showError("Wartość pola '" + this.name + "' jest zbyt długa. Maksymalna ilość znaków to " + this.max);
            return false;
        }
        else return true;
    }
    
}
