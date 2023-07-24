import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject;
import org.apache.pdfbox.util.LayerUtility;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ScalePDFContent {
    public static void main(String[] args) throws IOException {
        PDDocument doc = null;
        try {
            URL file = new URL("file:///PdfFile.pdf");
            doc = PDDocument.load(file);
            List<?> allPages = doc.getDocumentCatalog().getAllPages();
            float headerFooterHeight = 0.03f; // 3% of page height
            for (int i = 0; i < allPages.size(); i++) {
                PDPage page = (PDPage) allPages.get(i);
                PDRectangle mediaBox = page.getMediaBox();
                float pageHeight = mediaBox.getHeight();
                float margin = pageHeight * headerFooterHeight;
                float scale = (pageHeight - 2 * margin) / pageHeight;
                LayerUtility layerUtility = new LayerUtility(doc);
                PDFormXObject form = layerUtility.importPageAsForm(doc, i);
                AffineTransform at = AffineTransform.getTranslateInstance(0, margin);
                at.concatenate(AffineTransform.getScaleInstance(1, scale));
                layerUtility.wrapInSaveRestore(form);
                PDPageContentStream contentStream = new PDPageContentStream(doc, page);
                contentStream.drawForm(form, at);
                contentStream.close();
            }
            doc.save("PdfFile.pdf");
        } finally {
            if (doc != null) {
                doc.close();
            }
        }
    }
}
