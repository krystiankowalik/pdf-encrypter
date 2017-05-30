package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class PdfFile {
    private StringProperty password;
    private StringProperty pathName;

    public PdfFile(String pathName, String password) {
        //super(pathName);
        this.pathName = new SimpleStringProperty(pathName);
        this.password = new SimpleStringProperty(password);

    }

    public StringProperty getPathname(){
        return this.pathName;
    }

    public StringProperty getPassword() {
        return this.password;
    }
}
