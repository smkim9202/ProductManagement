package kr.co.api.product.management.application;

import kr.co.api.product.management.domain.Product;
import kr.co.api.product.management.infrastructure.ListProductRepository;
import kr.co.api.product.management.presentation.ProductDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SimpleProductService {

    private ListProductRepository listProductRepository;
    private ModelMapper modelMapper;
    private ValidationService validationService;

    @Autowired
    SimpleProductService(ListProductRepository listProductRepository, 
                         ModelMapper modelMapper, ValidationService validationService){
        this.listProductRepository = listProductRepository;
        this.modelMapper = modelMapper;
        this.validationService = validationService;
    }

    public ProductDto add(ProductDto productDto){
        // 1.ProductDto를 Product로 변환하는 코드
        Product product = modelMapper.map(productDto, Product.class);
        // 도메인 지식 유효성검사
        validationService.checkValid(product);

        // 2.레포지토리를 호출하는 코드
        Product saveProduct = listProductRepository.add(product);

        // 3. Product를 ProductDto로 변환하는 코드
        ProductDto saveProductDto = modelMapper.map(saveProduct, ProductDto.class);

        // 4. DTO 반환하는 코드
        return saveProductDto;
    }

    public ProductDto findByid(Long id){
        Product product = listProductRepository.findById(id);
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return productDto;
    }

    public List<ProductDto> findAll2(){
        List<Product> products = listProductRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        return productDtos;
    }

    public List<ProductDto> findAll(){
        List<Product> products = listProductRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(modelMapper.map(product, ProductDto.class));
        }
        return productDtos;
    }

    public List<ProductDto> findByNameContaining2(String name){
        List<Product> products = listProductRepository.findByNameContaining(name);
        List<ProductDto> productDtos = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
        return productDtos;
    }

    public List<ProductDto> findByNameContaining(String name){
        List<Product> products = listProductRepository.findByNameContaining(name);
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(modelMapper.map(product, ProductDto.class));
        }
        return productDtos;
    }

    public ProductDto update(ProductDto productDto){
        Product product = modelMapper.map(productDto, Product.class);
        // 도메인 지식 유효성검사
        validationService.checkValid(product);
        Product updateProduct = listProductRepository.update(product);
        ProductDto updateProductDto = modelMapper.map(updateProduct, ProductDto.class);
        return updateProductDto;
    }

    public void delete(Long id){
        listProductRepository.delete(id);
    }

}
