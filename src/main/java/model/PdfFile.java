package model;

import java.io.File;

public class PdfFile extends File {
    private String password;

    public PdfFile(String pathname, String password) {
        super(pathname);
        this.password = password;

    }

    public String getPathname(){
        return super.getPath();
    }

    public String getPassword() {
        return password;
    }
}
