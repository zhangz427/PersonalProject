package service

type BaseService struct {
	TrackableLogService
}

type TrackableLogService struct {
	LogContext string
}