package component.teacher

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Teacher
import web.html.InputType

external interface AddTeacherProps : Props {
    var addTeacher: (Teacher) -> Unit
}

val CTeacherAdd = FC<AddTeacherProps>("TeacherAdd") { props ->
    var firstname by useState("")
    var middleName by useState("")
    var lastname by useState("")
    span {
        p {
            +"Фамилия "
            input {
                type = InputType.text
                value = firstname
                onChange = { firstname = it.target.value }
            }
        }
        p {
            +"Имя "
            input {
                type = InputType.text
                value = middleName
                onChange = { middleName = it.target.value }
            }
        }
        p {
            +"Отчество "
            input {
                type = InputType.text
                value = lastname
                onChange = { lastname = it.target.value }
            }
        }
    }
    button {
        +"Добавить учителя"
        onClick = {
            props.addTeacher(Teacher(firstname, middleName, lastname, ""))
        }
    }
}
