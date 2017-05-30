package logic;

import model.PdfBatchJob;
import model.PdfJob;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

// TODO: 29.05.17 Job statuses to be added

public class PdfEncryptionUtilities {

    private PdfJob encrypt(PdfJob pdfJob) {

        System.out.println("Trying to encrypt using source password: " + pdfJob.getSourcePdfFile().getPassword());
        System.out.println("Trying to encrypt using target password: " + pdfJob.getTargetPdfFile().getPassword());


        PDDocument pdDocument = null;
        try {
            //doc = PDDocument.load(new File(pdfJob.getSourcePdfFile().getPathname()), pdfJob.getSourcePdfFile().getPassword());
            pdDocument = PDDocument.load(new File(pdfJob.getSourcePdfFile().getPathname()), pdfJob.getSourcePdfFile().getPassword());
            pdfJob.setStatus(PdfJob.Status.SUCCESS_DECRYPT);
        } catch (InvalidPasswordException e) {
            pdfJob.setStatus(PdfJob.Status.INVALID_PASSWORD);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            pdfJob.setStatus(PdfJob.Status.NO_SUCH_FILE);
            e.printStackTrace();
        } catch (AccessDeniedException e) {
            pdfJob.setStatus(PdfJob.Status.ACCESS_DENIED);
            e.printStackTrace();
        } catch (IOException e) {
            pdfJob.setStatus(PdfJob.Status.READ_ERROR);
            e.printStackTrace();
        } catch (Exception e){
            pdfJob.setStatus(PdfJob.Status.UNEXPECTED_ERROR);
            e.printStackTrace();
        }

        int keyLength = 128;

        AccessPermission ap = new AccessPermission();

        StandardProtectionPolicy standardProtectionPolicy =
                new StandardProtectionPolicy("", pdfJob.getTargetPdfFile().getPassword(), ap);

        standardProtectionPolicy.setEncryptionKeyLength(keyLength);
        standardProtectionPolicy.setPermissions(ap);

        if (pdDocument != null) {
            try {
                pdDocument.protect(standardProtectionPolicy);
                pdDocument.save(pdfJob.getTargetPdfFile().getPathname());
                pdDocument.close();
                pdfJob.setStatus(PdfJob.Status.SUCCESS_ENCRYPT);
            } catch (NullPointerException e) {
                pdfJob.setStatus(PdfJob.Status.NO_DOCUMENT);
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                pdfJob.setStatus(PdfJob.Status.NO_SUCH_FILE);
                e.printStackTrace();
            } catch (AccessDeniedException e) {
                pdfJob.setStatus(PdfJob.Status.ACCESS_DENIED);
                e.printStackTrace();
            } catch (IOException e) {
                pdfJob.setStatus(PdfJob.Status.WRITE_ERROR);
                e.printStackTrace();
            } catch (Exception e){
                pdfJob.setStatus(PdfJob.Status.UNEXPECTED_ERROR);
                e.printStackTrace();
            }
        }

        return pdfJob;
    }

    private PdfJob decrypt(PdfJob pdfJob) {

        System.out.println("Trying to decrypt using source password: " + pdfJob.getSourcePdfFile().getPassword());

        try {
            PDDocument  pdDocument = PDDocument.load(
                    new File(pdfJob.getSourcePdfFile().getPathname()), pdfJob.getSourcePdfFile().getPassword());
            pdDocument.setAllSecurityToBeRemoved(true);
            pdDocument.save(pdfJob.getTargetPdfFile().getPathname());
            pdDocument.close();
            pdfJob.setStatus(PdfJob.Status.SUCCESS_DECRYPT);
        } catch (InvalidPasswordException e) {
            pdfJob.setStatus(PdfJob.Status.INVALID_PASSWORD);
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            pdfJob.setStatus(PdfJob.Status.NO_SUCH_FILE);
            e.printStackTrace();
        } catch (AccessDeniedException e) {
            pdfJob.setStatus(PdfJob.Status.ACCESS_DENIED);
            e.printStackTrace();
        } catch (IOException e) {
            pdfJob.setStatus(PdfJob.Status.WRITE_ERROR);
            e.printStackTrace();
        } catch (Exception e){
            pdfJob.setStatus(PdfJob.Status.UNEXPECTED_ERROR);
            e.printStackTrace();
        }
        return pdfJob;

    }

    public PdfBatchJob encrypt(PdfBatchJob pdfBatchJob) {
        PdfJob updatedPdfJob;
        for (int i = 0; i < pdfBatchJob.size(); ++i) {
            updatedPdfJob = encrypt(pdfBatchJob.get(i));
            pdfBatchJob.set(i, updatedPdfJob);
        }
        return pdfBatchJob;
    }

    public PdfBatchJob decrypt(PdfBatchJob pdfBatchJob) {
        PdfJob updatedPdfJob;
        for (int i = 0; i < pdfBatchJob.size(); ++i) {
            updatedPdfJob= decrypt(pdfBatchJob.get(i));
            pdfBatchJob.set(i, updatedPdfJob);
        }
        return pdfBatchJob;
    }
}
