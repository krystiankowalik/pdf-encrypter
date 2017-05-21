package logic;

import model.PdfFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;

/**
 * Created by wd40 on 21.05.17.
 */
public class PdfUtility {

    private PdfFile sourcePdfFile;
    private PdfFile targetPdfFile;

    public PdfUtility(PdfFile sourcePdfFile, PdfFile targetPdfFile) {
        this.sourcePdfFile = sourcePdfFile;
        this.targetPdfFile = targetPdfFile;
    }

    public void encrypt() {

        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File(sourcePdfFile.getPathname()), sourcePdfFile.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int keyLength = 128;

        AccessPermission ap = new AccessPermission();

        StandardProtectionPolicy spp = new StandardProtectionPolicy("", targetPdfFile.getPassword(), ap);

        spp.setEncryptionKeyLength(keyLength);
        spp.setPermissions(ap);

        try {
            doc.protect(spp);
            doc.save(targetPdfFile.getPathname());
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decrypt() {

        PDDocument doc = null;
        try {
            doc = PDDocument.load(new File(sourcePdfFile.getPathname()), sourcePdfFile.getPassword());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            doc.setAllSecurityToBeRemoved(true);
            doc.save(targetPdfFile.getPathname());
            doc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
