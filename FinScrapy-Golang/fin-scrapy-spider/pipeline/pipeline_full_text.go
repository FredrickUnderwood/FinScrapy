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

func fullTextPipeline(source string) error {
	realTimeDataList, err := storage.FetchRealTimeDataFromRedis(source)
	if err != nil {
		return err
	}
	for _, realTimeData := range realTimeDataList {
		var body []byte
		if realTimeData.Url != "" {
			body, err = downloader.DownloadPage(realTimeData.Url)
			if err != nil {
				return err
			}
		}
		parser := parser.RouteFullTextParser(source)
		fullTextData, err := parser(body, realTimeData)
		if err != nil {
			return err
		}
		err = storage.StoreFullTextToES(fullTextData, source)
		if err != nil {
			return err
		}
	}
	return nil
}

func RunFullTextPipeline() error {
	config, err := config.LoadConfig()
	if err != nil {
		return err
	}
	for {
		sleepTime := getSleepTimeUntilRunFullTextPipeline()
		time.Sleep(sleepTime)
		var wg sync.WaitGroup
		for _, urlSourcePair := range config.RealTimeScrapySource {
			wg.Add(1)
			go func(source string) {
				defer wg.Done()
				err := fullTextPipeline(source)
				if err != nil {
					log.Printf("full text pipeline failed for source: %s, error: %v", source, err)
				}
			}(urlSourcePair.Source)
		}
		wg.Wait()

	}
}
