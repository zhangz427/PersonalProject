package controller

import (
	//Golang 标准库
	"encoding/json"
	"net/http"
	log "github.com/cihub/seelog"
	"strings"
	"encoding/base64"
	"time"
	"strconv"

	//第三访库
	"github.com/gorilla/schema"
	"github.com/gorilla/context"

	//Womai工具库
	baseUtil "womai.com/util"
	baseModel "womai.com/model"

	//项目相关
	"location.service/model"
	"location.service/service"
	"location.service/util"
	"location.service/config"

)

var (
	requestDecoder = schema.NewDecoder()
)

const (
	k_GEO_LOCATION_ERROR_MESSAGE = "定位失败"
)

func GetLocationHandler(rw http.ResponseWriter, req *http.Request) {

	timestamp := strconv.FormatInt(time.Now().UnixNano(), 10)
	context.Set(req, models.LOG_TAG, timestamp)
	defer context.Clear(req)

	// 获取请求参数
	err := req.ParseForm()
	if err != nil {
		// Handle error
		comRes := requestParaErrorProcess()
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}

	// 映射请求参数
	geoLocation := new(models.GeoLocationReq)
	err = requestDecoder.Decode(geoLocation, req.Form)
	if err != nil {
		// Handle error
		comRes := requestParaErrorProcess()
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}

	if len(strings.TrimSpace(geoLocation.Latitude)) == 0 || len(strings.TrimSpace(geoLocation.Longitude)) == 0 {
		log.Error("Latitude Longitude could not be null")
		comRes := requestParaErrorProcess()
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}

	convertedSourceId := convertSourceId(geoLocation.Source)
	if len(strings.TrimSpace(convertedSourceId)) == 0 {
		log.Error("Invalid source")
		comRes := requestParaErrorProcess()
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}

	if !validateSourceId(convertedSourceId) {
		log.Error("Invalid source:", convertedSourceId)
		comRes := requestParaErrorProcess()
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}

	geoLocation.Source = convertedSourceId

	comRes, errRes := getLocationData(req, geoLocation)
	if errRes != nil {
		responseByte, _ := json.Marshal(errRes)
		rw.Write(responseByte)
	} else {
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
	}
}

func GetGeolocationHandler(rw http.ResponseWriter, req *http.Request) {
	timestamp := strconv.FormatInt(time.Now().UnixNano(), 10)
	context.Set(req, models.LOG_TAG, timestamp)
	defer context.Clear(req)

	// 获取请求参数
	err := req.ParseForm()
	if err != nil {
		// Handle error
		comRes := convertToEncryptedResponse(requestParaErrorProcess())
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}

	encryptedRequestData := req.Form.Get("data")
	decryptedRequestData, errDecode := base64.StdEncoding.DecodeString(encryptedRequestData)
	if errDecode != nil {
		// Handle error
		comRes := convertToEncryptedResponse(requestParaErrorProcess())
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}
	encryptedHeaderData := req.Header.Get("headerData")
	decryptedHeaderData := baseUtil.TripleDESDecode(config.Conf.ThreeDESKey, encryptedHeaderData)

	geoLocationReq := parseEncryptedRequest(string(decryptedRequestData), decryptedHeaderData)

	if geoLocationReq == nil {
		// Handle error
		comRes := convertToEncryptedResponse(requestParaErrorProcess())
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}

	if len(strings.TrimSpace(geoLocationReq.Latitude)) == 0 || len(strings.TrimSpace(geoLocationReq.Longitude)) == 0 {
		log.Error("Latitude Longitude could not be null")
		comRes := convertToEncryptedResponse(requestParaErrorProcess())
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}

	if !validateSourceId(geoLocationReq.Source) {
		log.Error("Invalid source:", geoLocationReq.Source)
		comRes := convertToEncryptedResponse(requestParaErrorProcess())
		responseByte, _ := json.Marshal(comRes)
		rw.Write(responseByte)
		return
	}

	comRes, errRes := getLocationData(req, geoLocationReq)

	if errRes != nil {
		encryptedResponse := convertToEncryptedResponse(errRes)
		responseByte, _ := json.Marshal(encryptedResponse)
		rw.Write(responseByte)
	} else {
		encryptedResponse := convertToEncryptedResponse(comRes)
		responseByte, _ := json.Marshal(encryptedResponse)
		rw.Write(responseByte)
	}
}

func convertToEncryptedResponse(comRes interface{}) *baseModel.EncryptedResp {
	responseByte, _ := json.Marshal(comRes)
	encryptedResponse := base64.StdEncoding.EncodeToString(responseByte)
	wrappedResult := baseModel.EncryptedResp{Code: baseUtil.SUCCESS_CODE, Message: baseUtil.SUCCESS_MESSAGE}
	wrappedResult.Data = encryptedResponse
	return &wrappedResult
}

func parseEncryptedRequest(decryptedRequestData string, decryptedHeaderData string) *models.GeoLocationReq {

	var dat map[string]interface{}
	if err := json.Unmarshal([]byte(decryptedRequestData), &dat); err != nil {
		log.Error(err)
		return nil
	}

	strs := dat["data"].(map[string]interface{})

	var longitude, latitude, mipSourceId string

	longitude, _ = strs["longitude"].(string)
	latitude, _ = strs["latitude"].(string)

	var headerDat map[string]interface{}
	if err := json.Unmarshal([]byte(decryptedHeaderData), &headerDat); err != nil {
		log.Error(err)
		return nil
	}

	mipSourceId, _ = headerDat["mipSourceId"].(string)
	geoLocation := new(models.GeoLocationReq)
	geoLocation.Latitude = string(latitude)
	geoLocation.Longitude = string(longitude)
	geoLocation.Source = mipSourceId

	return geoLocation
}

func getLocationData(httpReq *http.Request,geoLocation *models.GeoLocationReq) (*baseModel.CommonResp, *baseModel.ErrorResp)  {
	log.Info("Timestamp:", context.Get(httpReq, models.LOG_TAG),", RequestData: request:{", geoLocation, "}")

	var geoLocationService service.IGeolocationService = service.GetGeolocationService("Timestamp:" + context.Get(httpReq, models.LOG_TAG).(string) + ", ")

	siteinfo, err, code := geoLocationService.GetGeoLocationInfo(geoLocation.Latitude, geoLocation.Longitude)
	comRes := baseModel.CommonResp{Code: baseUtil.SUCCESS_CODE, Message: baseUtil.SUCCESS_MESSAGE}
	var errorRes baseModel.ErrorResp
	if err != nil {
		errorRes = baseModel.ErrorResp{Code: code, Message: err.Error()}
		return nil, &errorRes
	} else {
		comRes.Data = siteinfo
		log.Info("Timestamp:", context.Get(httpReq, models.LOG_TAG), ", ResultData: request:{", geoLocation, "} Response:{", comRes, "}")
		return &comRes, nil
	}

}

func convertSourceId(encryptedSource string) string {
	decryptString := baseUtil.TripleDESDecode(config.Conf.ThreeDESKey, encryptedSource)
	if len(decryptString) > 12 {
		return decryptString[12:len(decryptString)]
	} else {
		return ""
	}

}

func validateSourceId(sourceId string) bool {
	return util.RedisClient.SIsMember("MIP_SOURCE_SET", sourceId).Val()
}

func requestParaErrorProcess() baseModel.ErrorResp {
	errorRes := baseModel.ErrorResp{Code: baseUtil.SERVER_ERROR_CODE, Message: k_GEO_LOCATION_ERROR_MESSAGE}
	return errorRes
}
