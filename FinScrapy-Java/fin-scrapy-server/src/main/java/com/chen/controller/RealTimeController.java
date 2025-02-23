package com.chen.controller;

import com.chen.dto.QueryRealTimeDataDTO;
import com.chen.entity.RealTimeData;
import com.chen.service.RealTimeService;
import com.chen.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/real_time")
public class RealTimeController {

    @Autowired
    private RealTimeService realTimeService;

    @PostMapping("/query")
    public ResultVO<List<RealTimeData>> queryRealTimeData(@RequestBody QueryRealTimeDataDTO queryRealTimeDataDTO) {
        return ResultVO.success(realTimeService.queryRealTimeData(queryRealTimeDataDTO));
    }


}
