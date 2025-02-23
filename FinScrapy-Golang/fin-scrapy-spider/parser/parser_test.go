package parser

import (
	"fmt"
	"testing"

	"github.com/FredrickUnderwood/FinScrapy/fin-scrapy-spider/downloader"
)

func TestParser(t *testing.T) {
	doc, _ := downloader.DownloadPage("https://api-one-wscn.awtmt.com/apiv1/content/lives?channel=global-channel&client=pc&limit=50&first_page=true&accept=live")
	results, _ := wallStreetCNRealTimeParser(doc)
	fmt.Println(results[3].Title)
	fmt.Println(results[3].Timestamp)
	fmt.Println(results[3].Url)
	// re := regexp.MustCompile(`【(.*?)】`)
	// test_txt := "【硅业分会：本周多晶硅价格持稳 n型复投料成交均价为4.17万元/吨】硅业分会消息，本周多晶硅价格持稳，n型复投料成交价格区间为3.90-4.60万元/吨，成交均价为4.17万元/吨，n型颗粒硅成交价格区间为3.80-4.10万元/吨，成交均价为3.90万元/吨，p型多晶硅成交价区间为3.20-3.60万元/吨，成交均价为3.40万元/吨。本周大部分多晶硅企业基本已签完本月全部订单，目前以交付为主。本周n型多晶硅有成交的企业数量为7家。价格方面，本周多晶硅企业涨价呼声有所减弱，受下游产品价格松动预期影响，多晶硅企业现阶段以稳价为主，区间高价订单较多为期现商及贸易商采购成交，"
	// matches := re.FindStringSubmatch(test_txt)
	// title := matches[1]
	// text := strings.TrimSpace(re.ReplaceAllString(test_txt, ""))
	// fmt.Println(title)
	// fmt.Println(text)
}
