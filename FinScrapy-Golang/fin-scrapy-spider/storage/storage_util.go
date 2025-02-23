package storage

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/config"
	"github.com/elastic/go-elasticsearch/v8"
	"github.com/elastic/go-elasticsearch/v8/esapi"
)

func createEsConn() (*elasticsearch.Client, error) {
	config, _ := config.LoadConfig()
	es, err := elasticsearch.NewClient(elasticsearch.Config{
		Addresses: []string{config.ElasticSearch.Address},
		Username:  config.ElasticSearch.Username,
		Password:  config.ElasticSearch.Password,
	})
	if err != nil {
		return nil, fmt.Errorf("%sNew ES client failed, error: %v", logPrefix, err)
	}
	return es, nil
}

func createEsIndex(indexName string) error {
	esClient, err := createEsConn()
	if err != nil {
		return err
	}
	mapping := map[string]interface{}{
		"mappings": map[string]interface{}{
			"properties": map[string]interface{}{
				"source": map[string]interface{}{
					"type": "keyword",
				},
				"title": map[string]interface{}{
					"type": "text",
				},
				"full_text": map[string]interface{}{
					"type": "text",
				},
				"timestamp": map[string]interface{}{
					"type": "keyword",
				},
			},
		},
	}
	mappingJson, err := json.Marshal(mapping)
	if err != nil {
		return fmt.Errorf("%sMarshal data failed, error: %v", logPrefix, err)
	}
	req := esapi.IndicesCreateRequest{
		Index: indexName,
		Body:  bytes.NewReader(mappingJson),
	}

	res, err := req.Do(context.Background(), esClient)
	if err != nil {
		return fmt.Errorf("%sDo ES create index request failed, error: %v", logPrefix, err)
	}
	defer res.Body.Close()
	fmt.Println(res.String())
	return nil
}

func getYesterdayEndTimestamp() int64 {
	loc, _ := time.LoadLocation("Asia/Shanghai")
	now := time.Now().In(loc)

	yesterday := now.AddDate(0, 0, -1)

	yesterdayEnd := time.Date(yesterday.Year(), yesterday.Month(), yesterday.Day(), 23, 59, 59, 0, yesterday.Location())
	return yesterdayEnd.UnixMilli()
}

func buildDocId(title string) string {
	if len(title) > 10 {
		return title[:10]
	}
	return title
}
