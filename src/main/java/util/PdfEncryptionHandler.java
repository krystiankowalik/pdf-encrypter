package util;

import event.type.UpdateJobEvent;
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
import java.util.List;
import java.util.concurrent.*;

public class PdfEncryptionHandler {

    private static PdfEncryptionHandler pdfEncryptionHandler;
    final private Logger logger = Logger.getLogger(getClass());

    public static PdfEncryptionHandler getInstance() {

        if (pdfEncryptionHandler == null) {
            pdfEncryptionHandler = new PdfEncryptionHandler();
        }
        return pdfEncryptionHandler;

    }

    private void encrypt(PdfJob pdfJob) {

        logger.info("Encrypting " + pdfJob);

        if (pdfJob.getTargetPdfFile().passwordProperty().getValueSafe().length() == 0) {
            logger.info("There is no target password - decrypting instead of encrypting.");
            decrypt(pdfJob);
            return;
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
        EventBusProvider.getInstance().post(new UpdateJobEvent(pdfJob));
    }

    private void decrypt(PdfJob pdfJob) {


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
        EventBusProvider.getInstance().post(new UpdateJobEvent(pdfJob));

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

    public void decryptAsync(PdfBatchJob pdfBatchJob) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        /*executorService.submit(() -> {
            split(pdfBatchJob)
                    .forEach(batchJob ->
                            new PdfTask(batchJob, PdfBatchJob.Type.DECRYPT).run());
        });*/
        executorService.submit(() ->
                split(pdfBatchJob)
                        .forEach(batchJob ->
                                new PdfTask(batchJob, PdfBatchJob.Type.DECRYPT).run()));
        shutdownExecutor(executorService);
    }

    public void encryptAsync(PdfBatchJob pdfBatchJob) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() ->
                split(pdfBatchJob)
                        .forEach(batchJob ->
                                new PdfTask(batchJob, PdfBatchJob.Type.ENCRYPT).run()));
        shutdownExecutor(executorService);
    }

    private void shutdownExecutor(ExecutorService executorService) {
        try {
            logger.debug("attempt to shutdown executor");
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.debug("tasks interrupted");
        } finally {
            if (!executorService.isTerminated()) {
                logger.debug("cancel non-finished tasks");
            }
            executorService.shutdownNow();
            logger.debug("shutdown finished");
        }
    }

    private List<PdfBatchJob> split(PdfBatchJob pdfBatchJob) {
        int size = pdfBatchJob.size();
        if (size > 5 && size < 10) {
            return pdfBatchJob.split(2);
        } else if (size > 10 && size < 15) {
            return pdfBatchJob.split(3);
        } else if (pdfBatchJob.size() > 15) {
            return pdfBatchJob.split(4);
        } else {
            return pdfBatchJob.split(5);
        }
    }

    class PdfTask implements Runnable {

        private PdfBatchJob pdfBatchJob;
        private PdfBatchJob.Type pdfBatchJobType;

        PdfTask(PdfBatchJob pdfBatchJob, PdfBatchJob.Type pdfBatchJobType) {
            this.pdfBatchJob = pdfBatchJob;
            this.pdfBatchJobType = pdfBatchJobType;
        }

        @Override
        public void run() {
            if (pdfBatchJobType == PdfBatchJob.Type.ENCRYPT) {
                pdfBatchJob
                        .getPdfBatchJob()
                        .forEach(PdfEncryptionHandler.this::encrypt);
            } else {
                pdfBatchJob
                        .getPdfBatchJob()
                        .forEach(PdfEncryptionHandler.this::decrypt);
            }
        }
    }

}

