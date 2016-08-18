package main

import (
	//Golang 标准库
	"fmt"
	"net/http"

	//第三访库
	log "github.com/cihub/seelog"
	"github.com/gorilla/mux"

	//项目相关
	"location.service/config"
	"location.service/controller"
)

func main() {
	// 路由
	router := mux.NewRouter()

	// 日志初始化
	defer log.Flush()
	initLogComponent()

	// URL映射配置
	subRouter := router.PathPrefix("/geo").Subrouter()
	subRouter.HandleFunc("/getLocation", controller.GetLocationHandler)
	subRouter.HandleFunc("/getGeolocation", controller.GetGeolocationHandler)
	log.Critical(http.ListenAndServe(":8000", subRouter))
}

func initLogComponent() {

	logConfigFile := config.ConfRoot + "/log.xml"
	logger, err := log.LoggerFromConfigAsFile(logConfigFile)

	if err != nil {
		fmt.Println(err)
	}

	loggerErr := log.ReplaceLogger(logger)

	if loggerErr != nil {
		fmt.Println(loggerErr)
	}
}
