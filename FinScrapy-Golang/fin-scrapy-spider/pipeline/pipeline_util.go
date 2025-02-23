package pipeline

import (
	"fmt"
	"strconv"
	"time"
)

func fillingUrl(url string, source string) (string, error) {
	loc, _ := time.LoadLocation("Asia/Shanghai")
	var filledUrl string
	switch source {
	case "east_money":
		filledUrl = fmt.Sprintf(url, strconv.FormatInt(time.Now().In(loc).UnixMilli(), 10))
	case "ths":
		filledUrl = url
	case "ifeng":
		filledUrl = fmt.Sprintf(url, strconv.FormatInt(time.Now().In(loc).Unix(), 10))
	case "wall_street_cn":
		filledUrl = url
	default:
		return "", fmt.Errorf("unknown source: %s", source)
	}
	return filledUrl, nil
}

func getSleepTimeUntilRunFullTextPipeline() time.Duration {
	loc, _ := time.LoadLocation("Asia/Shanghai")
	now := time.Now().In(loc)
	nextRun := time.Date(now.Year(), now.Month(), now.Day(), 0, 30, 0, 0, now.Location())
	if now.After(nextRun) {
		nextRun = nextRun.AddDate(0, 0, 1)
	}
	return nextRun.Sub(now)
}
