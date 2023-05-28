package component.audience

import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Audience
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

val containerAudienceList = FC("QueryAudienceList") { _: Props ->
    val queryClient = useQueryClient()
    val audienceListQueryKey = arrayOf("AudienceList").unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = audienceListQueryKey,
        queryFn = {
            fetchText(
                Config.audiencesPath
            )
        }
    )

    val addAudienceMutation = useMutation<HTTPResult, Any, Audience, Any>(
        mutationFn = { audience: Audience ->
            fetch(
                Config.audiencesPath,
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(
                        audience
                    )
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(audienceListQueryKey)
            }
        }
    )

    val deleteAudienceMutation = useMutation<HTTPResult, Any, String, Any>(
        { id: String ->
            fetch(
                "${Config.audiencesPath}/$id",
                jso {
                    method = "DELETE"
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(audienceListQueryKey)
            }
        }
    )

    val updateAudienceMutation = useMutation<HTTPResult, Any, Audience, Any>(
        mutationFn = { audience: Audience ->
            fetch(
                "${Config.audiencesPath}/${audience._id}",
                jso {
                    method = "PUT"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(audience)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(audienceListQueryKey)
            }
        }
    )

    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items =
            Json.decodeFromString<List<Audience>>(query.data ?: "")
        CAudienceAdd {
            addAudience = {
                addAudienceMutation.mutateAsync(it, null)
            }
        }
        CAudienceList {
            audiences = items
            deleteAudience = {
                deleteAudienceMutation.mutateAsync(it, null)
            }
            updateAudience = {
                updateAudienceMutation.mutateAsync(it, null)
            }
        }
    }
}