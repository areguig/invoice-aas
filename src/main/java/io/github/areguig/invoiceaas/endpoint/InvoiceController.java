package io.github.areguig.invoiceaas.endpoint;

import io.github.areguig.invoiceaas.PdfGenerator;
import io.github.areguig.invoiceaas.domain.Invoice;
import io.github.areguig.invoiceaas.domain.Item;
import java.io.ByteArrayInputStream;
import java.util.Collections;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@CommonsLog
public class InvoiceController {

    @Autowired
    private PdfGenerator pdfGenerator;

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            ,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView invoice(@RequestBody Invoice invoice){
        log.info(invoice);
        ModelAndView mav = new ModelAndView("invoice");
        mav.addObject("data", updateTotals(invoice));
        return mav;
    }

    @PostMapping(value = "/pdf", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            ,produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<ByteArrayResource> invoicePDF(@RequestBody Invoice invoice) throws Exception {
        log.info(invoice);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_+"+invoice.getId()+".pdf");
        byte[]  pdfAsytes = new byte[]{};
        pdfGenerator.createPdf("invoice", Collections.singletonMap("data",invoice)).write(pdfAsytes);
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body( new ByteArrayResource(pdfAsytes));

    }


    private static Invoice updateTotals(Invoice invoice){
        invoice.getItems().forEach(item -> {
            double total = item.getQuantity()*item.getPrice();
            if(item.getTax()>0){
                total += total* (item.getTax()/100);
            }
            item.setTotal(total);
        });
        invoice.setSubTotal(invoice.getItems().stream().mapToDouble(i -> i.getPrice()*i.getQuantity()).sum());
        invoice.setTaxTotal(invoice.getItems().stream().mapToDouble(i -> i.getPrice()*(i.getTax()/100)*i.getQuantity()).sum());
        invoice.setTotal(invoice.getItems().stream().mapToDouble(Item::getTotal).sum());
        return invoice;
    }

}
