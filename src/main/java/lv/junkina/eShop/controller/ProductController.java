package lv.junkina.eShop.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import lv.junkina.eShop.model.Product;
import lv.junkina.eShop.service.ProductService;
import lv.junkina.eShop.swagger.DescriptionVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {DescriptionVariables.PRODUCT})
@Log4j2
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping
    @ApiOperation(value = "Finds all product",
            notes = "Returns the entire list of products",
            response = Product.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = Product.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Product>> findAllProducts(){
        log.info("Retrieve list of Products");
        List<Product> productList = productService.findAllProducts();
        if (productList.isEmpty()) {
            log.warn("Product list is empty! {}", productList);
            return ResponseEntity.notFound().build();
        }
        log.debug("Product list is found. Size: {}", productList::size);
        return ResponseEntity.ok(productList);
    }
}
