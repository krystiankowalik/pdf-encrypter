package util;

import model.pdf.PdfBatchJob;
import model.pdf.PdfJob;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.concurrent.*;

public class PdfEncryptionHandler {

    private static PdfEncryptionHandler pdfEncryptionHandler;
    final private Logger logger = Logger.getLogger(getClass());

    public static PdfEncryptionHandler getInstance() {

        if (pdfEncryptionHandler == null) {
            pdfEncryptionHandler = new PdfEncryptionHandler();
            return pdfEncryptionHandler;
        } else {
            return pdfEncryptionHandler;
        }
    }

    private PdfJob encrypt(PdfJob pdfJob) {

        logger.info("Encrypting " + pdfJob);

        if (pdfJob.getTargetPdfFile().passwordProperty().getValueSafe().length() == 0) {
            logger.info("There is no target password - decrypting instead of encrypting.");
            return decrypt(pdfJob);
        }

        PDDocument pdDocument = null;
        try {
            File pdfFile = new File(pdfJob.getSourcePdfFile().getPathname());
            pdDocument = PDDocument.load(pdfFile, pdfJob.getSourcePdfFile().getPassword());
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.SUCCESS_DECRYPT);
        } catch (InvalidPasswordException e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.INVALID_PASSWORD, e);
        } catch (FileNotFoundException e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.NO_SUCH_FILE, e);
        } catch (AccessDeniedException e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.ACCESS_DENIED, e);
        } catch (IOException e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.READ_ERROR, e);
        } catch (Exception e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.UNEXPECTED_ERROR, e);
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
                pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.SUCCESS_ENCRYPT);
            } catch (NullPointerException e) {
                pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.NO_DOCUMENT, e);
            } catch (FileNotFoundException e) {
                pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.NO_SUCH_FILE, e);
            } catch (AccessDeniedException e) {
                pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.ACCESS_DENIED, e);
            } catch (IOException e) {
                pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.WRITE_ERROR, e);
            } catch (Exception e) {
                pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.UNEXPECTED_ERROR, e);
            }
        }
        return pdfJob;
    }

    private PdfJob decrypt(PdfJob pdfJob) {


        logger.info("Decrypting " + pdfJob.getSourcePdfFile().getPathname() + " using source password: " + pdfJob.getSourcePdfFile().getPassword());

        try {
            PDDocument pdDocument = PDDocument.load(
                    new File(pdfJob.getSourcePdfFile().getPathname()), pdfJob.getSourcePdfFile().getPassword());
            pdDocument.setAllSecurityToBeRemoved(true);
            pdDocument.save(pdfJob.getTargetPdfFile().getPathname());
            pdDocument.close();
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.SUCCESS_DECRYPT);
        } catch (InvalidPasswordException e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.INVALID_PASSWORD, e);
        } catch (FileNotFoundException e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.NO_SUCH_FILE, e);
        } catch (AccessDeniedException e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.ACCESS_DENIED, e);
        } catch (IOException e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.WRITE_ERROR, e);
        } catch (Exception e) {
            pdfJob = setPdfJobStatus(pdfJob, PdfJob.Status.UNEXPECTED_ERROR, e);
        }
        return pdfJob;

    }

    private PdfJob setPdfJobStatus(PdfJob pdfJob, PdfJob.Status pdfJobStatus) {
        pdfJob.setStatus(pdfJobStatus);
        logger.info("Status of: " + pdfJob + " has been changed to " + pdfJobStatus);
        return pdfJob;
    }

    private PdfJob setPdfJobStatus(PdfJob pdfJob, PdfJob.Status pdfJobStatus, Throwable e) {
        pdfJob.setStatus(pdfJobStatus);
        logger.info("Status of: " + pdfJob + " has been changed to " + pdfJobStatus);
        logger.error(e);
        return pdfJob;
    }

    public void encrypt(PdfBatchJob pdfBatchJob) {
        PdfJob updatedPdfJob;
        for (int i = 0; i < pdfBatchJob.size(); ++i) {
            updatedPdfJob = encrypt(pdfBatchJob.get(i));
            pdfBatchJob.set(i, updatedPdfJob);
        }
    }

    public void decrypt(PdfBatchJob pdfBatchJob) {
        PdfJob updatedPdfJob;
        for (int i = 0; i < pdfBatchJob.size(); ++i) {
            updatedPdfJob = decrypt(pdfBatchJob.get(i));
            pdfBatchJob.set(i, updatedPdfJob);
        }
    }

    public void decryptAsync(PdfBatchJob pdfBatchJob) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {

            decrypt(pdfBatchJob);
        });
        shutdownExecutor(executorService);
    }

    public void encryptAsync(PdfBatchJob pdfBatchJob) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {

            encrypt(pdfBatchJob);
        });
        shutdownExecutor(executorService);
    }

    private void shutdownExecutor(ExecutorService executorService) {
        try {
            System.out.println("attempt to shutdown executor");
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("tasks interrupted");
        } finally {
            if (!executorService.isTerminated()) {
                System.err.println("cancel non-finished tasks");
            }
            executorService.shutdownNow();
            System.out.println("shutdown finished");
        }
    }
}
