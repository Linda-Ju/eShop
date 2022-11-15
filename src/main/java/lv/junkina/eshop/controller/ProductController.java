package lv.junkina.eshop.controller;

import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import lv.junkina.eshop.model.Product;
import lv.junkina.eshop.service.ProductService;
import lv.junkina.eshop.swagger.DescriptionVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@Api(tags = {DescriptionVariables.PRODUCT})
@Log4j2
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @GetMapping("/all")
    @ApiOperation(value = "Get all products",
            notes = "Returns the entire list of products",
            response = Product.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded", response = Product.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Retrieve list of Products");
        List<Product> productList = productService.getAllProducts();
        if (productList.isEmpty()) {
            log.warn("Product list is empty! {}", productList);
            return ResponseEntity.notFound().build();
        }
        log.debug("Product list is found. Size: {}", productList::size);
        return ResponseEntity.ok(productList);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get product by ID",
            notes = "Enter ID to search specific product",
            response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The request has succeeded"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    public ResponseEntity<Product> getProductById(@ApiParam(value = "product ID", required = true)
                                                         @NonNull @PathVariable Long id) {
        log.info("Get product by passing ID, where product ID is :{} ", id);
        Optional<Product> product = (productService.getProductById(id));
        if (!product.isPresent()) {
            log.warn("Product with ID {} is not found.", id);
        } else {
            log.debug("Product with ID {} is found: {}", id, product);
        }
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Saves product in database",
            notes = "If provided valid product, saves it",
            response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The product is successfully saved"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Product> saveProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        log.info("Create new product by passing : {}", product);
        if (bindingResult.hasErrors()) {
            log.error("New product is not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
        Product productSaved = productService.saveProduct(product);
        log.debug("New product is created: {}", product);
        return new ResponseEntity<>(productSaved, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes the product by id",
            notes = "Deletes the product if provided id exists",
            response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "The product is successfully deleted"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProductById(@ApiParam(value = "The id of the product", required = true)
                                                   @NonNull @PathVariable Long id) {
        log.info("Delete Employee by passing ID, where ID is:{}", id);
        Optional<Product> product = productService.getProductById(id);
        if (!(product.isPresent())) {
            log.warn("Product for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        productService.deleteProductById(id);
        log.debug("Product with id {} is deleted: {}", id, product);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates the product by id",
            notes = "Updates the product if provided id exists",
            response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "The product is successfully updated"),
            @ApiResponse(code = 400, message = "Missed required parameters, parameters are not valid"),
            @ApiResponse(code = 401, message = "The request requires user authentication"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The server has not found anything matching the Request-URI"),
            @ApiResponse(code = 500, message = "Server error")})
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<Product> updateProductById(@ApiParam(value = "id of the product", required = true)
                                                       @NonNull @PathVariable Long id,
                                                       @Valid @RequestBody Product product, BindingResult bindingResult)  {
        log.info("Update existing product with ID: {} and new body: {}", id, product);
        if (bindingResult.hasErrors() || !id.equals(product.getId())) {
            log.warn("Product for update with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        productService.saveProduct(product);
        log.debug("Product with id {} is updated: {}", id, product);
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }

}
