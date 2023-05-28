package component.teacher

import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Teacher
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

val containerTeacherList = FC("QueryTeacherList") { _: Props ->
    val queryClient = useQueryClient()
    val teacherListQueryKey = arrayOf("TeacherList").unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = teacherListQueryKey,
        queryFn = {
            fetchText(
                Config.teachersPath
            )
        }
    )

    val addTeacherMutation = useMutation<HTTPResult, Any, Teacher, Any>(
        mutationFn = { teacher: Teacher ->
            fetch(
                Config.teachersPath,
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(
                        teacher
                    )
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(teacherListQueryKey)
            }
        }
    )

    val deleteTeacherMutation = useMutation<HTTPResult, Any, String, Any>(
        { id: String ->
            fetch(
                "${Config.teachersPath}/$id",
                jso {
                    method = "DELETE"
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(teacherListQueryKey)
            }
        }
    )

    val updateTeacherMutation = useMutation<HTTPResult, Any, Teacher, Any>(
        mutationFn = { teacher: Teacher ->
            fetch(
                "${Config.teachersPath}/${teacher._id}",
                jso {
                    method = "PUT"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(teacher)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(teacherListQueryKey)
            }
        }
    )

    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items =
            Json.decodeFromString<List<Teacher>>(query.data ?: "")
        CTeacherAdd {
            addTeacher = {
                addTeacherMutation.mutateAsync(it, null)
            }
        }
        CTeacherList {
            teachers = items
            deleteTeacher = {
                deleteTeacherMutation.mutateAsync(it, null)
            }
            updateTeacher = {
                updateTeacherMutation.mutateAsync(it, null)
            }
        }
    }
}