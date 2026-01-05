package com.example.shopon.repository;

import com.example.shopon.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Set<Category> findByIdIn(Set<Long> ids);
}
