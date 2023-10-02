package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.ProductDto;
import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import com.ecommerce.library.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String products(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        List<ProductDto> productDtoList = productService.findAll();
        model.addAttribute("title", "新增產品");
        model.addAttribute("products", productDtoList);
        model.addAttribute("size", productDtoList.size());
        return "products";
    }

    @GetMapping("/add-product")
    public String addProductForm(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        List<Category> categories = categoryService.findAllByActivated();

        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductDto());
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute("product") ProductDto productDto,
                              @RequestParam("imageProduct") MultipartFile imageProduct,
                              RedirectAttributes attributes) {

        try {
            productService.save(imageProduct, productDto);
            attributes.addFlashAttribute("success", "產品 新增成功!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "產品 新增失敗!");
        }

        return "redirect:/products/0";
    }

    @GetMapping("/update-product/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        model.addAttribute("title", "產品更新!");

        List<Category> categories = categoryService.findAllByActivated();
        ProductDto productDto = productService.getById(id);

        model.addAttribute("categories", categories);
        model.addAttribute("productDto", productDto);

        return "update-product";
    }

    @PostMapping("/update-product/{id}")
    public String processUpdate(@PathVariable("id") Long id,
                                @ModelAttribute("productDto") ProductDto productDto,
                                @RequestParam("imageProduct") MultipartFile imageProduct,
                                RedirectAttributes attributes) {
        try {
            productService.update(imageProduct, productDto);
            attributes.addFlashAttribute("success", "產品 更新成功!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "產品 更新失敗!");
        }
        return "redirect:/products/0";

    }

    @RequestMapping(value = "/enable-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enabledProduct(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            productService.enableById(id);
            attributes.addFlashAttribute("success", "產品 已啟用成功!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "產品 已啟用失敗!");
        }
        return "redirect:/products";
    }

    @RequestMapping(value = "/delete-product/{id}", method = {RequestMethod.PUT, RequestMethod.GET})
    public String deletedProduct(@PathVariable("id") Long id, RedirectAttributes attributes) {
        try {
            productService.deleteById(id);
            attributes.addFlashAttribute("success", "產品 已停用成功!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("error", "產品 已停用失敗");
        }
        return "redirect:/products";
    }

    @GetMapping("/products/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        Page<ProductDto> products = productService.pageProducts(pageNo);
        model.addAttribute("title", "管理產品");
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/search-result/{pageNo}")
    public String productsPage(@PathVariable("pageNo") int pageNo, @RequestParam("keyword") String keyword,Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Page<ProductDto> products = productService.searchProducts(pageNo, keyword);

        model.addAttribute("title", "搜尋頁面");
        model.addAttribute("products", products);
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        return "result-products";
    }
}
