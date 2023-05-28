package component.teacher

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.useState
import ru.altmanea.webapp.data.Teacher

external interface TeacherListProps : Props {
    var teachers: List<Teacher>
    var deleteTeacher: (String) -> Unit
    var updateTeacher: (Teacher) -> Unit
}

val CTeacherList = FC<TeacherListProps>("TeacherList") { props ->
    var editedId by useState("")
    ol {
        props.teachers.forEach { teachers ->
            li {
                if (teachers._id == editedId)
                    CTeacherEdit {
                        teacher = teachers
                        saveTeacher = {
                            props.updateTeacher(it)
                            editedId = ""
                        }
                    }
                else
                    CTeacherInList {
                        teacher = teachers
                    }
                button {
                    +" ✂ "
                    onClick = {
                        props.deleteTeacher(teachers._id)
                    }
                }
                button {
                    +" ✎ "
                    onClick = {
                        editedId = teachers._id
                    }
                }
            }
        }
    }
}
