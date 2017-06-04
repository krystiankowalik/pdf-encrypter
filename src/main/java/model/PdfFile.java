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
    public PdfFile(String pathname) {
        //super(pathname);
        this.pathname = new SimpleStringProperty(pathname);
        this.password = new SimpleStringProperty("");

    }


    public String getPassword() {
        return password.getValueSafe();
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getPathname() {
        return pathname.getValueSafe();
    }

    public StringProperty pathnameProperty() {
        return pathname;
    }

    @Override
    public String toString() {
        return "PdfFile{" +
                "password=" + password.getValueSafe() +
                ", pathname=" + pathname.getValueSafe() +
                '}';
    }
}
