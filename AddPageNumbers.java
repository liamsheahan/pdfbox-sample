import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;

public class AddPageNumbers {
    public static void main(String[] args) throws IOException {
        // Load the PDF document
        PDDocument document = PDDocument.load(new File("input.pdf"));

        // Get the number of pages in the document
        int pageCount = document.getNumberOfPages();

        // Loop through each page in the document
        for (int i = 0; i < pageCount; i++) {
            // Get the current page
            PDPage page = document.getPage(i);

            // Create a content stream for the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

            // Set the font and font size
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Add the page number to the bottom center of the page
            contentStream.beginText();
            contentStream.newLineAtOffset(page.getMediaBox().getWidth() / 2, 30);
            contentStream.showText(Integer.toString(i + 1));
            contentStream.endText();

            // Close the content stream
            contentStream.close();
        }

        // Save the updated PDF document
        document.save("output.pdf");

        // Close the PDF document
        document.close();
    }
}
