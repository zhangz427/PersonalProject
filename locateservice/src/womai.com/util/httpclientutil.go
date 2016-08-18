package util

import (
	//Golang 标准库
	"errors"
	"io/ioutil"
	"net/http"
	"strings"
	"time"

	//第三访库
	log "github.com/cihub/seelog"
)

const (
	netWorkErrorMessage = "网络调用异常"
	netWorkTimeOut      = 5
)

func SendPostRequest(reqParaMap map[string]string, url string, logContext string) (string, error) {

	var err error
	http.DefaultClient.Timeout = time.Second * netWorkTimeOut
	requestbody := strings.NewReader(convertMapToString(reqParaMap, logContext))

	resp, err := http.Post(url, "application/x-www-form-urlencoded", requestbody)

	if err != nil {
		log.Error(logContext, err.Error())
		return "", errors.New(netWorkErrorMessage)
	}

	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Error(logContext, err.Error())
		return "", errors.New(netWorkErrorMessage)
	}

	log.Info(logContext, "responseData : ", string(body))
	return string(body), nil
}

func convertMapToString(paraMap map[string]string, logContext string) string {
	var convertedString string = ""
	var index int = 0
	for key, value := range paraMap {
		if index == 0 {
			convertedString = convertedString + key + "=" + value
		} else {
			convertedString = convertedString + "&" + key + "=" + value
		}
		index++
	}
	log.Info(logContext, "requestData : ", convertedString)
	return convertedString
}
