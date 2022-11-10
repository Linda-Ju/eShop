package lv.junkina.eshop.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lv.junkina.eshop.swagger.DescriptionVariables;
import org.springframework.stereotype.Component;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@ApiModel(description = "Model of the product data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @ApiModelProperty(notes = "The unique id of the product")
    private Long id;

    @ApiModelProperty(notes = "Name of the product")
    @NotEmpty
    private String name;

    @ApiModelProperty(notes = "Price of the product")
    @DecimalMin(value = "0.01", message = DescriptionVariables.PRICE_MIN)
    private BigDecimal price;

    @ApiModelProperty(notes = "Quantity")
    @Min(value = 1, message = DescriptionVariables.QUANTITY_MIN)
    private int quantity;

    @ApiModelProperty(notes = "Product description")
    private String description;

    @ApiModelProperty(notes = "Photo of the product")
    private String photo;

}
