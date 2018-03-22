package io.github.areguig.invoiceaas;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

@Component
public class PdfGenerator {
    @Autowired
    private TemplateEngine templateEngine;

    public FileOutputStream createPdf(String templateName, Map<String, Object> map) throws Exception {
        Context ctx = new Context();
        for (Object o : map.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            ctx.setVariable(pair.getKey().toString(), pair.getValue());
        }

        String processedHtml = templateEngine.process(templateName, ctx);
        FileOutputStream os;
        String fileName = UUID.randomUUID().toString();

        final File outputFile = File.createTempFile(fileName, ".pdf");
        os = new FileOutputStream(outputFile);

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processedHtml);
        renderer.layout();
        renderer.createPDF(os, false);
        renderer.finishPDF();
        return os;
    }
}