package main

import (
	"sync"

	"database/sql"
)

type IServiceContainer interface {
}

type kernel struct{}

var (
	k             *kernel
	containerOnce sync.Once
)

func ServiceContainer() IServiceContainer {
	if k == nil {
		containerOnce.Do(func() {
			k = &kernel{}
		})
	}
	return k
}
