package io.github.areguig.invoiceaas.domain;

import lombok.Data;

@Data
public class Item {

    String title;

    String description;

    Double price;

    Integer quantity;

    double tax;

    double total;
}
