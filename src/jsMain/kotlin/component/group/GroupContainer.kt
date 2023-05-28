package component.group

import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Group
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

val containerGroupList = FC("QueryGroupList") { _: Props ->
    val queryClient = useQueryClient()
    val groupListQueryKey = arrayOf("GroupList").unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = groupListQueryKey,
        queryFn = {
            fetchText(
                Config.groupsPath
            )
        }
    )

    val addGroupMutation = useMutation<HTTPResult, Any, Group, Any>(
        mutationFn = { group: Group ->
            fetch(
                Config.groupsPath,
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(
                        group
                    )
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(groupListQueryKey)
            }
        }
    )

    val deleteGroupMutation = useMutation<HTTPResult, Any, String, Any>(
        { id: String ->
            fetch(
                "${Config.groupsPath}/$id",
                jso {
                    method = "DELETE"
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(groupListQueryKey)
            }
        }
    )

    val updateGroupMutation = useMutation<HTTPResult, Any, Group, Any>(
        mutationFn = { group: Group ->
            fetch(
                "${Config.groupsPath}/${group._id}",
                jso {
                    method = "PUT"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(group)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(groupListQueryKey)
            }
        }
    )

    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items =
            Json.decodeFromString<List<Group>>(query.data ?: "")
        CGroupAdd {
            addGroup = {
                addGroupMutation.mutateAsync(it, null)
            }
        }
        CGroupList {
            groups = items
            deleteGroup = {
                deleteGroupMutation.mutateAsync(it, null)
            }
            updateGroup = {
                updateGroupMutation.mutateAsync(it, null)
            }
        }
    }
}