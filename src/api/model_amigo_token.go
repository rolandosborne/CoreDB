/*
 * CoreDB API
 *
 * An interface to distributed nodes for personal storage.
 *
 * API version: 1.0.4
 * Contact: info@coredb.org
 * Generated by: Swagger Codegen (https://github.com/swagger-api/swagger-codegen.git)
 */
package coredb

type AmigoToken struct {

	AmigoId string `json:"amigoId"`

	Amigo *AmigoMessage `json:"amigo"`

	Signature string `json:"signature"`

	Token string `json:"token"`
}