package component.lesson

import react.FC
import react.Props
import react.dom.html.ReactHTML.span
import ru.altmanea.webapp.data.Lesson

external interface LessonInListProps : Props {
    var lesson: Lesson
}

val CLessonInList = FC<LessonInListProps>("LessonInList") { props ->
    span {
        +props.lesson.name
    }
}