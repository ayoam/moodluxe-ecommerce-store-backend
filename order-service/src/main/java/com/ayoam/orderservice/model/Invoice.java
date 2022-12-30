package com.ayoam.orderservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue
            (strategy = GenerationType.SEQUENCE,generator = "invoice_generator")
    @SequenceGenerator
            (name="invoice_generator", sequenceName = "invoice_seq",
                    initialValue = 1000,
                    allocationSize=50)
    private Long invoiceId;

    @CreationTimestamp
    @Column(name = "InvoiceDate", nullable = false, updatable = false)
    private Date InvoiceDate;


    public String getFormattedInvoiceDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        return formatter.format(this.InvoiceDate);
    }
}
