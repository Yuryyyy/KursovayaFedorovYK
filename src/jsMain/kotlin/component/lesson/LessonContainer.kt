package component.lesson

import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

val containerLessonList = FC("QueryLessonList") { _: Props ->
    val queryClient = useQueryClient()
    val lessonListQueryKey = arrayOf("LessonList").unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = lessonListQueryKey,
        queryFn = {
            fetchText(
                Config.lessonsPath
            )
        }
    )

    val addLessonMutation = useMutation<HTTPResult, Any, Lesson, Any>(
        mutationFn = { lesson: Lesson ->
            fetch(
                Config.lessonsPath,
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(
                        lesson
                    )
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(lessonListQueryKey)
            }
        }
    )

    val deleteLessonMutation = useMutation<HTTPResult, Any, String, Any>(
        { id: String ->
            fetch(
                "${Config.lessonsPath}/$id",
                jso {
                    method = "DELETE"
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(lessonListQueryKey)
            }
        }
    )

    val updateLessonMutation = useMutation<HTTPResult, Any, Lesson, Any>(
        mutationFn = { lesson: Lesson ->
            fetch(
                "${Config.lessonsPath}/${lesson._id}",
                jso {
                    method = "PUT"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(lesson)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(lessonListQueryKey)
            }
        }
    )

    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items =
            Json.decodeFromString<List<Lesson>>(query.data ?: "")
        CLessonAdd {
            addLesson = {
                addLessonMutation.mutateAsync(it, null)
            }
        }
        CLessonList {
            lessons = items
            deleteLesson = {
                deleteLessonMutation.mutateAsync(it, null)
            }
            updateLesson = {
                updateLessonMutation.mutateAsync(it, null)
            }
        }
    }
}