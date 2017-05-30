package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PdfFile {
    private StringProperty password;
    private StringProperty pathname;

    public PdfFile(String pathname, StringProperty password) {
        //super(pathname);
        this.pathname = new SimpleStringProperty(pathname);
        this.password = (password);

    }

    public String getPassword() {
        return password.get();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getPathname() {
        return pathname.get();
    }

    public StringProperty pathnameProperty() {
        return pathname;
    }
}
