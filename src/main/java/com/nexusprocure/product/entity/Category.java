package com.nexusprocure.product.entity;

import com.nexusprocure.common.base.BaseEntity;
import com.nexusprocure.product.enums.CategoryStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories", uniqueConstraints = {@UniqueConstraint(name = "uk_category_name", columnNames = "name")})
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Category Name is Required")
    @Size(max = 100, message = "Category Name must not exceed more than 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "description", length = 500)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CategoryStatus status;
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();
    public Category(){}
    public Long getId(){
        return id;
    }
    public void setId(Long id){this.id = id;}
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public CategoryStatus getStatus(){
        return status;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setStatus(CategoryStatus status){
        this.status = status;
    }


}
