package component.lesson

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Lesson
import web.html.InputType

external interface EditLessonProps : Props {
    var lesson: Lesson
    var saveLesson: (Lesson) -> Unit
}

val CLessonEdit = FC<EditLessonProps>("LessonEdit") { props ->
    var name by useState(props.lesson.name)
    span {
            input {
                type = InputType.text
                value = name
                onChange = { name = it.target.value }
            }
    }
    button {
        +"✓"
        onClick = {
            props.saveLesson(Lesson(name, props.lesson._id))
        }
    }
}

