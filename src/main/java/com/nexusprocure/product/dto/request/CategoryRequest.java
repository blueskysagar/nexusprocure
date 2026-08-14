package com.nexusprocure.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequest {
    @NotBlank(message = "Category Name is required")
    @Size(max = 100, message = "Category Name must not exceed 100 characters")
    private String name;
    @Size(max = 500, message = "Category description must not exceed 500 characters")
    private String description;
    public CategoryRequest(){}
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }

}
