package com.chen.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import com.chen.constant.FinScrapyConstant;
import com.chen.dto.QueryFullTextDataDTO;
import com.chen.entity.FullTextData;
import com.chen.vo.FullTextDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FullTextESMapper {

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public List<FullTextDataVO> searchFullTextDataForAnalyzer(QueryFullTextDataDTO queryFullTextDataDTO, String logPrefix) {
        List<FullTextDataVO> result = new ArrayList<>();
        for (String keyword: List.of(queryFullTextDataDTO.getKeywords().split(","))) {
            BoolQuery boolQuery = BoolQuery.of(b -> b
                    .must(m -> m.term(t -> t.field("source").value(queryFullTextDataDTO.getSource())))
                    .filter(f -> f.range(r -> r.date(d -> d.field("timestamp")
                            .gte(queryFullTextDataDTO.getStartTime()).lte(queryFullTextDataDTO.getEndTime()))))
                    .should(List.of(
                            MatchPhraseQuery.of(m -> m.field("title").query(keyword))._toQuery(),
                            MatchPhraseQuery.of(m -> m.field("full_text").query(keyword))._toQuery()))
                    .minimumShouldMatch("1"));
            SearchRequest request = SearchRequest.of(s -> s
                    .scroll(t -> t.time("1m"))
                    .index(FinScrapyConstant.FULL_TEXT_ES_INDEX)
                    .query(Query.of(q -> q.bool(boolQuery)))
                    .sort(sort -> sort.field(f -> f.field("timestamp").order(SortOrder.Desc))));
            try {
                SearchResponse<FullTextData> initialResponse = elasticsearchClient.search(request, FullTextData.class);
                if (initialResponse.hits() != null && !initialResponse.hits().hits().isEmpty()) {
                    String scrollId = initialResponse.scrollId();
                    initialResponse.hits().hits().forEach(fullTextDataHit -> result.add(new FullTextDataVO(keyword, fullTextDataHit.source())));
                    while (true) {
                        ScrollRequest scrollRequest = new ScrollRequest.Builder()
                                .scrollId(scrollId)
                                .scroll(t -> t.time("1m"))
                                .build();
                        ScrollResponse<FullTextData> response = elasticsearchClient.scroll(scrollRequest, FullTextData.class);
                        if (response.hits() == null || response.hits().hits().isEmpty()) break;
                        response.hits().hits().forEach(fullTextDataHit -> result.add(new FullTextDataVO(keyword, fullTextDataHit.source())));
                        scrollId = response.scrollId();
                    }
                    clearScrollContext(scrollId);
                }

            } catch (IOException e) {
                log.error("{}Search full text from elasticsearch failed!", logPrefix, e);
            }
        }
        return result;
    }

    public List<FullTextData> searchFullTextDataForServer(QueryFullTextDataDTO queryFullTextDataDTO, String logPrefix) {
        List<FullTextData> result = new ArrayList<>();

        BoolQuery boolQuery = BoolQuery.of(b -> b
                .must(m -> m.term(t -> t.field("source").value(queryFullTextDataDTO.getSource()))));
        SearchRequest request = SearchRequest.of(s -> s
                .index(FinScrapyConstant.FULL_TEXT_ES_INDEX)
                .query(Query.of(q -> q.bool(boolQuery)))
                .sort(sort -> sort.field(f -> f.field("timestamp").order(SortOrder.Desc)))
                .from((queryFullTextDataDTO.getPage() - 1) * queryFullTextDataDTO.getSize())
                .size(queryFullTextDataDTO.getSize()));
        try {
            SearchResponse<FullTextData> response = elasticsearchClient.search(request, FullTextData.class);
            response.hits().hits().forEach(fullTextData -> result.add(fullTextData.source()));
        } catch (IOException e) {
            log.error("{}Search full text from elasticsearch failed!", logPrefix, e);
        }
        return result;
    }

    private void clearScrollContext(String scrollId) throws IOException {
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest.Builder()
                .scrollId(List.of(scrollId)).build();
        elasticsearchClient.clearScroll(clearScrollRequest);
    }
}
