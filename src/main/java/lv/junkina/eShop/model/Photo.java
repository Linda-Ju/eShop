package lv.junkina.eShop.model;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

@ApiModel(description = "Model of the product photo data ")
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @ApiModelProperty(notes = "The unique id of the photo")
    private Long id;

    @ApiModelProperty(notes = "Name of the photo")
    @NonNull
    @NotEmpty
    private String name;

    @ApiModelProperty(notes = "Photo of the product")
    @NonNull
    @NotEmpty
    private byte[] photo;
}
