package com.example.demo.repository;

import com.example.demo.model.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DataRepository extends JpaRepository<Data, Long> {
    @Query("select d from Data d order by d.time desc limit 1")
    Data getLastestResult();

}
