package downloader

import (
	"fmt"
	"io"
	"net/http"
)

func DownloadPage(url string) ([]byte, error) {
	resp, err := http.Get(url)
	if err != nil {
		return nil, fmt.Errorf("%sGet page from url: {%s} failed, error: %v", logPrefix, url, err)
	}
	defer resp.Body.Close()
	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, fmt.Errorf("%sRead body from url: {%s} failed, error: %v", logPrefix, url, err)
	}
	return body, nil
}
