package com.ecommerce.admin.controller;

import com.ecommerce.library.model.Category;
import com.ecommerce.library.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String categories(Model model, Principal principal) {

        if (principal == null){
            return  "redirect:/login";
        }

        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("size", categories.size());
        model.addAttribute("title", "產品類別");
        model.addAttribute("categoryNew", new Category());

        return "categories";
    }

    @PostMapping("/add-category")
    public String add(@ModelAttribute("categoryNew") Category category,
                      RedirectAttributes attributes) {
        try {
            categoryService.save(category);
            attributes.addFlashAttribute("success", "新增成功!");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "新增失敗，名稱重複!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "新增失敗!");
        }

        return "redirect:/categories";
    }

    @RequestMapping(value = "/findById", method = { RequestMethod.PUT, RequestMethod.GET })
    @ResponseBody
    public Category findById(Long id) {
        return categoryService.findById(id);
    }

    @GetMapping("/update-category")
    public String update(Category category, RedirectAttributes attributes){
        try{
            categoryService.update(category);
            attributes.addFlashAttribute("success", "分類名稱 更新成功!");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "更新失敗，名稱重複!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "更新失敗!");
        }

        return "redirect:/categories";
    }

    @GetMapping("/delete-category")
    public String delete(Long id, RedirectAttributes attributes) {
        try {
            categoryService.deleteById(id);
            attributes.addFlashAttribute("success", "分類 停用成功!");
        } catch (Exception e) {
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "分類 停用失敗!");
        }

        return "redirect:/categories";
    }

    @RequestMapping(value = "/enable-category", method = {RequestMethod.PUT, RequestMethod.GET})
    public String enable(Long id, RedirectAttributes attributes){
        try {
            categoryService.enabledById(id);
            attributes.addFlashAttribute("success", "分類 啟用成功!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("failed", "分類 啟用失敗!");
        }
        return "redirect:/categories";
    }
}
