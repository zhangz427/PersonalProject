package model

import (
	"fmt"
)

type CommonResp struct {
	Code    string      `json:"code"`
	Message string      `json:"message"`
	Data    fmt.Stringer `json:"data"`
}

type ErrorResp struct {
	Code    string      `json:"code"`
	Message string      `json:"message"`
}

type EncryptedResp struct {
	Code    string      `json:"code"`
	Message string      `json:"message"`
	Data    string 	    `json:"data"`
}

func (resp CommonResp) String() string {
	if resp.Data != nil {
		return "Code:" + resp.Code + ", Message:" + resp.Message + ", Data:" + resp.Data.String()
	} else {
		return "Code:" + resp.Code + ", Message:" + resp.Message
	}
}