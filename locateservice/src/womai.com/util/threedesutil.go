package util

import (
	//Golang 标准库
	"crypto/des"
	"encoding/base64"

	//第三访库
	log "github.com/cihub/seelog"
)

const encodeStd = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

var coder = base64.NewEncoding(encodeStd)

func TripleDESDecode(key, src string) string {
	if len(key) != 16 && len(key) != 24 {
		log.Error("key length error, must be 16 or 24")
		return ""
	}

	data, err := coder.DecodeString(src)
	if err != nil {
		log.Error(err)
		return ""
	}

	tripleDESKey := make([]byte, 0, 24)
	if len(key) == 16 {
		tripleDESKey = append(tripleDESKey, key[:16]...)
		tripleDESKey = append(tripleDESKey, key[:8]...)
	} else {
		tripleDESKey = append(tripleDESKey, key[:]...)
	}

	td, err := des.NewTripleDESCipher(tripleDESKey)
	if err != nil {
		log.Error(err)
		return ""
	}

	n := len(data) / td.BlockSize()
	var rb []byte
	for i := 0; i < n; i++ {
		dst := make([]byte, td.BlockSize())
		td.Decrypt(dst, data[i*8:(i+1)*8])
		rb = append(rb, dst[:]...)
	}

	lastValue := int(rb[len(rb)-1])
	return string(rb[0 : len(rb)-lastValue])
}

func TripleDESEncode(key, src string) string {
	var result string

	if len(key) != 16 && len(key) != 24 {
		log.Error("key length error, must be 16 or 24")
		return ""
	}

	tripleDESKey := make([]byte, 0, 24)
	if len(key) == 16 {
		tripleDESKey = append(tripleDESKey, key[:16]...)
		tripleDESKey = append(tripleDESKey, key[:8]...)
	} else {
		tripleDESKey = append(tripleDESKey, key[:]...)
	}

	td, err := des.NewTripleDESCipher(tripleDESKey)
	if err != nil {
		log.Error(err)
		return ""
	}

	mod := len(src) % td.BlockSize()
	v := td.BlockSize() - mod

	data := []byte(src)
	for i := 0; i < v; i++ {
		data = append(data, byte(v))
	}

	n := len(data) / td.BlockSize()
	var rb []byte
	for i := 0; i < n; i++ {
		dst := make([]byte, td.BlockSize())
		td.Encrypt(dst, data[i*8:(i+1)*8])
		rb = append(rb, dst[:]...)
	}

	result = coder.EncodeToString(rb)
	return result
}
