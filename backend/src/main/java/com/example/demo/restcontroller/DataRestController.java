package com.example.demo.restcontroller;

import com.example.demo.dto.SearchDataDto;
import com.example.demo.service.DataService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/data")
public class DataRestController {
    private final DataService dataService;

    @RequestMapping()
    public ResponseEntity<Object> get7LastestResult()
    {
        return ResponseEntity.ok(this.dataService.get7LastestResult());
    }

    @PostMapping("/get-by-condition")
    public ResponseEntity<Object> getByCondition(@RequestBody String searchDataJson)
    {
        SearchDataDto searchDataDto = (new Gson()).fromJson(searchDataJson, SearchDataDto.class);
        this.dataService.getByCondition(searchDataDto);
        if (!searchDataDto.isValid())
            return ResponseEntity.badRequest().body(searchDataDto.getMessages());
        return ResponseEntity.ok(searchDataDto);
    }

}
