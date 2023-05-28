package component.teacher

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Teacher
import web.html.InputType

external interface EditTeacherProps : Props {
    var teacher: Teacher
    var saveTeacher: (Teacher) -> Unit
}

val CTeacherEdit = FC<EditTeacherProps>("TeacherEdit") { props ->
    var firstname by useState(props.teacher.firstname)
    var middleName by useState(props.teacher.middleName)
    var lastname by useState(props.teacher.lastname)
    span {
        input {
            type = InputType.text
            value = firstname
            onChange = { firstname = it.target.value }
        }
        input {
            type = InputType.text
            value = middleName
            onChange = { middleName = it.target.value }
        }
        input {
            type = InputType.text
            value = lastname
            onChange = { lastname = it.target.value }
        }
    }
    button {
        +"✓"
        onClick = {
            props.saveTeacher(Teacher(firstname, middleName, lastname, props.teacher._id))
        }
    }
}

