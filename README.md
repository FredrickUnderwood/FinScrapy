# FinScrapy 财经资讯爬虫分析软件

目前支持数据源:
- 同花顺财经: https://www.10jqka.com.cn
- 东方财富网: https://www.eastmoney.com
- 凤凰网财经: https://finance.ifeng.com
- 华尔街见闻: https://wallstreetcn.com

目前支持的功能:
- 实时爬取资讯Title或Summary到Redis
- 存量爬取资讯全文到ElasticSearch
- 包含订阅关键词的资讯实时推送到邮箱(需要调用[Bitten服务](https://github.com/FredrickUnderwood/bitten))
- 接入DeepSeek API实现基于关键词的预测和复盘

技术
- Middleware
  - Redis
  - Kafka
  - ElasticSearch
- Database
  - MySQL
- Framework
  - SpringBoot 3
  - SpringCloud
- Lang
  - Java
  - Golang
  - Lua
