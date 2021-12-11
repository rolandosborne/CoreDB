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

type Insight struct {

	DialogueId string `json:"dialogueId"`

	Revision int32 `json:"revision"`

	AmigoId string `json:"amigoId"`

	AmigoRegistry string `json:"amigoRegistry,omitempty"`
}
