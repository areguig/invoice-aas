package io.github.areguig.invoiceaas.endpoint;

import io.github.areguig.invoiceaas.domain.Invoice;
import java.io.ByteArrayInputStream;
import lombok.extern.apachecommons.CommonsLog;
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

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            ,produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView invoice(@RequestBody Invoice invoice){
        log.info(invoice);
        ModelAndView mav = new ModelAndView("invoice");
        mav.addObject("data", invoice);
        return mav;
    }

    @PostMapping(value = "/pdf", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
            ,produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> invoicePDF(@RequestBody Invoice invoice){
        log.info(invoice);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=invoice_+"+invoice.getId()+".pdf");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(new ByteArrayInputStream((""+invoice.toString()+"").getBytes())));

    }

}
