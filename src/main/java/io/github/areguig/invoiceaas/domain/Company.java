package io.github.areguig.invoiceaas.domain;

import lombok.Data;

@Data
public class Company {

    String summary;

    String address;

    String phone;

    String email;

    String logo;

    String siret;
}
