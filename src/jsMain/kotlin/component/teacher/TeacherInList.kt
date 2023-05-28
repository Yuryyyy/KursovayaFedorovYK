package component.teacher

import react.FC
import react.Props
import react.dom.html.ReactHTML.span
import ru.altmanea.webapp.data.Teacher

external interface TeacherInListProps : Props {
    var teacher: Teacher
}

val CTeacherInList = FC<TeacherInListProps>("TeacherInList") { props ->
    span {
        +props.teacher.fullname()
    }
}