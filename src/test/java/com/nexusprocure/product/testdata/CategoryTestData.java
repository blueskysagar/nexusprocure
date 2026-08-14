package com.nexusprocure.product.testdata;

import com.nexusprocure.product.entity.Category;
import com.nexusprocure.product.enums.CategoryStatus;

import java.util.concurrent.atomic.AtomicInteger;

public final class CategoryTestData {
    private CategoryTestData(){}
    private static final String DEFAULT_NAME = "Electronics";
    private static final AtomicInteger counter = new AtomicInteger();
    private static final String DEFAULT_DESCRIPTION = "Electronic Products";
    private static final CategoryStatus DEFAULT_STATUS = CategoryStatus.ACTIVE;

    public static Category entity() {

        Category category = new Category();

        category.setName(
                "Electronics_" + counter.incrementAndGet()
        );
        category.setDescription(DEFAULT_DESCRIPTION);
        category.setStatus(DEFAULT_STATUS);
        return category;
    }
}
