package main

import (
	"log"
	"os"
	"sync"

	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/pipeline"
)

func main() {
	os.Setenv("FIN_SCRAPY", "dev")

	var wg sync.WaitGroup

	wg.Add(1)
	go func() {
		defer wg.Done()
		err := pipeline.RunRealTimePipeline()
		if err != nil {
			log.Fatalf("run real time pipeline failed, error: %v", err)
		}
	}()

	wg.Add(1)
	go func() {
		defer wg.Done()
		err := pipeline.RunFullTextPipeline()
		if err != nil {
			log.Fatalf("run full text pipeline failed, error: %v", err)
		}
	}()

	wg.Wait()

}
