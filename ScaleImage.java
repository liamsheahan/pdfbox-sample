import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

public class ScaleImage {
    public static void main(String[] args) {
        // Set the desired image width
        float desiredWidth = 200;

        // Load the existing PDF document
        File file = new File("example.pdf");
        try (PDDocument document = PDDocument.load(file)) {
            // Create an object for PDImageXObject
            PDImageXObject pdImage = PDImageXObject.createFromFile("image.jpg", document);

            // Calculate the scaling factor
            float scaleFactor = desiredWidth / pdImage.getWidth();

            // Calculate the scaled image dimensions
            float scaledWidth = pdImage.getWidth() * scaleFactor;
            float scaledHeight = pdImage.getHeight() * scaleFactor;

            // Iterate through each page of the PDF
            for (PDPage page : document.getPages()) {
                // Create a new content stream
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                    // Draw the scaled image in the top-left corner of the page
                    contentStream.drawImage(pdImage, 0, 0, scaledWidth, scaledHeight);
                }
            }

            // Save the document
            document.save("example-with-scaled-image.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
