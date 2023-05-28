package component.schedule

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.*

external interface AddScheduleProps : Props {
    var lessons: List<Lesson>
    var teachers: List<Teacher>
    var audiences: List<Audience>
    var groups: List<Group>
    var addSchedule: (Schedule) -> Unit
}

val CScheduleAdd = FC<AddScheduleProps>("ScheduleAdd") { props ->
    var name by useState("")
    var typeoflesson by useState("")
    var time by useState("")
    var dayofweek by useState("")
    var typeofweek by useState("")
    var group by useState("")
    var teacher by useState("")
    var audience by useState("")

    span {
        p {
            select{
                option{
                    value = ""
                    +"Выберете название предмета"
                }
                props.lessons.forEach { type ->
                    option{
                        value = type.name
                        +type.name
                    }
                }
                onChange = { name = it.target.value }
            }
        }
        p {
            select{
                option{
                    value = ""
                    +"Выберете тип занятия"
                }
                LessonType.list.forEach { type ->
                    option{
                        value = type.mark
                        +type.mark
                    }
                }
                onChange = { typeoflesson = it.target.value }
            }
        }
        p {
            select{
                option{
                    value = ""
                    +"Выберете время"
                }
                Number.list.forEach { type ->
                    option{
                        value = type.mark
                        +type.mark
                    }
                }
                onChange = { time = it.target.value }
            }
        }
        p {
            select{
                option{
                    value = ""
                    +"Выберете день недели"
                }
                DayOfWeek.list.forEach { type ->
                    option{
                        value = type.mark
                        +type.mark
                    }
                }
                onChange = { dayofweek = it.target.value }
            }
        }
        p {
            select{
                option{
                    value = ""
                    +"Выберете тип недели"
                }
                TypeOfWeek.list.forEach { type ->
                    option{
                        value = type.mark
                        +type.mark
                    }
                }
                onChange = { typeofweek = it.target.value }
            }
        }
        p {
            select{
                option{
                    value = ""
                    +"Выберете группу"
                }
                props.groups.forEach { type ->
                    option{
                        value = type.name
                        +type.name
                    }
                }
                onChange = { group = it.target.value }
            }
        }
        p {
            select{
                option{
                    value = ""
                    +"Выберете преподавателя"
                }
                props.teachers.forEach { type ->
                    option{
                        value = type.fullname()
                        +type.fullname()
                    }
                }
                onChange = { teacher = it.target.value }
            }
        }
        p {
            select{
                option{
                    value = ""
                    +"Выберете аудиторию"
                }
                props.audiences.forEach { type ->
                    option{
                        value = type.classroom
                        +type.classroom.toString()
                    }
                }
                onChange = { audience = it.target.value }
            }
        }
    }
    button {
        +"Добавить занятие"
        if(name!= "" && typeoflesson!= "" && time!= "" && dayofweek!= "" && typeofweek!= "" && group!= "" && teacher!= "" && audience!= "")
            onClick = {
                props.addSchedule(Schedule(name, typeoflesson, time, dayofweek, typeofweek, group, teacher, audience.toInt(), ""))
            }
    }
}
