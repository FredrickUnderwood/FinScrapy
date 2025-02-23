package storage

import (
	"context"
	"encoding/json"
	"fmt"
	"time"

	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/config"
	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/model"
	"github.com/go-redis/redis/v8"
)

func StoreRealTimeDataToRedis(realTimeData []model.RealTimeData, source string) error {
	ctx := context.Background()
	loc, _ := time.LoadLocation("Asia/Shanghai")
	config, err := config.LoadConfig()
	if err != nil {
		return err
	}
	client := redis.NewClient(&redis.Options{
		Addr: config.Redis.Address,
		DB:   config.Redis.DB,
	})
	setKey := source + "_real_time_data"
	for _, data := range realTimeData {
		jsonData, err := json.Marshal(data)
		if err != nil {
			return fmt.Errorf("%sMarshal data failed, error: %v", logPrefix, err)
		}
		timestamp, err := time.ParseInLocation("2006-01-02 15:04:05", data.Timestamp, loc)
		// 不存入前一日的新闻进Redis
		if timestamp.UnixMilli() < getYesterdayEndTimestamp() {
			continue
		}
		if err != nil {
			return fmt.Errorf("%sParse timestamp failed, error: %v", logPrefix, err)
		}
		score := float64(timestamp.UnixMilli())
		err = client.ZAdd(ctx, setKey, &redis.Z{
			Score:  score,
			Member: jsonData,
		}).Err()
		if err != nil {
			return fmt.Errorf("%sExecute ZADD failed, error: %v", logPrefix, err)
		}
	}
	return nil
}
