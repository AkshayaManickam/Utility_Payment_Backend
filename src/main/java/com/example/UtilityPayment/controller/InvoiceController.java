package com.example.UtilityPayment.controller;

import com.example.UtilityPayment.model.Invoice;
import com.example.UtilityPayment.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "http://localhost:4300")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/pending/{userEmail}")
    public List<Invoice> getPendingBills(@PathVariable String userEmail) {
        System.out.println("User Email: " + userEmail);  // Debugging line to check if the email is received correctly.
        return invoiceService.getPendingBills(userEmail);
    }

}

