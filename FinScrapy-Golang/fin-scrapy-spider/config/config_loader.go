package config

import (
	"fmt"
	"os"

	"gopkg.in/yaml.v3"
)

type RedisConfig struct {
	Address string `yaml:"address"`
	DB      int    `yaml:"database"`
}

type ScrapySourceConfig struct {
	Url    string `yaml:"url"`
	Source string `yaml:"source"`
}

type ElasticSearchConfig struct {
	Address  string `yaml:"address"`
	Username string `yaml:"username"`
	Password string `yaml:"password"`
}

type Config struct {
	Redis                  RedisConfig          `yaml:"redis"`
	RealTimeScrapySource   []ScrapySourceConfig `yaml:"real_time_scrapy_source"`
	RealTimeTickerInterval int                  `yaml:"real_time_ticker_interval"`
	ElasticSearch          ElasticSearchConfig  `yaml:"elasticsearch"`
}

func LoadConfig() (Config, error) {
	currentEnv := os.Getenv("FIN_SCRAPY")
	var configPath string
	if currentEnv == "dev" {
		configPath = "dev_config.yaml"
	} else if currentEnv == "test" {
		configPath = "test_config.yaml"
	} else {
		configPath = "prod_config.yaml"
	}
	// TODO 修改ConfigPath
	configPath = "./config/" + configPath
	configFile, err := os.ReadFile(configPath)
	if err != nil {
		return Config{}, fmt.Errorf("%sRead config file from %s failed, error: %v", logPrefix, configPath, err)
	}
	var config Config
	err = yaml.Unmarshal(configFile, &config)
	if err != nil {
		return Config{}, fmt.Errorf("%sUnmarshal config from %s failed, error: %v", logPrefix, configPath, err)
	}
	return config, nil
}
