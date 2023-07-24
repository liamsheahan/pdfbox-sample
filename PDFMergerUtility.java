import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.common.PDPageLabels;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDPageLabelRange;

import java.io.File;
import java.io.IOException;

public class MergePDF {
    public static void main(String[] args) throws IOException {
        // Create a new PDFMergerUtility instance
        PDFMergerUtility merger = new PDFMergerUtility();

        // Add the source PDF files to be merged
        merger.addSource(new File("file1.pdf"));
        merger.addSource(new File("file2.pdf"));

        // Create a new PDDocument to hold the merged result
        PDDocument result = new PDDocument();

        // Merge the source PDF files into the result document
        merger.appendDocument(result, PDDocument.load(new File("file1.pdf")));
        merger.appendDocument(result, PDDocument.load(new File("file2.pdf")));

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

        // Save the merged result document
        result.save("merged.pdf");

        // Close the result document
        result.close();
    }
}
