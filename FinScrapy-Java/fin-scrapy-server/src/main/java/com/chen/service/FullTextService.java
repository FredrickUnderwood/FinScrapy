package com.chen.service;

import com.chen.dto.QueryFullTextDataDTO;
import com.chen.entity.FullTextData;
import com.chen.vo.FullTextDataVO;

import java.util.List;

public interface FullTextService {

    List<FullTextData> queryFullTextSpecifiedSource(QueryFullTextDataDTO queryFullTextDataDTO);

}
