package id.artivisi.training.kisel.jposweb.webjposclient.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class TopupRequest {

    @NotNull @NotEmpty
    private String msisdn;

    @NotNull @Min(10000)
    private BigDecimal nilai;
}
