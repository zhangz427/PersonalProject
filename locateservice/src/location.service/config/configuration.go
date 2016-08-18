package config

import (
	//Golang 标准库
	"io/ioutil"
	"encoding/json"
	"fmt"
	"os"
	"flag"

	//项目相关
	"location.service/model"
)

type Configuration struct {
	BaiDuGeoApiUrl string
	BaiDuGeoApiAk  string
	RedisMasterName  string
	RedisPassword  string
	RedisSentinelAddrs []string
	ThreeDESKey string
}

var Conf Configuration

var ConfRoot string

var SiteArrayData models.SiteAddressArray

func init() {
	goPath := os.Getenv("GOPATH")
	fmt.Println("GOPATH:" + goPath)

	var envVariable string
	flag.StringVar(&envVariable, "env", "undefined", "Environment variable")
	var confRoot string
	flag.StringVar(&confRoot, "confRoot", "undefined", "confRoot variable")
	flag.Parse()
	fmt.Println("EnvVariable:" + envVariable)

	if envVariable == "prod" || envVariable == "test" {
		if confRoot != "undefined" {
			ConfRoot = confRoot
		} else {
			ConfRoot = "/opt/conf/locateService/"
		}
	} else {
		ConfRoot = "src/location.service/config/dev"
	}

	fmt.Println("ConfRoot:" + ConfRoot)

	fileName := ConfRoot + "/configuration.json"

	data, err := ioutil.ReadFile(fileName)
	if err != nil {
		panic("Fail to init environment")
	}

	err = json.Unmarshal(data, &Conf)
	if err != nil {
		panic("Fail to init environment")
	}

	fmt.Println("BaiDuGeoApiUrl:" + Conf.BaiDuGeoApiUrl)
	fmt.Println("BaiDuGeoApiAk:" + Conf.BaiDuGeoApiAk)
	fmt.Println("RedisMasterName:", Conf.RedisMasterName)
	fmt.Println("RedisPassword:", Conf.RedisPassword)
	fmt.Println("RedisSentinelAddrs:", Conf.RedisSentinelAddrs)
	fmt.Println("ThreeDESKey:", Conf.ThreeDESKey)

	readSiteInfoFromLocalFile()
}

func readSiteInfoFromLocalFile() {

	fileName := ConfRoot + "/site.json"
	data, err := ioutil.ReadFile(fileName)
	if err != nil {
		panic(err.Error())
	}

	err = json.Unmarshal(data, &SiteArrayData)
	if err != nil {
		panic("no site data is load")
	}
}