package parser

type EastMoneyRealTimeResponse struct {
	Data struct {
		FastNewsList []EastMoneyFastNews `json:"fastNewsList"`
	} `json:"data"`
}

type EastMoneyFastNews struct {
	Summary   string   `json:"summary"`
	Code      string   `json:"code"`
	Title     string   `json:"title"`
	ShowTime  string   `json:"showTime"`
	StockList []string `json:"stockList"`
	Image     []string `json:"image"`
}

type THSRealTimeResponse struct {
	Data struct {
		THSNewsList []THSNews `json:"list"`
	} `json:"data"`
}

type THSNews struct {
	Digest string `json:"digest"`
	Url    string `json:"url"`
	Ctime  string `json:"ctime"`
}

type IFengRealTimeResponse struct {
	IFengNewsList []IFengNews `json:"data"`
}

type IFengNews struct {
	Brief string `json:"brief"`
	Ctime string `json:"ctime"`
}

type WallStreetCNRealTimeResponse struct {
	Data struct {
		WallStreetCNNewsList []WallStreetCNNews `json:"items"`
	} `json:"data"`
}

type WallStreetCNNews struct {
	Title       string `json:"Title"`
	DisplayTime int64  `json:"display_time"`
	ContentText string `json:"content_text"`
	Id          int64  `json:"id"`
}
