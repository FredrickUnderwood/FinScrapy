package com.chen.vo;

import com.chen.entity.FullTextData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullTextDataVO {

    private String keyword;

    private FullTextData fullTextData;

}
