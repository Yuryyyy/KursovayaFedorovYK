package component.schedule

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.th
import react.useState
import ru.altmanea.webapp.data.*

external interface EditScheduleProps : Props {
    var schedule: Schedule
    var lessons: List<Lesson>
    var teachers: List<Teacher>
    var audiences: List<Audience>
    var groups: List<Group>
    var saveSchedule: (Schedule) -> Unit
}

val CScheduleEdit = FC<EditScheduleProps>("ScheduleEdit") { props ->
    var name by useState(props.schedule.name)
    var typeoflesson by useState(props.schedule.typeoflesson)
    var time by useState(props.schedule.time)
    var dayofweek by useState(props.schedule.dayofweek)
    var typeofweek by useState(props.schedule.typeofweek)
    var group by useState(props.schedule.group)
    var teacher by useState(props.schedule.teacher)
    var audience by useState(props.schedule.audience)

    th {
        select {
            option {
                +name
            }
            props.lessons.forEach { type ->
                option {
                    value = type.name
                    +type.name
                }
            }
            onChange = { name = it.target.value }
        }
    }
    th {
        select {
            option {
                +typeoflesson
            }
            LessonType.list.forEach { type ->
                option {
                    value = type.mark
                    +type.mark
                }
            }
            onChange = { typeoflesson = it.target.value }
        }
    }
    th {
        select {
            option {
                +time
            }
            Number.list.forEach { type ->
                option {
                    value = type.mark
                    +type.mark
                }
            }
            onChange = { time = it.target.value }
        }
    }
    th {
        select {
            option {
                +dayofweek
            }
            DayOfWeek.list.forEach { type ->
                option {
                    value = type.mark
                    +type.mark
                }
            }
            onChange = { dayofweek = it.target.value }
        }
    }
    th {
        select {
            option {
                +typeofweek
            }
            TypeOfWeek.list.forEach { type ->
                option {
                    value = type.mark
                    +type.mark
                }
            }
            onChange = { typeofweek = it.target.value }
        }
    }
    th {
        select {
            option {
                +group
            }
            props.groups.forEach { type ->
                option {
                    value = type.name
                    +type.name
                }
            }
            onChange = { group = it.target.value }
        }
    }
    th {
        select {
            option {
                +teacher
            }
            props.teachers.forEach { type ->
                option {
                    value = type.fullname()
                    +type.fullname()
                }
            }
            onChange = { teacher = it.target.value }
        }
    }
    th {
        select {
            option {
                +audience.toString()
            }
            props.audiences.forEach { type ->
                option {
                    value = type.classroom
                    +type.classroom.toString()
                }
            }
            onChange = { audience = it.target.value.toInt() }
        }
    }
    button {
        +"✓"
        onClick = {
            props.saveSchedule(Schedule(name, typeoflesson, time, dayofweek, typeofweek, group, teacher, audience, props.schedule._id))
        }
    }
}
