package com.example.UtilityPayment.service;

import com.example.UtilityPayment.model.Invoice;
import com.example.UtilityPayment.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    // Method to fetch pending bills for a user
    public List<Invoice> getPendingBills(String userEmail) {
        return invoiceRepository.findPendingBillsByUserEmail(userEmail);
    }
}
