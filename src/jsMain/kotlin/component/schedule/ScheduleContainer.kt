package component.schedule

import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.*
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

val containerScheduleList = FC("QueryScheduleList") { _: Props ->
    val queryClient = useQueryClient()
    val scheduleListQueryKey = arrayOf("ScheduleList").unsafeCast<QueryKey>()
    val myQueryKeyLesson = arrayOf(Config.lessonsPath).unsafeCast<QueryKey>()
    val myQueryKeyTeacher = arrayOf(Config.teachersPath).unsafeCast<QueryKey>()
    val myQueryKeyAudience = arrayOf(Config.audiencesPath).unsafeCast<QueryKey>()
    val myQueryKeyGroup = arrayOf(Config.groupsPath).unsafeCast<QueryKey>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = scheduleListQueryKey,
        queryFn = {
            fetchText(Config.schedulePath)
        }
    )

    val queryLesson = useQuery<String, QueryError, String, QueryKey>(
        queryKey = myQueryKeyLesson,
        queryFn = {
            fetchText(Config.lessonsPath)
        }
    )

    val queryTeacher = useQuery<String, QueryError, String, QueryKey>(
        queryKey = myQueryKeyTeacher,
        queryFn = {
            fetchText(Config.teachersPath)
        }
    )

    val queryAudience = useQuery<String, QueryError, String, QueryKey>(
        queryKey = myQueryKeyAudience,
        queryFn = {
            fetchText(Config.audiencesPath)
        }
    )

    val queryGroup = useQuery<String, QueryError, String, QueryKey>(
        queryKey = myQueryKeyGroup,
        queryFn = {
            fetchText(Config.groupsPath)
        }
    )

    val addScheduleMutation = useMutation<HTTPResult, Any, Schedule, Any>(
        mutationFn = { schedule: Schedule ->
            fetch(
                Config.schedulePath,
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(
                        schedule
                    )
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(scheduleListQueryKey)
            }
        }
    )

    val deleteScheduleMutation = useMutation<HTTPResult, Any, String, Any>(
        { id: String ->
            fetch(
                "${Config.schedulePath}/$id",
                jso {
                    method = "DELETE"
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(scheduleListQueryKey)
            }
        }
    )

    val updateScheduleMutation = useMutation<HTTPResult, Any, Schedule, Any>(
        mutationFn = { schedule: Schedule ->
            fetch(
                "${Config.schedulePath}/${schedule._id}",
                jso {
                    method = "PUT"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(schedule)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(scheduleListQueryKey)
            }
        }
    )

    val lessonsList: List<Lesson> =
        try {
            Json.decodeFromString(queryLesson.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }

    val teachersList: List<Teacher> =
        try {
            Json.decodeFromString(queryTeacher.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }

    val audiencesList: List<Audience> =
        try {
            Json.decodeFromString(queryAudience.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }

    val groupsList: List<Group> =
        try {
            Json.decodeFromString(queryGroup.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }

    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items =
            Json.decodeFromString<List<Schedule>>(query.data ?: "")
        CScheduleAdd {
            lessons = lessonsList
            teachers = teachersList
            audiences = audiencesList
            groups = groupsList
            addSchedule = {
                addScheduleMutation.mutateAsync(it, null)
            }
        }
        CScheduleList {
            schedules = items
            lessons = lessonsList
            teachers = teachersList
            audiences = audiencesList
            groups = groupsList
            deleteSchedule = {
                deleteScheduleMutation.mutateAsync(it, null)
            }
            updateSchedule = {
                updateScheduleMutation.mutateAsync(it, null)
            }
        }
    }
}