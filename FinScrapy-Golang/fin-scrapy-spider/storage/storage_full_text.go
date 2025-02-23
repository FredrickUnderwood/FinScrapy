package storage

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"strconv"

	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/config"
	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/model"
	"github.com/elastic/go-elasticsearch/v8/esapi"
	"github.com/go-redis/redis/v8"
)

func FetchRealTimeDataFromRedis(source string) ([]model.RealTimeData, error) {
	config, err := config.LoadConfig()
	if err != nil {
		return nil, err
	}
	client := redis.NewClient(&redis.Options{
		Addr: config.Redis.Address,
		DB:   config.Redis.DB,
	})
	key := source + "_real_time_data"
	maxScore := strconv.FormatInt(getYesterdayEndTimestamp(), 10)
	zRangeCmd := client.ZRangeByScore(context.Background(), key, &redis.ZRangeBy{
		Min: "-inf",
		Max: maxScore,
	})
	results, err := zRangeCmd.Result()
	if err != nil {
		return nil, fmt.Errorf("%sZRangeByScore failed, error: %v", logPrefix, err)
	}
	var realTimeDataList []model.RealTimeData
	for _, result := range results {
		var data model.RealTimeData
		err := json.Unmarshal([]byte(result), &data)
		if err != nil {
			return nil, fmt.Errorf("%sUnmarshal real time data from redis failed, error: %v", logPrefix, err)
		}
		realTimeDataList = append(realTimeDataList, data)
	}
	remCmd := client.ZRemRangeByScore(context.Background(), key, "-inf", maxScore)
	if _, err := remCmd.Result(); err != nil {
		return nil, fmt.Errorf("%sZRemRangeByScore failed, error: %v", logPrefix, err)
	}
	return realTimeDataList, nil
}

func StoreFullTextToES(fullTextData model.FullTextData, source string) error {
	esClient, err := createEsConn()
	if err != nil {
		return fmt.Errorf("%sCreate ES connection failed, error: %v", logPrefix, err)
	}
	doc := map[string]interface{}{
		"source":    source,
		"title":     fullTextData.Title,
		"full_text": fullTextData.Text,
		"timestamp": fullTextData.Timestamp,
	}
	docJson, err := json.Marshal(doc)
	if err != nil {
		return fmt.Errorf("%sMarshal data for ES failed, error: %v", logPrefix, err)
	}

	docId := fmt.Sprintf("%s_%s_%s", source, fullTextData.Timestamp, buildDocId(fullTextData.Title))

	indexName := "full_text_index"
	req := esapi.IndexRequest{
		DocumentID: docId,
		Index:      indexName,
		Body:       bytes.NewReader(docJson),
		Refresh:    "true",
	}
	res, err := req.Do(context.Background(), esClient)
	if err != nil {
		return fmt.Errorf("%sDo ES create document request failed, error: %v", logPrefix, err)
	}
	defer res.Body.Close()

	return nil
}
