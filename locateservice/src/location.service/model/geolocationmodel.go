package models

const (
	LOG_TAG contextKeyType = iota
)

type contextKeyType int

type GeoLocationReq struct {
	Latitude  string `schema:"latitude"`
	Longitude string `schema:"longitude"`
	Source    string `schema:"source"`
}


func (req *GeoLocationReq) String() string {
	return "Latitude:" + req.Latitude + ", Longitude:" + req.Longitude + ", Source:" + req.Source
}



type GeoLocationResp struct {
	CityId   string `json:"cityId"`
	CityName string `json:"cityName"`
	Mid      string `json:"mid"`
}

func (resp *GeoLocationResp) String() string {
	return "CityId:" + resp.CityId + ", CityName:" + resp.CityName + ", Mid:" + resp.Mid
}

type GeoLoacationInfo struct {
	City     string
	Province string
	District string
}

type SiteAddressInfo struct {
	ProvinceId   string
	ProvinceName string
	CityId       string
	CityName     string
	Mid          string
}

type SiteAddressArray struct {
	SiteArray []SiteAddressInfo
}



