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

type SubjectView struct {

	SubjectId string `json:"subjectId"`

	Revision int32 `json:"revision"`

	TagRevision int32 `json:"tagRevision"`
}
