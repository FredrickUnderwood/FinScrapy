package parser

import (
	"strings"

	"golang.org/x/net/html"
)

func getClassAttribute(node *html.Node) string {
	for _, attr := range node.Attr {
		if attr.Key == "class" {
			return attr.Val
		}
	}
	return ""
}

func getIdAttribute(node *html.Node) string {
	for _, attr := range node.Attr {
		if attr.Key == "id" {
			return attr.Val
		}
	}
	return ""
}

func getAttribute(node *html.Node, key string) string {
	for _, attr := range node.Attr {
		if attr.Key == key {
			return attr.Val
		}
	}
	return ""
}

func getDivByClass(node *html.Node, classname string) *html.Node {
	if node.Type == html.ElementNode && node.Data == "div" && getClassAttribute(node) == classname {
		return node
	}
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		if result := getDivByClass(c, classname); result != nil {
			return result
		}
	}
	return nil
}

func getTextByNode(node *html.Node) string {
	var builder strings.Builder
	if node.Type == html.TextNode {
		builder.WriteString(node.Data)
	}
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		builder.WriteString(getTextByNode(c))
	}
	return builder.String()
}

func getDivById(node *html.Node, id string) *html.Node {
	if node.Type == html.ElementNode && node.Data == "div" && getIdAttribute(node) == id {
		return node
	}
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		if result := getDivById(c, id); result != nil {
			return result
		}
	}
	return nil
}

func getH2ByClass(node *html.Node, classname string) *html.Node {
	if node.Type == html.ElementNode && node.Data == "h2" && getClassAttribute(node) == classname {
		return node
	}
	for c := node.FirstChild; c != nil; c = c.NextSibling {
		if result := getH2ByClass(c, classname); result != nil {
			return result
		}
	}
	return nil
}
