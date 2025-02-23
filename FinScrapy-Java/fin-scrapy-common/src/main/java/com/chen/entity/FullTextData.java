package com.chen.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FullTextData {

    @JsonProperty("source")
    private String source;

    @JsonProperty("title")
    private String title;

    @JsonProperty("full_text")
    private String fullText;

    @JsonProperty("timestamp")
    private String timestamp;

}
