package com.example.demo.controller;

import com.example.demo.dto.SearchDataDto;
import com.example.demo.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/all-data")
public class AllDataController {
    @Autowired
    private DataService dataService;

    @RequestMapping()
    public String getAllDataView(Model model,
                                 @RequestParam(name = "start-date", required = false) String startDate,
                                 @RequestParam(name = "end-date", required = false) String endDate,
                                 @RequestParam(name = "temperature-value", required = false) String temperatureValue,
                                 @RequestParam(name = "humidity-value", required = false) String humidityValue,
                                 @RequestParam(name = "mq3-value", required = false) String mq3Value,
                                 @RequestParam(name = "mq4-value", required = false) String mq4Value,
                                 @RequestParam(name = "page", required = false) Integer pageIndex
                                 )
    {
        SearchDataDto searchDto = SearchDataDto.builder()
                                        .startDate(startDate)
                                        .endDate(endDate)
                                        .pageIndex(pageIndex)
                                        .temperatureValue(temperatureValue)
                                        .humidityValue(humidityValue)
                                        .mq3Value(mq3Value)
                                        .mq4Value(mq4Value)
                                        .build();

        this.dataService.getByCondition(searchDto);
        model.addAttribute("searchDto", searchDto);
        return "allData";
    }
}
