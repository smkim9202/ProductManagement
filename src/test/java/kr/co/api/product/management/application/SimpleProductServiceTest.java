package kr.co.api.product.management.application;

import kr.co.api.product.management.domain.EntityNotFoundException;
import kr.co.api.product.management.presentation.ProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SimpleProductServiceTest {

    @Autowired
    SimpleProductService simpleProductService;

    //@Transactional
    @Test
    @DisplayName("상품을 추가한 후 id로 조회하면 해당 상품이 조회되어야한다.")
    void productAddAndFindByIdTest() {
        // given
        ProductDto productDto = new ProductDto("색연필", 5000, 1220);

        // when
        ProductDto savedProductDto = simpleProductService.add(productDto);
        Long savedProductId = savedProductDto.getId();
        ProductDto foundProductDto = simpleProductService.findById(savedProductId);

        // then
        assertTrue(savedProductDto.getId().equals(foundProductDto.getId()));
        assertTrue(savedProductDto.getName().equals(foundProductDto.getName()));
        assertTrue(savedProductDto.getPrice().equals(foundProductDto.getPrice()));
        assertTrue(savedProductDto.getAmount().equals(foundProductDto.getAmount()));
    }

    //@Transactional
    @Test
    @DisplayName("상품을 추가한 후 해당 id로 수정하면 수정된 상품이 조회되어야한다.")
    void productUpdateAndFindByIdTest() {
        // given
        ProductDto productDto = new ProductDto("김밥", 2000, 120);
        ProductDto savedProductDto = simpleProductService.add(productDto);
        Long savedProductId = savedProductDto.getId();
        ProductDto updateProductDto = new ProductDto(savedProductId, "참치김밥", 5000, 1220);

        // when
        simpleProductService.update(updateProductDto);
        ProductDto foundProductDto = simpleProductService.findById(savedProductId);

        // then
        assertTrue(updateProductDto.getId().equals(foundProductDto.getId()));
        assertTrue(updateProductDto.getName().equals(foundProductDto.getName()));
        assertTrue(updateProductDto.getPrice().equals(foundProductDto.getPrice()));
        assertTrue(updateProductDto.getAmount().equals(foundProductDto.getAmount()));
    }

    @Test
    @DisplayName("존재하지 않는 상품 id로 조회하면 EntityNotFoundException이 발생해야한다.")
    void findProductNotExistIdTest() {
        // given
        Long notExistId = -1L;

        // when & then
        assertThrows(EntityNotFoundException.class, () -> {simpleProductService.findById(notExistId);});
    }

    //@Transactional
    @Test
    @DisplayName("상품 삭제 후 상품 id로 조회하면 해당 EntityNotFoundException이 발생해야한다.")
    void productFindByIdAndDeleteTest() {
        // given
        ProductDto productDto = new ProductDto("다시삭제", 5000, 1220);
        ProductDto savedProductDto = simpleProductService.add(productDto);
        Long savedProductId = savedProductDto.getId();

        // when
        simpleProductService.delete(savedProductId);

        // then
        assertThrows(EntityNotFoundException.class, () -> {simpleProductService.findById(savedProductId);});
    }

    //@Transactional
    @Test
    @DisplayName("상품을 모두 조회하면 등록된 상품 중 추가한 상품이 포함되어야 한다.")
    void findAllTest() {
        // given
        ProductDto productDto1 = new ProductDto("김밥", 2000, 120);
        ProductDto productDto2 = new ProductDto("라면", 3000, 150);
        ProductDto savedProductDto1 = simpleProductService.add(productDto1);
        ProductDto savedProductDto2 = simpleProductService.add(productDto2);

        // when
        List<ProductDto> foundProductDtoList = simpleProductService.findAll();

        // then
        assertTrue(foundProductDtoList.stream().anyMatch(p -> p.getId().equals(savedProductDto1.getId())));
        assertTrue(foundProductDtoList.stream().anyMatch(p -> p.getId().equals(savedProductDto2.getId())));
    }

    //@Transactional
    @Test
    @DisplayName("이름에 특정 키워드가 포함된 상품만 조회되어야 하며, 검색 결과는 비어있지 않아야 한다.")
    void findByNameContainingTest() {
        // given
        simpleProductService.add(new ProductDto("참치김밥", 3000, 100));
        simpleProductService.add(new ProductDto("치즈김밥", 3200, 100));
        simpleProductService.add(new ProductDto("라면", 3500, 100));

        // when
        List<ProductDto> foundProductDtoList = simpleProductService.findByNameContaining("김밥");

        // then
        assertTrue(foundProductDtoList.size() > 0, "상품 이름에 '김밥'이 포함된 상품이 하나 이상 존재해야 한다.");
        assertTrue(foundProductDtoList.stream().allMatch(p -> p.getName().contains("김밥")),
                "검색된 모든 상품 이름이 '김밥'을 포함해야 한다.");
    }
}