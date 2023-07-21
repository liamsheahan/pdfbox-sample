import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFStreamEngine;
import org.apache.pdfbox.util.TextPosition;

import java.io.IOException;
import java.util.List;

public class PDFMarginFinder extends PDFStreamEngine {
    private float minX = Float.MAX_VALUE;
    private float minY = Float.MAX_VALUE;
    private float maxX = Float.MIN_VALUE;
    private float maxY = Float.MIN_VALUE;

    public static void main(String[] args) throws IOException {
        PDDocument document = PDDocument.load("example.pdf");
        List<PDPage> pages = document.getDocumentCatalog().getAllPages();
        PDPage page = pages.get(0); // Get the first page

        PDFMarginFinder finder = new PDFMarginFinder();
        finder.processStream(page, page.findResources(), page.getContents().getStream());

        float leftMargin = finder.minX;
        float rightMargin = page.findMediaBox().getWidth() - finder.maxX;
        float topMargin = page.findMediaBox().getHeight() - finder.maxY;
        float bottomMargin = finder.minY;

        System.out.println("Left margin: " + leftMargin);
        System.out.println("Right margin: " + rightMargin);
        System.out.println("Top margin: " + topMargin);
        System.out.println("Bottom margin: " + bottomMargin);

        document.close();
    }

    @Override
    protected void processTextPosition(TextPosition text) {
        if (text.getX() < minX) {
            minX = text.getX();
        }
        if (text.getY() < minY) {
            minY = text.getY();
        }
        if (text.getX() + text.getWidth() > maxX) {
            maxX = text.getX() + text.getWidth();
        }
        if (text.getY() + text.getHeight() > maxY) {
            maxY = text.getY() + text.getHeight();
        }
    }
}
