package io.github.areguig.invoiceaas.domain;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Invoice {

    @NotNull
    @NotEmpty
    String id;

    String currency;

    String lang;

    String dueDate;

    String date;

    @NotNull
    List<Item> items;

    @NotNull
    Customer customer;

    @NotNull
    Company company;

    String notes;

    double total;

    double subTotal;

    double taxTotal;
}
