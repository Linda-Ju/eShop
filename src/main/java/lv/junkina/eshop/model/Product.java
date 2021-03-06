package lv.junkina.eshop.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import javax.validation.constraints.NotEmpty;

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
    private double price;

    @ApiModelProperty(notes = "Quantity")
    private int quantity;

    @ApiModelProperty(notes = "Product description")
    private String description;

    @ApiModelProperty(notes = "Photo of the product")
    private String photo;

}
