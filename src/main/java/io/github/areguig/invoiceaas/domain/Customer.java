package io.github.areguig.invoiceaas.domain;

import lombok.Data;

@Data
public class Customer {

    String summary;

    String address;

    String phone;

    String email;
}
