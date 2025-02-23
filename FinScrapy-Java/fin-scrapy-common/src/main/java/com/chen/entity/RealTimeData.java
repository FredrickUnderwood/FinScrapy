package com.chen.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeData {

    @JSONField(name = "Title")
    private String title;

    @JSONField(name = "Url")
    private String url;

    @JSONField(name = "Timestamp")
    private String Timestamp;

}
