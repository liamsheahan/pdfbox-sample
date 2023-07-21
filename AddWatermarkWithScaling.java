// US Legal page size is 8.5 x 14 inches, or 612 x 1008 points (since 1 inch = 72 points). 
// In Apache PDFBox, the position of elements on a page is determined by their x and y coordinates. The origin point (0, 0) is located at the bottom-left corner of the page, with the x-axis extending horizontally to the right and the y-axis extending vertically upwards. The units are in points, where 1 point is equal to 1/72 of an inch.
// When you draw an element on a page, such as an image or a piece of text, you can specify its position by providing the x and y coordinates as arguments. For example, to draw an image at the top-left corner of the page, you would use contentStream.drawImage(pdImage, 0, page.getMediaBox().getHeight() - pdImage.getHeight());, where page.getMediaBox().getHeight() returns the height of the page and pdImage.getHeight() returns the height of the image.
// You can use these coordinates to position elements relative to each other by adding or subtracting offsets from their x and y values.

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

public class AddWatermarkWithScaling {
    public static void main(String[] args) {
        // Set the banner height as a percentage of the page height
        float bannerHeightPercentage = 0.05f;

        // Load the existing PDF document
        File file = new File("example.pdf");
        try (PDDocument document = PDDocument.load(file)) {
            // Create an object for PDImageXObject
            PDImageXObject pdImage = PDImageXObject.createFromFile("image.jpg", document);

            // Iterate through each page of the PDF
            for (PDPage page : document.getPages()) {
                // Get the current page size
                PDRectangle mediaBox = page.getMediaBox();
                float width = mediaBox.getWidth();
                float height = mediaBox.getHeight();

                // Calculate the banner height in points
                float bannerHeight = height * bannerHeightPercentage;

                // Calculate the scaling factor for the existing content
                float scaleFactor = (height - bannerHeight) / height;

                // Calculate the scaling factor for the watermark image
                float imageScaleFactor = Math.min(bannerHeight / pdImage.getHeight(), 1);

                // Create a new content stream
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                    // Scale down all of the existing content
                    contentStream.transform(Matrix.getScaleInstance(scaleFactor, scaleFactor));

                    // Draw a white rectangle to cover the original page margins
                    contentStream.setNonStrokingColor(1f, 1f, 1f);
                    contentStream.addRect(0, height - bannerHeight, width, bannerHeight);
                    contentStream.fill();

                    // Draw the watermark image in the top-left corner of the banner area
                    float imageWidth = pdImage.getWidth() * imageScaleFactor;
                    float imageHeight = pdImage.getHeight() * imageScaleFactor;
                    float imageX = 0;
                    float imageY = height - ((bannerHeight - imageHeight) / 2) - imageHeight;
                    contentStream.drawImage(pdImage, imageX, imageY, imageWidth, imageHeight);
                }
            }

            // Save the document
            document.save("example-with-watermark-and-scaling.pdf");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
