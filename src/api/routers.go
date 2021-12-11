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

import (
	"fmt"
	"net/http"
	"strings"

	"github.com/gorilla/mux"
)

type Route struct {
	Name        string
	Method      string
	Pattern     string
	HandlerFunc http.HandlerFunc
}

type Routes []Route

func NewRouter() *mux.Router {
	router := mux.NewRouter().StrictSlash(true)
	for _, route := range routes {
		var handler http.Handler
		handler = route.HandlerFunc
		handler = Logger(handler, route.Name)

		router.
			Methods(route.Method).
			Path(route.Pattern).
			Name(route.Name).
			Handler(handler)
	}

	return router
}

func Index(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hello World!")
}

var routes = Routes{
	Route{
		"Index",
		"GET",
		"/",
		Index,
	},

	Route{
		"AssignAccount",
		strings.ToUpper("Post"),
		"/access/services/tokens",
		AssignAccount,
	},

	Route{
		"AttachAccount",
		strings.ToUpper("Post"),
		"/access/accounts/attached",
		AttachAccount,
	},

	Route{
		"AuthorizeAttach",
		strings.ToUpper("Post"),
		"/access/services/attached",
		AuthorizeAttach,
	},

	Route{
		"AuthorizeCreate",
		strings.ToUpper("Post"),
		"/access/services/created",
		AuthorizeCreate,
	},

	Route{
		"CreateAccount",
		strings.ToUpper("Post"),
		"/access/accounts/created",
		CreateAccount,
	},

	Route{
		"CreateAmigo",
		strings.ToUpper("Post"),
		"/access/amigos",
		CreateAmigo,
	},

	Route{
		"GeneratePass",
		strings.ToUpper("Post"),
		"/access/accounts/tokens",
		GeneratePass,
	},

	Route{
		"GetAlerts",
		strings.ToUpper("Get"),
		"/account/alerts",
		GetAlerts,
	},

	Route{
		"GetConfig",
		strings.ToUpper("Get"),
		"/account/configs/{configId}",
		GetConfig,
	},

	Route{
		"GetConfigs",
		strings.ToUpper("Get"),
		"/account/configs",
		GetConfigs,
	},

	Route{
		"RemoveAlert",
		strings.ToUpper("Delete"),
		"/account/alert/{alertId}",
		RemoveAlert,
	},

	Route{
		"RemoveConfig",
		strings.ToUpper("Delete"),
		"/account/configs/{configId}",
		RemoveConfig,
	},

	Route{
		"SetConfig",
		strings.ToUpper("Put"),
		"/account/configs/{configId}",
		SetConfig,
	},

	Route{
		"AddRegistryService",
		strings.ToUpper("Post"),
		"/admin/registry/services",
		AddRegistryService,
	},

	Route{
		"AddServerAccount",
		strings.ToUpper("Post"),
		"/admin/accounts",
		AddServerAccount,
	},

	Route{
		"AddServerAlert",
		strings.ToUpper("Post"),
		"/admin/accounts/{amigoId}/alerts",
		AddServerAlert,
	},

	Route{
		"AddServerStat",
		strings.ToUpper("Post"),
		"/admin/server/stats",
		AddServerStat,
	},

	Route{
		"AdminStatus",
		strings.ToUpper("Get"),
		"/admin/accounts/status",
		AdminStatus,
	},

	Route{
		"DeleteAccount",
		strings.ToUpper("Delete"),
		"/admin/accounts/{amigoId}",
		DeleteAccount,
	},

	Route{
		"GetAccountMessages",
		strings.ToUpper("Get"),
		"/admin/accounts/messages",
		GetAccountMessages,
	},

	Route{
		"GetAdminConfig",
		strings.ToUpper("Get"),
		"/admin/server/configs/{configId}",
		GetAdminConfig,
	},

	Route{
		"GetAdminConfigs",
		strings.ToUpper("Get"),
		"/admin/server/configs",
		GetAdminConfigs,
	},

	Route{
		"GetRegistryService",
		strings.ToUpper("Get"),
		"/admin/registry/services/{amigoId}",
		GetRegistryService,
	},

	Route{
		"GetRegistryServices",
		strings.ToUpper("Get"),
		"/admin/registry/services",
		GetRegistryServices,
	},

	Route{
		"GetServerAccount",
		strings.ToUpper("Get"),
		"/admin/accounts/{amigoId}",
		GetServerAccount,
	},

	Route{
		"GetServerAccounts",
		strings.ToUpper("Get"),
		"/admin/accounts",
		GetServerAccounts,
	},

	Route{
		"GetServerAlerts",
		strings.ToUpper("Get"),
		"/admin/accounts/{amigoId}/alerts",
		GetServerAlerts,
	},

	Route{
		"GetServerConfig",
		strings.ToUpper("Get"),
		"/admin/accounts/{amigoId}/config/{configId}",
		GetServerConfig,
	},

	Route{
		"GetServerConfigs",
		strings.ToUpper("Get"),
		"/admin/accounts/{amigoId}/configs",
		GetServerConfigs,
	},

	Route{
		"GetServerStats",
		strings.ToUpper("Get"),
		"/admin/server/stats",
		GetServerStats,
	},

	Route{
		"RemoveAdminConfig",
		strings.ToUpper("Delete"),
		"/admin/server/configs/{configId}",
		RemoveAdminConfig,
	},

	Route{
		"RemoveRegistryService",
		strings.ToUpper("Delete"),
		"/admin/registry/services/{amigoId}",
		RemoveRegistryService,
	},

	Route{
		"RemoveServerAlert",
		strings.ToUpper("Delete"),
		"/admin/accounts/{amigoId}/alerts/{alertId}",
		RemoveServerAlert,
	},

	Route{
		"RemoveServerConfig",
		strings.ToUpper("Delete"),
		"/admin/accounts/{amigoId}/config/{configId}",
		RemoveServerConfig,
	},

	Route{
		"ServerInfo",
		strings.ToUpper("Get"),
		"/admin/info",
		ServerInfo,
	},

	Route{
		"SetAccountService",
		strings.ToUpper("Put"),
		"/admin/accounts/{amigoId}/service",
		SetAccountService,
	},

	Route{
		"UpdateAdminConfig",
		strings.ToUpper("Put"),
		"/admin/server/configs/{configId}",
		UpdateAdminConfig,
	},

	Route{
		"UpdateRegistryService",
		strings.ToUpper("Put"),
		"/admin/registry/services/{amigoId}",
		UpdateRegistryService,
	},

	Route{
		"UpdateServerAccountEnabled",
		strings.ToUpper("Put"),
		"/admin/accounts/{amigoId}/enabled",
		UpdateServerAccountEnabled,
	},

	Route{
		"UpdateServerAccountRegistry",
		strings.ToUpper("Put"),
		"/admin/accounts/{amigoId}/registry",
		UpdateServerAccountRegistry,
	},

	Route{
		"UpdateServerAccountRevision",
		strings.ToUpper("Put"),
		"/admin/accounts/{amigoId}/revision",
		UpdateServerAccountRevision,
	},

	Route{
		"UpdateServerConfig",
		strings.ToUpper("Put"),
		"/admin/accounts/{amigoId}/config/{configId}",
		UpdateServerConfig,
	},

	Route{
		"GetAgentMessage",
		strings.ToUpper("Put"),
		"/agent/service",
		GetAgentMessage,
	},

	Route{
		"AddCommentaryBlurb",
		strings.ToUpper("Post"),
		"/commentary/dialogue/{dialogueId}/blurb",
		AddCommentaryBlurb,
	},

	Route{
		"AddCommentaryInsight",
		strings.ToUpper("Post"),
		"/commentary/insight",
		AddCommentaryInsight,
	},

	Route{
		"GetCommentaryDialogue",
		strings.ToUpper("Get"),
		"/commentary/dialogue/{dialogueId}",
		GetCommentaryDialogue,
	},

	Route{
		"GetCommentaryTopic",
		strings.ToUpper("Get"),
		"/commentary/dialogue/{dialogueId}/topic/{topicId}",
		GetCommentaryTopic,
	},

	Route{
		"GetCommentaryTopicViews",
		strings.ToUpper("Get"),
		"/commentary/dialogue/{dialogueId}/topic/view",
		GetCommentaryTopicViews,
	},

	Route{
		"RemoveCommentaryBlurb",
		strings.ToUpper("Delete"),
		"/commentary/dialogue/{dialogueId}/blurb/{blurbId}",
		RemoveCommentaryBlurb,
	},

	Route{
		"RemoveCommentaryInsight",
		strings.ToUpper("Delete"),
		"/commentary/insight/{dialogueId}",
		RemoveCommentaryInsight,
	},

	Route{
		"SetCommentaryBlurb",
		strings.ToUpper("Put"),
		"/commentary/dialogue/{dialogueId}/blurb/{blurbId}",
		SetCommentaryBlurb,
	},

	Route{
		"UpdateCommentaryDialogue",
		strings.ToUpper("Put"),
		"/commentary/dialogue/{dialogueId}",
		UpdateCommentaryDialogue,
	},

	Route{
		"UpdateCommentaryInsight",
		strings.ToUpper("Put"),
		"/commentary/insight/{dialogueId}",
		UpdateCommentaryInsight,
	},

	Route{
		"AddContactAgent",
		strings.ToUpper("Post"),
		"/contact/agents",
		AddContactAgent,
	},

	Route{
		"FilterContactAttributes",
		strings.ToUpper("Post"),
		"/contact/attributes/filter",
		FilterContactAttributes,
	},

	Route{
		"FilterContactViews",
		strings.ToUpper("Post"),
		"/contact/attributes/view",
		FilterContactViews,
	},

	Route{
		"GetContactAttribute",
		strings.ToUpper("Get"),
		"/contact/attributes/{attributeId}",
		GetContactAttribute,
	},

	Route{
		"GetContactRevision",
		strings.ToUpper("Get"),
		"/contact/revision",
		GetContactRevision,
	},

	Route{
		"AddConversationBlurb",
		strings.ToUpper("Post"),
		"/conversation/dialogue/{dialogueId}/blurb",
		AddConversationBlurb,
	},

	Route{
		"AddConversationDialogue",
		strings.ToUpper("Post"),
		"/conversation/dialogue",
		AddConversationDialogue,
	},

	Route{
		"GetConversationDialogue",
		strings.ToUpper("Get"),
		"/conversation/dialogue/{dialogueId}",
		GetConversationDialogue,
	},

	Route{
		"GetConversationDialogueViews",
		strings.ToUpper("Get"),
		"/conversation/dialogue/view",
		GetConversationDialogueViews,
	},

	Route{
		"GetConversationInsight",
		strings.ToUpper("Get"),
		"/conversation/insight/{dialogueId}",
		GetConversationInsight,
	},

	Route{
		"GetConversationInsightViews",
		strings.ToUpper("Get"),
		"/conversation/insight/view",
		GetConversationInsightViews,
	},

	Route{
		"GetConversationTopic",
		strings.ToUpper("Get"),
		"/conversation/dialogue/{dialogueId}/topic/{topicId}",
		GetConversationTopic,
	},

	Route{
		"GetConversationTopicViews",
		strings.ToUpper("Get"),
		"/conversation/dialogue/{dialogueId}/topic/view",
		GetConversationTopicViews,
	},

	Route{
		"RemoveConversationBlurb",
		strings.ToUpper("Delete"),
		"/conversation/dialogue/{dialogueId}/blurb/{blurbId}",
		RemoveConversationBlurb,
	},

	Route{
		"RemoveConversationDialogue",
		strings.ToUpper("Delete"),
		"/conversation/dialogue/{dialogueId}",
		RemoveConversationDialogue,
	},

	Route{
		"RemoveConversationInsight",
		strings.ToUpper("Delete"),
		"/conversation/insight/{dialogueId}",
		RemoveConversationInsight,
	},

	Route{
		"SetConversationBlurb",
		strings.ToUpper("Put"),
		"/conversation/dialogue/{dialogueId}/blurb/{blurbId}",
		SetConversationBlurb,
	},

	Route{
		"SetConversationDialogue",
		strings.ToUpper("Put"),
		"/conversation/dialogue/{dialogueId}",
		SetConversationDialogue,
	},

	Route{
		"UpdateConversationInsight",
		strings.ToUpper("Put"),
		"/conversation/insight/{dialogueId}",
		UpdateConversationInsight,
	},

	Route{
		"AddLabel",
		strings.ToUpper("Post"),
		"/group/labels",
		AddLabel,
	},

	Route{
		"GetGroupRevision",
		strings.ToUpper("Get"),
		"/group/revision",
		GetGroupRevision,
	},

	Route{
		"GetLabel",
		strings.ToUpper("Get"),
		"/group/labels/{labelId}",
		GetLabel,
	},

	Route{
		"GetLabels",
		strings.ToUpper("Get"),
		"/group/labels",
		GetLabels,
	},

	Route{
		"GetLabelsView",
		strings.ToUpper("Get"),
		"/group/labels/view",
		GetLabelsView,
	},

	Route{
		"RemoveLabel",
		strings.ToUpper("Delete"),
		"/group/labels/{labelId}",
		RemoveLabel,
	},

	Route{
		"UpdateLabelName",
		strings.ToUpper("Put"),
		"/group/labels/{labelId}/name",
		UpdateLabelName,
	},

	Route{
		"GetDirtyFlag",
		strings.ToUpper("Get"),
		"/identity/dirty",
		GetDirtyFlag,
	},

	Route{
		"GetIdentity",
		strings.ToUpper("Get"),
		"/identity",
		GetIdentity,
	},

	Route{
		"GetIdentityImage",
		strings.ToUpper("Get"),
		"/identity/image",
		GetIdentityImage,
	},

	Route{
		"GetIdentityMessage",
		strings.ToUpper("Get"),
		"/identity/message",
		GetIdentityMessage,
	},

	Route{
		"GetIdentityRevision",
		strings.ToUpper("Get"),
		"/identity/revision",
		GetIdentityRevision,
	},

	Route{
		"SetDirtyFlag",
		strings.ToUpper("Put"),
		"/identity/dirty",
		SetDirtyFlag,
	},

	Route{
		"UpdateDescription",
		strings.ToUpper("Put"),
		"/identity/description",
		UpdateDescription,
	},

	Route{
		"UpdateHandle",
		strings.ToUpper("Put"),
		"/identity/handle",
		UpdateHandle,
	},

	Route{
		"UpdateImage",
		strings.ToUpper("Put"),
		"/identity/image",
		UpdateImage,
	},

	Route{
		"UpdateLocation",
		strings.ToUpper("Put"),
		"/identity/location",
		UpdateLocation,
	},

	Route{
		"UpdateName",
		strings.ToUpper("Put"),
		"/identity/name",
		UpdateName,
	},

	Route{
		"UpdateRegistry",
		strings.ToUpper("Put"),
		"/identity/registry",
		UpdateRegistry,
	},

	Route{
		"AddAmigo",
		strings.ToUpper("Post"),
		"/index/amigos",
		AddAmigo,
	},

	Route{
		"AddAmigoLabel",
		strings.ToUpper("Post"),
		"/index/amigos/{amigoId}/labels/{labelId}",
		AddAmigoLabel,
	},

	Route{
		"AddAmigoReject",
		strings.ToUpper("Post"),
		"/index/rejects/{amigoId}",
		AddAmigoReject,
	},

	Route{
		"GetAmigo",
		strings.ToUpper("Get"),
		"/index/amigos/{amigoId}",
		GetAmigo,
	},

	Route{
		"GetAmigoIdentity",
		strings.ToUpper("Get"),
		"/index/amigos/{amigoId}/identity",
		GetAmigoIdentity,
	},

	Route{
		"GetAmigoLogo",
		strings.ToUpper("Get"),
		"/index/amigos/{amigoId}/logo",
		GetAmigoLogo,
	},

	Route{
		"GetAmigoRejects",
		strings.ToUpper("Get"),
		"/index/rejects",
		GetAmigoRejects,
	},

	Route{
		"GetAmigoRequests",
		strings.ToUpper("Get"),
		"/index/requests/view",
		GetAmigoRequests,
	},

	Route{
		"GetAmigoRevision",
		strings.ToUpper("Get"),
		"/index/amigos/{amigoId}/revision",
		GetAmigoRevision,
	},

	Route{
		"GetAmigos",
		strings.ToUpper("Get"),
		"/index/amigos",
		GetAmigos,
	},

	Route{
		"GetIndexLabels",
		strings.ToUpper("Get"),
		"/index/labels",
		GetIndexLabels,
	},

	Route{
		"GetIndexRevision",
		strings.ToUpper("Get"),
		"/index/revision",
		GetIndexRevision,
	},

	Route{
		"GetPendingRequest",
		strings.ToUpper("Get"),
		"/index/requests/{shareId}",
		GetPendingRequest,
	},

	Route{
		"RemoveAmigo",
		strings.ToUpper("Delete"),
		"/index/amigos/{amigoId}",
		RemoveAmigo,
	},

	Route{
		"RemoveAmigoLabel",
		strings.ToUpper("Delete"),
		"/index/amigos/{amigoId}/labels/{labelId}",
		RemoveAmigoLabel,
	},

	Route{
		"RemoveAmigoNotes",
		strings.ToUpper("Delete"),
		"/index/amigos/{amigoId}/notes",
		RemoveAmigoNotes,
	},

	Route{
		"RemoveAmigoReject",
		strings.ToUpper("Delete"),
		"/index/rejects/{amigoId}",
		RemoveAmigoReject,
	},

	Route{
		"RemoveAmigoRequest",
		strings.ToUpper("Delete"),
		"/index/requests/{shareId}",
		RemoveAmigoRequest,
	},

	Route{
		"SetAmigoLabels",
		strings.ToUpper("Put"),
		"/index/amigos/{amigoId}/labels",
		SetAmigoLabels,
	},

	Route{
		"UpdateAmigo",
		strings.ToUpper("Put"),
		"/index/amigos",
		UpdateAmigo,
	},

	Route{
		"UpdateAmigoNotes",
		strings.ToUpper("Put"),
		"/index/amigos/{amigoId}/notes",
		UpdateAmigoNotes,
	},

	Route{
		"ViewAmigos",
		strings.ToUpper("Get"),
		"/index/amigos/view",
		ViewAmigos,
	},

	Route{
		"GetListingIdentity",
		strings.ToUpper("Get"),
		"/listing/identity",
		GetListingIdentity,
	},

	Route{
		"AccountToken",
		strings.ToUpper("Post"),
		"/portal/account",
		AccountToken,
	},

	Route{
		"CheckAdmin",
		strings.ToUpper("Get"),
		"/portal/admin",
		CheckAdmin,
	},

	Route{
		"CheckLogin",
		strings.ToUpper("Get"),
		"/portal/username",
		CheckLogin,
	},

	Route{
		"CheckToken",
		strings.ToUpper("Get"),
		"/portal/account",
		CheckToken,
	},

	Route{
		"GetDeviceAmigoProfile",
		strings.ToUpper("Get"),
		"/portal/amigos/{amigoId}",
		GetDeviceAmigoProfile,
	},

	Route{
		"GetDeviceAmigos",
		strings.ToUpper("Get"),
		"/portal/amigos",
		GetDeviceAmigos,
	},

	Route{
		"GetProfile",
		strings.ToUpper("Get"),
		"/portal/profile",
		GetProfile,
	},

	Route{
		"RemoveDeviceAmigoProfile",
		strings.ToUpper("Delete"),
		"/portal/amigos/{amigoId}",
		RemoveDeviceAmigoProfile,
	},

	Route{
		"ResetLogin",
		strings.ToUpper("Put"),
		"/portal/profile/login",
		ResetLogin,
	},

	Route{
		"ResetPassword",
		strings.ToUpper("Put"),
		"/portal/profile/password",
		ResetPassword,
	},

	Route{
		"ResetToken",
		strings.ToUpper("Put"),
		"/portal/account",
		ResetToken,
	},

	Route{
		"SetAdmin",
		strings.ToUpper("Put"),
		"/portal/admin",
		SetAdmin,
	},

	Route{
		"SetPassCode",
		strings.ToUpper("Put"),
		"/portal/profile/passcode",
		SetPassCode,
	},

	Route{
		"SetProfile",
		strings.ToUpper("Post"),
		"/portal/profile",
		SetProfile,
	},

	Route{
		"AddAttribute",
		strings.ToUpper("Post"),
		"/profile/attributes",
		AddAttribute,
	},

	Route{
		"ClearAttributeLabel",
		strings.ToUpper("Delete"),
		"/profile/attributes/{attributeId}/labels/{labelId}",
		ClearAttributeLabel,
	},

	Route{
		"FilterAttributes",
		strings.ToUpper("Post"),
		"/profile/attributes/filter",
		FilterAttributes,
	},

	Route{
		"GetAttribute",
		strings.ToUpper("Get"),
		"/profile/attributes/{attributeId}",
		GetAttribute,
	},

	Route{
		"GetProfileLabels",
		strings.ToUpper("Get"),
		"/profile/labels",
		GetProfileLabels,
	},

	Route{
		"GetProfileRevision",
		strings.ToUpper("Get"),
		"/profile/revision",
		GetProfileRevision,
	},

	Route{
		"RemoveAttribute",
		strings.ToUpper("Delete"),
		"/profile/attributes/{attributeId}",
		RemoveAttribute,
	},

	Route{
		"SetAttribute",
		strings.ToUpper("Put"),
		"/profile/attributes/{attributeId}",
		SetAttribute,
	},

	Route{
		"SetAttributeLabel",
		strings.ToUpper("Post"),
		"/profile/attributes/{attributeId}/labels/{labelId}",
		SetAttributeLabel,
	},

	Route{
		"SetAttributeLabels",
		strings.ToUpper("Put"),
		"/profile/attributes/{attributeId}/labels",
		SetAttributeLabels,
	},

	Route{
		"ViewAttributes",
		strings.ToUpper("Post"),
		"/profile/attributes/view",
		ViewAttributes,
	},

	Route{
		"AddQuestion",
		strings.ToUpper("Post"),
		"/prompt/questions",
		AddQuestion,
	},

	Route{
		"AddQuestionAnswer",
		strings.ToUpper("Post"),
		"/prompt/questions/{promptId}/answers",
		AddQuestionAnswer,
	},

	Route{
		"DeleteQuestionAnswer",
		strings.ToUpper("Delete"),
		"/prompt/questions/{promptId}/answers/{answerId}",
		DeleteQuestionAnswer,
	},

	Route{
		"GetPromptRevision",
		strings.ToUpper("Get"),
		"/prompt/revision",
		GetPromptRevision,
	},

	Route{
		"GetQuestions",
		strings.ToUpper("Get"),
		"/prompt/questions",
		GetQuestions,
	},

	Route{
		"RemoveQuestion",
		strings.ToUpper("Delete"),
		"/prompt/questions/{promptId}",
		RemoveQuestion,
	},

	Route{
		"UpdateQuestion",
		strings.ToUpper("Put"),
		"/prompt/questions/{promptId}",
		UpdateQuestion,
	},

	Route{
		"GetId",
		strings.ToUpper("Get"),
		"/registry/amigo/id",
		GetId,
	},

	Route{
		"GetLogo",
		strings.ToUpper("Get"),
		"/registry/amigo/messages/logo",
		GetLogo,
	},

	Route{
		"GetMessage",
		strings.ToUpper("Get"),
		"/registry/amigo/messages",
		GetMessage,
	},

	Route{
		"GetName",
		strings.ToUpper("Get"),
		"/registry/amigo/messages/name",
		GetName,
	},

	Route{
		"GetRegistryRevision",
		strings.ToUpper("Get"),
		"/registry/amigo/messages/revision",
		GetRegistryRevision,
	},

	Route{
		"DeleteApp",
		strings.ToUpper("Delete"),
		"/service/amigos/{amigoId}",
		DeleteApp,
	},

	Route{
		"GetApps",
		strings.ToUpper("Get"),
		"/service/amigos",
		GetApps,
	},

	Route{
		"GetServiceEntry",
		strings.ToUpper("Get"),
		"/service/amigos/{amigoId}",
		GetServiceEntry,
	},

	Route{
		"GetServiceRevision",
		strings.ToUpper("Get"),
		"/service/revision",
		GetServiceRevision,
	},

	Route{
		"SetServicePermission",
		strings.ToUpper("Put"),
		"/service/amigos/{amigoId}",
		SetServicePermission,
	},

	Route{
		"AddConnection",
		strings.ToUpper("Post"),
		"/share/connections",
		AddConnection,
	},

	Route{
		"GetConnection",
		strings.ToUpper("Get"),
		"/share/connections/{shareId}",
		GetConnection,
	},

	Route{
		"GetConnections",
		strings.ToUpper("Get"),
		"/share/connections",
		GetConnections,
	},

	Route{
		"GetConnectionsView",
		strings.ToUpper("Get"),
		"/share/connections/view",
		GetConnectionsView,
	},

	Route{
		"GetRevision",
		strings.ToUpper("Get"),
		"/share/revision",
		GetRevision,
	},

	Route{
		"GetShareAmigos",
		strings.ToUpper("Get"),
		"/share/amigos",
		GetShareAmigos,
	},

	Route{
		"GetShareMessage",
		strings.ToUpper("Get"),
		"/share/{shareId}/message",
		GetShareMessage,
	},

	Route{
		"RemoveConnection",
		strings.ToUpper("Delete"),
		"/share/connections/{shareId}",
		RemoveConnection,
	},

	Route{
		"SetAnswer",
		strings.ToUpper("Post"),
		"/share/answers",
		SetAnswer,
	},

	Route{
		"SetShareMessage",
		strings.ToUpper("Post"),
		"/share/messages",
		SetShareMessage,
	},

	Route{
		"UpdateConnection",
		strings.ToUpper("Put"),
		"/share/connections/{shareId}/status",
		UpdateConnection,
	},

	Route{
		"AddSubject",
		strings.ToUpper("Post"),
		"/show/subjects",
		AddSubject,
	},

	Route{
		"AddSubjectAsset",
		strings.ToUpper("Post"),
		"/show/subjects/{subjectId}/assets",
		AddSubjectAsset,
	},

	Route{
		"AddSubjectLabel",
		strings.ToUpper("Post"),
		"/show/subjects/{subjectId}/labels/{labelId}",
		AddSubjectLabel,
	},

	Route{
		"AddSubjectTags",
		strings.ToUpper("Post"),
		"/show/subjects/{subjectId}/tags",
		AddSubjectTags,
	},

	Route{
		"ClearSubjectLabel",
		strings.ToUpper("Delete"),
		"/show/subjects/{subjectId}/labels/{labelId}",
		ClearSubjectLabel,
	},

	Route{
		"FilterSubjects",
		strings.ToUpper("Post"),
		"/show/subjects/filter",
		FilterSubjects,
	},

	Route{
		"GetShowLabels",
		strings.ToUpper("Get"),
		"/show/labels",
		GetShowLabels,
	},

	Route{
		"GetShowRevision",
		strings.ToUpper("Get"),
		"/show/revision",
		GetShowRevision,
	},

	Route{
		"GetShowStatus",
		strings.ToUpper("Get"),
		"/show/status",
		GetShowStatus,
	},

	Route{
		"GetSubject",
		strings.ToUpper("Get"),
		"/show/subjects/{subjectId}",
		GetSubject,
	},

	Route{
		"GetSubjectAsset",
		strings.ToUpper("Get"),
		"/show/subjects/{subjectId}/assets/{assetId}",
		GetSubjectAsset,
	},

	Route{
		"GetSubjectTags",
		strings.ToUpper("Get"),
		"/show/subjects/{subjectId}/tags",
		GetSubjectTags,
	},

	Route{
		"RemoveSubject",
		strings.ToUpper("Delete"),
		"/show/subjects/{subjectId}",
		RemoveSubject,
	},

	Route{
		"RemoveSubjectAsset",
		strings.ToUpper("Delete"),
		"/show/subjects/{subjectId}/assets/{assetId}",
		RemoveSubjectAsset,
	},

	Route{
		"RemoveSubjectTag",
		strings.ToUpper("Delete"),
		"/show/subjects/{subjectId}/tags/{tagId}",
		RemoveSubjectTag,
	},

	Route{
		"SetSubjectLabels",
		strings.ToUpper("Put"),
		"/show/subjects/{subjectId}/labels",
		SetSubjectLabels,
	},

	Route{
		"UpdateSubjectAccess",
		strings.ToUpper("Put"),
		"/show/subjects/{subjectId}/access",
		UpdateSubjectAccess,
	},

	Route{
		"UpdateSubjectData",
		strings.ToUpper("Put"),
		"/show/subjects/{subjectId}/data",
		UpdateSubjectData,
	},

	Route{
		"UpdateSubjectExpire",
		strings.ToUpper("Put"),
		"/show/subjects/{subjectId}/expire",
		UpdateSubjectExpire,
	},

	Route{
		"ViewSubjects",
		strings.ToUpper("Post"),
		"/show/subjects/view",
		ViewSubjects,
	},

	Route{
		"GetAccess",
		strings.ToUpper("Get"),
		"/token/access",
		GetAccess,
	},

	Route{
		"GetRevisions",
		strings.ToUpper("Get"),
		"/token/revisions",
		GetRevisions,
	},

	Route{
		"SetAgentMessage",
		strings.ToUpper("Post"),
		"/token/agent",
		SetAgentMessage,
	},

	Route{
		"SetRevisions",
		strings.ToUpper("Post"),
		"/token/revisions",
		SetRevisions,
	},

	Route{
		"GetUser",
		strings.ToUpper("Get"),
		"/user/amigos/{amigoId}",
		GetUser,
	},

	Route{
		"GetUserIdentity",
		strings.ToUpper("Get"),
		"/user/amigos/{amigoId}/identity",
		GetUserIdentity,
	},

	Route{
		"GetUserRevision",
		strings.ToUpper("Get"),
		"/user/revision",
		GetUserRevision,
	},

	Route{
		"GetUsers",
		strings.ToUpper("Get"),
		"/user/amigos",
		GetUsers,
	},

	Route{
		"RemoveUser",
		strings.ToUpper("Delete"),
		"/user/amigos/{amigoId}",
		RemoveUser,
	},

	Route{
		"AddViewAgent",
		strings.ToUpper("Post"),
		"/view/agents",
		AddViewAgent,
	},

	Route{
		"AddViewSubjectTags",
		strings.ToUpper("Post"),
		"/view/subjects/{subjectId}/tags",
		AddViewSubjectTags,
	},

	Route{
		"FilterViewRevisions",
		strings.ToUpper("Post"),
		"/view/subjects/view",
		FilterViewRevisions,
	},

	Route{
		"FilterViewSubjects",
		strings.ToUpper("Post"),
		"/view/subjects/filter",
		FilterViewSubjects,
	},

	Route{
		"GetViewRevision",
		strings.ToUpper("Get"),
		"/view/revision",
		GetViewRevision,
	},

	Route{
		"GetViewSubject",
		strings.ToUpper("Get"),
		"/view/subjects/{subjectId}",
		GetViewSubject,
	},

	Route{
		"GetViewSubjectAsset",
		strings.ToUpper("Get"),
		"/view/subjects/{subjectId}/assets/{assetId}",
		GetViewSubjectAsset,
	},

	Route{
		"GetViewSubjectTags",
		strings.ToUpper("Get"),
		"/view/subjects/{subjectId}/tags",
		GetViewSubjectTags,
	},

	Route{
		"RemoveViewSubjectTag",
		strings.ToUpper("Delete"),
		"/view/subjects/{subjectId}/tags/{tagId}",
		RemoveViewSubjectTag,
	},
}
