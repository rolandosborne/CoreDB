package main

import (
  "fmt"
	"net/http"
)

func main() {
  fmt.Println("listening on port 8080");
	http.ListenAndServe(":8080", ChiRouter().InitRouter())
}
