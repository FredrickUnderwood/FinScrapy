package com.chen.service;

import com.chen.dto.QueryRealTimeDataDTO;
import com.chen.entity.RealTimeData;

import java.util.List;

public interface RealTimeService {

    List<RealTimeData> queryRealTimeData(QueryRealTimeDataDTO queryRealTimeDataDTO);
}
