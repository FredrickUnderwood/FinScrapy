package pipeline

import (
	"log"
	"sync"
	"time"

	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/config"
	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/downloader"
	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/parser"
	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/storage"
)

func realTimePipeline(url string, source string) error {
	url, err := fillingUrl(url, source)
	if err != nil {
		return err
	}
	body, err := downloader.DownloadPage(url)
	if err != nil {
		return err
	}
	parser := parser.RouteRealTimeParser(source)
	realTimeData, err := parser(body)
	if err != nil {
		return err
	}
	err = storage.StoreRealTimeDataToRedis(realTimeData, source)
	if err != nil {
		return err
	}
	return nil
}

func RunRealTimePipeline() error {
	config, err := config.LoadConfig()
	if err != nil {
		return err
	}
	ticker := time.NewTicker(time.Duration(config.RealTimeTickerInterval) * time.Second)
	defer ticker.Stop()

	for range ticker.C {
		var wg sync.WaitGroup
		for _, urlSourcePair := range config.RealTimeScrapySource {
			url, source := urlSourcePair.Url, urlSourcePair.Source
			wg.Add(1)
			go func(url string, source string) {
				defer wg.Done()
				err := realTimePipeline(url, source)
				if err != nil {
					log.Printf("real time pipeline failed for source: %s, error: %v", source, err)
				}
			}(url, source)
		}
		wg.Wait()
	}
	return nil

}
