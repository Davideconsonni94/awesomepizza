package it.adesso.awesomepizza.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRequest {

    @NotNull
    @NotEmpty
    private String customer;
    @NotNull
    @NotEmpty
    private String pizza;
}
