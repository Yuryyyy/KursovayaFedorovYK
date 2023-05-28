import component.audience.containerAudienceList
import component.group.containerGroupList
import component.lesson.containerLessonList
import component.schedule.containerScheduleList
import component.teacher.containerTeacherList
import react.FC
import react.Props
import react.create
import react.dom.client.createRoot
import react.dom.html.ReactHTML.div
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.router.dom.Link
import ru.altmanea.webapp.config.Config
import tanstack.query.core.QueryClient
import tanstack.react.query.QueryClientProvider
import tanstack.react.query.devtools.ReactQueryDevtools
import web.dom.document

fun main() {
    val container = document.getElementById("root")!!
    createRoot(container).render(app.create())
}

val app = FC<Props>("App") {
    HashRouter {
        QueryClientProvider {
            client = QueryClient()
            div {
                Link {
                    +"Teachers"
                    to = Config.teachersPath
                }
            }
            div {
                Link {
                    +"Groups"
                    to = Config.groupsPath
                }
            }
            div {
                Link {
                    +"Lessons"
                    to = Config.lessonsPath
                }
            }
            div {
                Link {
                    +"Audiences"
                    to = Config.audiencesPath
                }
            }
            div {
                Link {
                    +"Schedule"
                    to = Config.schedulePath
                }
            }
            Routes {
                Route {
                    path = Config.teachersPath
                    element = containerTeacherList.create()
                }
                Route {
                    path = Config.groupsPath
                    element = containerGroupList.create()
                }
                Route {
                    path = Config.lessonsPath
                    element = containerLessonList.create()
                }
                Route {
                    path = Config.audiencesPath
                    element = containerAudienceList.create()
                }
                Route {
                    path = Config.schedulePath
                    element = containerScheduleList.create()
                }
            }
            ReactQueryDevtools { }
        }
    }
}