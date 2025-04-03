package com.example.UtilityPayment.controller;


import com.example.UtilityPayment.model.Invoice;
import com.example.UtilityPayment.service.Discount.DiscountContext;
import com.example.UtilityPayment.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "*") // Allow frontend to access backend
public class DiscountController {

    private final DiscountContext discountContext;
    private final InvoiceService invoiceService;

    public DiscountController(DiscountContext discountContext, InvoiceService invoiceService) {
        this.discountContext = discountContext;
        this.invoiceService = invoiceService;
    }

    @GetMapping("/calculate")
    public ResponseEntity<Double> calculatePayment(@RequestParam Long invoiceId, @RequestParam String discountType) {
        try {
            Invoice invoice = invoiceService.getInvoiceById(invoiceId);
            if (invoice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            double amount = discountContext.calculateDiscountedAmount(discountType, invoice.getTotalAmount());
            return ResponseEntity.ok(amount);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}