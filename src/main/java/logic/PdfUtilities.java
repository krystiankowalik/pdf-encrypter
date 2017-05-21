package logic;

import model.PdfFiles;

/**
 * Created by wd40 on 21.05.17.
 */
public class PdfUtilities {


    private PdfFiles pdfSourceFiles;
    private PdfFiles pdfTargetFiles;

    private PdfUtility pdfUtility;

    public PdfUtilities(PdfFiles pdfSourceFiles, PdfFiles pdfTargetFiles) {
        this.pdfSourceFiles = pdfSourceFiles;
        this.pdfTargetFiles = pdfTargetFiles;
    }

    public PdfFiles getPdfSourceFiles() {
        return pdfSourceFiles;
    }


    public PdfFiles getPdfTargetFiles() {
        return pdfTargetFiles;
    }

    public void batchEncrypt(){
        for(int i=0; i<pdfSourceFiles.size(); ++i){
            pdfUtility= new PdfUtility(pdfSourceFiles.get(i), pdfTargetFiles.get(i));
            pdfUtility.encrypt();
        }
    }
    public void batchDecrypt(){
        for(int i=0; i<pdfSourceFiles.size(); ++i){
            pdfUtility= new PdfUtility(pdfSourceFiles.get(i), pdfTargetFiles.get(i));
            pdfUtility.decrypt();
        }
    }


}
