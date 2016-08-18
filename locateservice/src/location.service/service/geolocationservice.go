package service

import (
	//Golang 标准库
	"errors"
	"strings"

	//第三访库
	"github.com/bitly/simplejson"
	log "github.com/cihub/seelog"

	//Womai工具库
	baseService "womai.com/service"
	baseUtil "womai.com/util"

	//项目相关
	"location.service/model"
	"location.service/config"
)

const (
	k_BAIDU_GEO_OUTPUT_TYPE_JSON   = "json"
	k_BAIDU_GEO_COORDT_TYPE_BD0911 = "bd09ll"
	k_GEO_LOCATION_ERROR_MESSAGE      = "定位失败"
	k_LOCATION_NOT_SUPPORT_MESSAGE = "当前位置未开通站点"
	k_LOCATION_NOT_SUPPORT_ERROR_CODE = "-92"
)

type IGeolocationService interface {
	GetGeoLocationInfo(latitude string, longitude string) (*models.GeoLocationResp, error, string)
}

type GeolocationService struct {
	baseService.BaseService
}

func GetGeolocationService(logContext string) *GeolocationService {
	geoLocationService := new(GeolocationService)
	geoLocationService.LogContext = logContext
	return geoLocationService
}

func (geoLocationService *GeolocationService) GetGeoLocationInfo(latitude string, longitude string) (*models.GeoLocationResp, error, string) {

	geoRequestMap := map[string]string{
		"ak":        config.Conf.BaiDuGeoApiAk,
		"output":    k_BAIDU_GEO_OUTPUT_TYPE_JSON,
		"coordtype": k_BAIDU_GEO_COORDT_TYPE_BD0911,
		"location":  latitude + "," + longitude,
		"pois":      "0",
	}

	// 调用百度API获取地址信息
	addressString, err := baseUtil.SendPostRequest(geoRequestMap, config.Conf.BaiDuGeoApiUrl, geoLocationService.LogContext)
	if err != nil {
		return nil, errors.New(k_GEO_LOCATION_ERROR_MESSAGE), baseUtil.SERVER_ERROR_CODE
	}

	// Json解析
	j, err := simplejson.NewJson([]byte(addressString))
	if err != nil {
		return nil, errors.New(k_GEO_LOCATION_ERROR_MESSAGE), baseUtil.SERVER_ERROR_CODE
	}

	// 判断百度返回数据 status 状态值
	if respIsSuccess(j) {

		// 获取百度返回的地址信息
		addressInfo := new(models.GeoLoacationInfo)
		addressInfo.City, _ = j.Get("result").Get("addressComponent").Get("city").String()
		addressInfo.Province, _ = j.Get("result").Get("addressComponent").Get("province").String()
		addressInfo.District, _ = j.Get("result").Get("addressComponent").Get("district").String()

		if len(addressInfo.City) == 0 && len(addressInfo.District) == 0 {
			log.Error(geoLocationService.LogContext, "Invalid coordinate,", latitude, " ", longitude)
			return nil, errors.New(k_GEO_LOCATION_ERROR_MESSAGE), k_LOCATION_NOT_SUPPORT_ERROR_CODE
		}

		// 匹配地址库
		siteInfo := matchSiteAddress(addressInfo)
		if strings.EqualFold(siteInfo.CityName, "") {
			// 未匹配的地址存入日志
			log.Error(geoLocationService.LogContext, "No matching address: ", *addressInfo)
			return nil, errors.New(k_LOCATION_NOT_SUPPORT_MESSAGE), k_LOCATION_NOT_SUPPORT_ERROR_CODE
		} else {
			return siteInfo, nil, ""
		}

	} else {
		return nil, errors.New(k_GEO_LOCATION_ERROR_MESSAGE), baseUtil.SERVER_ERROR_CODE
	}
}

func respIsSuccess(json *simplejson.Json) bool {
	if result, _ := json.Get("status").Int(); result == 0 {
		return true
	} else {
		return false
	}
}

func matchSiteAddress(address *models.GeoLoacationInfo) *models.GeoLocationResp {

	geoInfo := new(models.GeoLocationResp)
	for v := range config.SiteArrayData.SiteArray {
		site := config.SiteArrayData.SiteArray[v]

		if strings.Contains(address.Province, site.ProvinceName) &&
			strings.Contains(address.City, site.CityName) {

			geoInfo.CityId = site.CityId
			geoInfo.Mid = site.Mid
			geoInfo.CityName = site.CityName
			return geoInfo
		}
	}
	for v := range config.SiteArrayData.SiteArray {
		site := config.SiteArrayData.SiteArray[v]
		if strings.Contains(address.Province, site.ProvinceName) &&
		strings.Contains(address.District, site.CityName) {
			geoInfo.CityId = site.CityId
			geoInfo.Mid = site.Mid
			geoInfo.CityName = site.CityName
			return geoInfo
		}
	}
	return geoInfo
}
