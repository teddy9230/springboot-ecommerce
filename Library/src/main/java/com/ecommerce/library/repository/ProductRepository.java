package com.ecommerce.library.repository;


import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p")
    Page<Product> pageProduct(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.description LIKE %?1% OR p.name LIKE %?1%")
    Page<Product> searchProducts(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.description LIKE %?1% OR p.name LIKE %?1%")
    List<Product> searchProductsList(String keyword);

//  Customer
    @Query("SELECT p FROM Product p WHERE p.is_activated = TRUE AND p.is_deleted = FALSE")
    List<Product> getAllProducts();

    @Query(value = "SELECT * FROM products p WHERE p.is_deleted = FALSE AND p.is_activated = TRUE order by rand() ASC limit 4 ", nativeQuery = true)
    List<Product> listViewProducts();

    @Query(value = "SELECT * FROM products p Inner JOIN categories c ON c.category_id = p.category_id WHERE p.category_id = ?1", nativeQuery = true)
    List<Product> getRelatedProducts(Long categoryId);

    @Query(value = "SELECT p FROM Product p Inner JOIN Category c ON c.id = p.category.id WHERE c.id = ?1 AND p.is_deleted = FALSE AND p.is_activated = TRUE")
    List<Product> getProductsInCategory(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.is_activated = TRUE AND p.is_deleted = FALSE" +
            " ORDER BY p.costPrice DESC ")
    List<Product> filterHighPrice();

    @Query("SELECT p FROM Product p WHERE p.is_activated = TRUE AND p.is_deleted = FALSE ORDER BY p.costPrice ")
    List<Product> filterLowPrice();
}
