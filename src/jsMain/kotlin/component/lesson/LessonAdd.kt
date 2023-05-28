package component.lesson

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Lesson
import web.html.InputType

external interface AddLessonProps : Props {
    var addLesson: (Lesson) -> Unit
}

val CLessonAdd = FC<AddLessonProps>("LessonAdd") { props ->
    var name by useState("")
    span {
        p {
            +"Урок "
            input {
                type = InputType.text
                value = name
                onChange = { name = it.target.value }
            }
        }
    }
    button {
        +"Добавить урок"
        onClick = {
            props.addLesson(Lesson(name, ""))
        }
    }
}