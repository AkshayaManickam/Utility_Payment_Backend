package com.example.UtilityPayment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "customerId"),
        @UniqueConstraint(columnNames = "serviceConnectionNo"),
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "phone")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true)
    private String customerId;

    @NotNull
    @Column(nullable = false, unique = true)
    private String serviceConnectionNo;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")
    @Column(nullable = false, unique = true)
    private String phone;

    @NotNull
    @Column(nullable = false)
    private String address;

    @Min(0)
    @Column(nullable = false)
    private int unitsConsumption;

    @NotNull
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    @Column(nullable = false)
    private String startDate;

    @Min(0)
    @Column(nullable = false)
    private double creditCardBalance = 50000.0;  // Default Mock Balance

    @Min(0)
    @Column(nullable = false)
    private double debitCardBalance = 30000.0;  // Default Mock Balance

    @Min(0)
    @Column(nullable = false)
    private double walletBalance = 20000.0;

    public double getCreditCardBalance() {
        return creditCardBalance;
    }

    public void setCreditCardBalance(double creditCardBalance) {
        this.creditCardBalance = creditCardBalance;
    }

    public double getDebitCardBalance() {
        return debitCardBalance;
    }

    public void setDebitCardBalance(double debitCardBalance) {
        this.debitCardBalance = debitCardBalance;
    }

    public double getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(double walletBalance) {
        this.walletBalance = walletBalance;
    }
// Default Mock Balance


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getServiceConnectionNo() { return serviceConnectionNo; }
    public void setServiceConnectionNo(String serviceConnectionNo) { this.serviceConnectionNo = serviceConnectionNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getUnitsConsumption() { return unitsConsumption; }
    public void setUnitsConsumption(int unitsConsumption) { this.unitsConsumption = unitsConsumption; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
}

