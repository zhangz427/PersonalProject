package util

import (
	//第三访库
	redis "gopkg.in/redis.v4"

	//项目相关
	"location.service/config"
)

var RedisClient *redis.Client

func init() {
	RedisClient = redis.NewFailoverClient(&redis.FailoverOptions{
		MasterName:    config.Conf.RedisMasterName,
		SentinelAddrs: config.Conf.RedisSentinelAddrs,
		Password: config.Conf.RedisPassword,
	})

	_, err := RedisClient.Ping().Result()
	if err != nil {
		panic(err)
	}
}