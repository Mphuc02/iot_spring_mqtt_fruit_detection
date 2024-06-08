package com.example.demo.service;

import com.example.demo.model.Data;
import com.example.demo.dto.DataDto;
import com.example.demo.dto.SearchDataDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataService {
    List<DataDto> get7LastestResult();
    List<DataDto> getAllResult();
    void getByCondition(SearchDataDto searchDataDto);
    void saveData(Data data);
}
