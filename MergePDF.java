import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDPageLabels;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDPageLabelRange;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MergePDF {
    public static void main(String[] args) throws IOException {
        // Create input streams for the source PDF files
        InputStream file1Stream = new ByteArrayInputStream(getFile1Bytes());
        InputStream file2Stream = new ByteArrayInputStream(getFile2Bytes());

        // Create a new PDFMergerUtility instance
        PDFMergerUtility merger = new PDFMergerUtility();

        // Add the source PDF files to be merged
        merger.addSource(file1Stream);
        merger.addSource(file2Stream);

        // Create a new PDDocument to hold the merged result
        PDDocument result = new PDDocument();

        // Merge the source PDF files into the result document
        merger.appendDocument(result, PDDocument.load(file1Stream));
        merger.appendDocument(result, PDDocument.load(file2Stream));

        // Create a new PDPageLabels instance for the result document
        PDPageLabels pageLabels = new PDPageLabels(result);

        // Create a new PDPageLabelRange instance to specify the page numbering style and starting page number
        PDPageLabelRange range = new PDPageLabelRange();
        range.setStyle(PDPageLabelRange.STYLE_DECIMAL);
        range.setStart(1);

        // Set the page label range for the first page of the result document
        pageLabels.setLabelItem(0, range);

        // Set the page labels for the result document
        result.getDocumentCatalog().setPageLabels(pageLabels);

        // Save the merged result document to an output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        result.save(outputStream);

        // Close the result document and input streams
        result.close();
        file1Stream.close();
        file2Stream.close();
    }

    private static byte[] getFile1Bytes() {
        // Replace this method with your own implementation to get the bytes of file1.pdf
    }

    private static byte[] getFile2Bytes() {
        // Replace this method with your own implementation to get the bytes of file2.pdf
    }
}
