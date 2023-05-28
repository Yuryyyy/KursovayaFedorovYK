package component.schedule

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.h3
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.th
import react.dom.html.ReactHTML.tr
import react.useState
import ru.altmanea.webapp.data.*

external interface ScheduleListProps : Props {
    var schedules: List<Schedule>
    var lessons: List<Lesson>
    var teachers: List<Teacher>
    var audiences: List<Audience>
    var groups: List<Group>
    var deleteSchedule: (String) -> Unit
    var updateSchedule: (Schedule) -> Unit
}

val CScheduleList = FC<ScheduleListProps>("ScheduleList") { props ->
    var name by useState("")
    var group by useState("")
    var teacher by useState("")
    var audience by useState("")
    var editedId by useState("")
    h3 { +"Расписание уроков" }
    table {
        border = 1
        tr{
            th{+"Название предмета"}
            th{+"Тип занятия"}
            th{+"Время"}
            th{+"День недели"}
            th{+"Тип недели"}
            th{+"Группа"}
            th{+"Преподаватель"}
            th{+"Аудитория"}
        }
        tr{
            th {
                select {
                    option {
                        +name
                        disabled
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
            th {}
            th {}
            th {}
            th {}
            th {
                select {
                    option {
                        +group
                        disabled
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
                        disabled
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
                        +audience
                        disabled
                    }
                    props.audiences.forEach { type ->
                        option {
                            value = type.classroom
                            +type.classroom.toString()
                        }
                    }
                    onChange = { audience = it.target.value }
                }
            }
            th {
                button {
                    +" ✂ "
                    onClick = {
                        name = ""
                        group = ""
                        teacher = ""
                        audience = ""

                    }
                }
            }
        }
        if(name != "" || group != "" || teacher != "" || audience != "") {
            props.schedules.forEach { schedules ->
                if ((schedules.name == name || name == "") && (schedules.group == group || group == "") && (schedules.teacher == teacher || teacher == "") && (schedules.audience.toString() == audience || audience == "")) {
                    tr {
                        if (schedules._id == editedId)
                            CScheduleEdit {
                                schedule = schedules
                                lessons = lessons
                                teachers = teachers
                                audiences = audiences
                                groups = groups
                                saveSchedule = {
                                    props.updateSchedule(it)
                                    editedId = ""
                                }
                            }
                        else {
                            CScheduleInList {
                                schedule = schedules
                            }
                        }
                        button {
                            +" ✂ "
                            onClick = {
                                props.deleteSchedule(schedules._id)
                            }
                        }
                        button {
                            +" ✎ "
                            onClick = {
                                editedId = schedules._id
                            }
                        }
                    }
                }
            }
        } else {
            props.schedules.forEach { schedules ->
                tr {
                    if (schedules._id == editedId)
                        CScheduleEdit {
                            schedule = schedules
                            lessons = props.lessons
                            teachers = props.teachers
                            audiences = props.audiences
                            groups = props.groups
                            saveSchedule = {
                                props.updateSchedule(it)
                                editedId = ""
                            }
                        }
                    else {
                        CScheduleInList {
                            schedule = schedules
                        }
                    }
                    button {
                        +" ✂ "
                        onClick = {
                            props.deleteSchedule(schedules._id)
                        }
                    }
                    button {
                        +" ✎ "
                        onClick = {
                            editedId = schedules._id
                        }
                    }
                }
            }
        }
    }
}
