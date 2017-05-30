package logic;

import model.PdfBatchJob;
import model.PdfJob;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.IOException;

// TODO: 29.05.17 Job statuses to be added

public class PdfEncryptionUtilities {

    private void encrypt(PdfJob pdfJob) {

        System.out.println("Trying to encrypt using source password: "+ pdfJob.getSourcePdfFile().getPassword());
        System.out.println("Trying to encrypt using target password: "+ pdfJob.getTargetPdfFile().getPassword());


        PDDocument doc = null;
        try {
            //doc = PDDocument.load(new File(pdfJob.getSourcePdfFile().getPathname()), pdfJob.getSourcePdfFile().getPassword());
            doc = PDDocument.load(new File(pdfJob.getSourcePdfFile().getPathname()), pdfJob.getSourcePdfFile().getPassword());
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int keyLength = 128;

        AccessPermission ap = new AccessPermission();

        StandardProtectionPolicy spp = new StandardProtectionPolicy("", pdfJob.getTargetPdfFile().getPassword(), ap);


        spp.setEncryptionKeyLength(keyLength);
        spp.setPermissions(ap);

        if (doc != null) {
            try {
                doc.protect(spp);
                doc.save(pdfJob.getTargetPdfFile().getPathname());
                doc.close();
                pdfJob.setStatus(PdfJob.Status.SUCCESS);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private void decrypt(PdfJob pdfJob) {

        System.out.println("Trying to decrypt using source password: "+ pdfJob.getSourcePdfFile().getPassword());

        PDDocument doc;
        try {
            doc = PDDocument.load(new File(pdfJob.getSourcePdfFile().getPathname()), pdfJob.getSourcePdfFile().getPassword());
            doc.setAllSecurityToBeRemoved(true);
            doc.save(pdfJob.getTargetPdfFile().getPathname());
            doc.close();
            pdfJob.setStatus(PdfJob.Status.SUCCESS);
        }catch(InvalidPasswordException e){
            pdfJob.setStatus(PdfJob.Status.INVALID_PASSWORD);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void encrypt(PdfBatchJob pdfBatchJob) {
        for (int i = 0; i < pdfBatchJob.size(); ++i){
            encrypt(pdfBatchJob.get(i));
        }
    }

    public void decrypt(PdfBatchJob pdfBatchJob) {
        for (int i = 0; i < pdfBatchJob.size(); ++i){
            decrypt(pdfBatchJob.get(i));
        }
    }
}
