package parser

import (
	"bytes"
	"fmt"
	"regexp"
	"strings"

	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/model"
	"golang.org/x/net/html"
	"golang.org/x/text/encoding/simplifiedchinese"
)

type FullTextParser func(body []byte, realTimeData model.RealTimeData) (model.FullTextData, error)

func eastMoneyFullTextParser(body []byte, realTimeData model.RealTimeData) (model.FullTextData, error) {
	doc, err := html.Parse(strings.NewReader(string(body)))
	if err != nil {
		return model.FullTextData{}, fmt.Errorf("%sParse html from East Money failed, error: %v", logPrefix, err)
	}
	var fullText string
	var title string
	fullTextNode := getDivByClass(doc, "txtinfos")
	if fullTextNode != nil {
		fullText = getTextByNode(fullTextNode)
	} else {
		fullText = ""
	}
	titleNode := getDivByClass(doc, "title")
	if titleNode != nil {
		title = getTextByNode(titleNode)
	} else {
		title = realTimeData.Title
	}
	return model.FullTextData{
		Title:     title,
		Text:      strings.TrimSpace(fullText),
		Timestamp: realTimeData.Timestamp,
	}, nil
}

func thsFullTextParser(body []byte, realTimeData model.RealTimeData) (model.FullTextData, error) {
	reader := simplifiedchinese.GBK.NewDecoder().Reader(bytes.NewReader(body))
	doc, err := html.Parse(reader)
	if err != nil {
		return model.FullTextData{}, fmt.Errorf("%sParse html from Tong Hua Shun failed, error: %v", logPrefix, err)
	}
	var fullText string
	var title string
	fullTextNode := getDivById(doc, "contentApp")
	if fullTextNode != nil {
		fullText = getTextByNode(fullTextNode)
	} else {
		fullText = ""
	}
	titleNode := getH2ByClass(doc, "main-title")
	if titleNode != nil {
		title = getTextByNode(titleNode)
	} else {
		title = realTimeData.Title
	}
	return model.FullTextData{
		Title:     title,
		Text:      strings.TrimSpace(fullText),
		Timestamp: realTimeData.Timestamp,
	}, nil
}

func ifengFullTextParser(_ []byte, realTimeData model.RealTimeData) (model.FullTextData, error) {
	re := regexp.MustCompile(`【(.*?)】`)
	matches := re.FindStringSubmatch(realTimeData.Title)
	var title string
	var text string
	if len(matches) < 2 {
		title = realTimeData.Title
		text = ""
	} else {
		title = matches[1]
		text = strings.TrimSpace(re.ReplaceAllString(realTimeData.Title, ""))
	}
	return model.FullTextData{
		Title:     title,
		Text:      text,
		Timestamp: realTimeData.Timestamp,
	}, nil
}

func wallStreetCNFullTextParser(_ []byte, realTimeData model.RealTimeData) (model.FullTextData, error) {
	re := regexp.MustCompile(`【(.*?)】`)
	matches := re.FindStringSubmatch(realTimeData.Title)
	var title string
	var text string
	if len(matches) < 2 {
		title = realTimeData.Title
		text = ""
	} else {
		title = matches[1]
		text = strings.TrimSpace(re.ReplaceAllString(realTimeData.Title, ""))
	}
	return model.FullTextData{
		Title:     title,
		Text:      text,
		Timestamp: realTimeData.Timestamp,
	}, nil
}

func RouteFullTextParser(source string) FullTextParser {
	switch source {
	case "east_money":
		return eastMoneyFullTextParser
	case "ths":
		return thsFullTextParser
	case "ifeng":
		return ifengFullTextParser
	case "wall_street_cn":
		return wallStreetCNFullTextParser
	default:
		return nil
	}
}
