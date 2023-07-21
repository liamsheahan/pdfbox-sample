import org.apache.pdfbox.contentstream.PDFStreamEngine;
import org.apache.pdfbox.contentstream.operator.Operator;
import org.apache.pdfbox.cos.COSBase;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;
import java.util.List;

public class GetMargins {
    public static void main(String[] args) {
        try (PDDocument document = PDDocument.load("example.pdf")) {
            // Get the first page of the document
            PDPage page = document.getPage(0);

            // Create a new MarginFinder
            MarginFinder marginFinder = new MarginFinder();
            marginFinder.processPage(page);

            // Get the effective margins
            PDRectangle margins = marginFinder.getMargins();
            System.out.println("Left margin: " + margins.getLowerLeftX());
            System.out.println("Right margin: " + (page.getMediaBox().getWidth() - margins.getUpperRightX()));
            System.out.println("Top margin: " + (page.getMediaBox().getHeight() - margins.getUpperRightY()));
            System.out.println("Bottom margin: " + margins.getLowerLeftY());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MarginFinder extends PDFStreamEngine {
        private float minX = Float.MAX_VALUE;
        private float minY = Float.MAX_VALUE;
        private float maxX = Float.MIN_VALUE;
        private float maxY = Float.MIN_VALUE;

        public PDRectangle getMargins() {
            return new PDRectangle(minX, minY, maxX - minX, maxY - minY);
        }

        @Override
        protected void processOperator(Operator operator, List<COSBase> operands) throws IOException {
            String name = operator.getName();
            if ("Td".equals(name) || "TD".equals(name) || "Tm".equals(name)) {
                Matrix textMatrix = getTextMatrix();
                float x = textMatrix.getTranslateX();
                float y = textMatrix.getTranslateY();
                minX = Math.min(minX, x);
                minY = Math.min(minY, y);
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
            }
            super.processOperator(operator, operands);
        }
    }
}
