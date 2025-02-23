package parser

import (
	"encoding/json"
	"fmt"
	"log"
	"strconv"
	"strings"
	"time"

	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/model"
)

type RealTimeParser func(body []byte) ([]model.RealTimeData, error)

func eastMoneyRealTimeParser(body []byte) ([]model.RealTimeData, error) {
	results := make([]model.RealTimeData, 0)
	var response EastMoneyRealTimeResponse
	if err := json.Unmarshal(body, &response); err != nil {
		return nil, fmt.Errorf("%sUnmarshal body from East Money failed, error: %v", logPrefix, err)
	}
	for _, news := range response.Data.FastNewsList {
		var result model.RealTimeData
		result.Title = news.Summary
		result.Timestamp = news.ShowTime // "2025-02-14 16:54:28"
		result.Url = fmt.Sprintf(eastMoneyRealTimeDetailBaseUrl, news.Code)
		results = append(results, result)
	}

	return results, nil
}

func thsRealTimeParser(body []byte) ([]model.RealTimeData, error) {
	loc, _ := time.LoadLocation("Asia/Shanghai")
	results := make([]model.RealTimeData, 0)
	var response THSRealTimeResponse
	if err := json.Unmarshal(body, &response); err != nil {
		return nil, fmt.Errorf("%sUnmarshal body from Tong Hua Shun failed, error: %v", logPrefix, err)
	}
	for _, news := range response.Data.THSNewsList {
		var result model.RealTimeData
		result.Title = news.Digest
		result.Url = news.Url
		unixTimestamp, err := strconv.ParseInt(news.Ctime, 10, 64)
		if err != nil {
			log.Printf("%sBuild unix timestamp failed, error: %v", logPrefix, err)
		}
		result.Timestamp = time.Unix(unixTimestamp, 0).In(loc).Format("2006-01-02 15:04:05")
		results = append(results, result)
	}
	return results, nil
}

func ifengRealTimeParser(body []byte) ([]model.RealTimeData, error) {
	loc, _ := time.LoadLocation("Asia/Shanghai")
	results := make([]model.RealTimeData, 0)
	var response IFengRealTimeResponse
	if err := json.Unmarshal(body, &response); err != nil {
		return nil, fmt.Errorf("%sUnmarshal body from Feng Huang Wang failed, error: %v", logPrefix, err)
	}
	for _, news := range response.IFengNewsList {
		var result model.RealTimeData
		result.Title = news.Brief
		unixTimestamp, err := strconv.ParseInt(news.Ctime, 10, 64)
		if err != nil {
			log.Printf("%sBuild unix timestamp failed, error: %v", logPrefix, err)
		}
		result.Timestamp = time.Unix(unixTimestamp, 0).In(loc).Format("2006-01-02 15:04:05")
		results = append(results, result)
	}
	return results, nil
}

func wallStreetCNRealTimeParser(body []byte) ([]model.RealTimeData, error) {
	loc, _ := time.LoadLocation("Asia/Shanghai")
	results := make([]model.RealTimeData, 0)
	var response WallStreetCNRealTimeResponse
	if err := json.Unmarshal(body, &response); err != nil {
		return nil, fmt.Errorf("%sUnmarshal body from Wall Street CN failed, error: %v", logPrefix, err)
	}
	for _, news := range response.Data.WallStreetCNNewsList {
		var result model.RealTimeData
		var title strings.Builder
		if news.Title != "" {
			title.WriteString("【")
			title.WriteString(news.Title)
			title.WriteString("】")
			title.WriteString(news.ContentText)
			result.Title = title.String()
		} else {
			result.Title = news.ContentText
		}

		result.Timestamp = time.Unix(news.DisplayTime, 0).In(loc).Format("2006-01-02 15:04:05")
		// result.Url = fmt.Sprintf(wallStreetCNRealTimeDetailBaseUrl, strconv.FormatInt(news.Id, 10))
		results = append(results, result)
	}
	return results, nil
}

func RouteRealTimeParser(source string) RealTimeParser {
	switch source {
	case "east_money":
		return eastMoneyRealTimeParser
	case "ths":
		return thsRealTimeParser
	case "ifeng":
		return ifengRealTimeParser
	case "wall_street_cn":
		return wallStreetCNRealTimeParser
	default:
		return nil
	}
}
