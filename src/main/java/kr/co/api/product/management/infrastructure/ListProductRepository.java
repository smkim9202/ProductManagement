package kr.co.api.product.management.infrastructure;

import kr.co.api.product.management.domain.EntityNotFoundException;
import kr.co.api.product.management.domain.Product;
import kr.co.api.product.management.presentation.ProductDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ListProductRepository {

    // CopyOnWriteArrayList는 멀티 스레드라는 특수한 환경 때문에 Thread Safety 컬렉션
    // 지역변수나 매개변수로 전달되는 리스트의 경우 하나의 스레드에만 접급하기 때문에 안정성이 필요하지 않아 ArrayList 많이 사용
    private List<Product> products = new CopyOnWriteArrayList<>();
    private AtomicLong sequence = new AtomicLong(1L);

    public Product add(Product product) {
        product.setId(sequence.getAndAdd(1L));

        products.add(product);
        return product;
    }

    public Product findById2(Long id){
        return products.stream().filter(product -> product.sameId(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Product를 찾지 못했습니다."));
    }

    public Product findById(Long id){
        Product foundProduct = null;
        for (Product product : products) {
            if (product.sameId(id)) {
                foundProduct = product;
                break;
            }
        }
        if (foundProduct == null) {
            throw new EntityNotFoundException("Product를 찾지 못했습니다.");
        }
         return foundProduct;
    }

    public List<Product> findAll() {
        return products;
    }

    public List<Product> findByNameContaining2(String name){
        return products.stream()
                .filter(product -> product.containsName(name)).toList();
    }

    public List<Product> findByNameContaining(String name){
        List<Product> foundProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.containsName(name)) {
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }

    public Product update(Product product){
        Integer indexToModify = products.indexOf(product);
        products.set(indexToModify, product);
        return product;
    }

    public void delete(Long id){
        Product product = this.findById(id);
        products.remove(product);
    }


}
