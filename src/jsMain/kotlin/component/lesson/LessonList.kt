package component.lesson

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.useState
import ru.altmanea.webapp.data.Lesson


external interface LessonListProps : Props {
    var lessons: List<Lesson>
    var deleteLesson: (String) -> Unit
    var updateLesson: (Lesson) -> Unit
}

val CLessonList = FC<LessonListProps>("LessonList") { props ->
    var editedId by useState("")
    ol {
        props.lessons.forEach { lessons ->
            li {
                if (lessons._id == editedId)
                    CLessonEdit {
                        lesson = lessons
                        saveLesson = {
                            props.updateLesson(it)
                            editedId = ""
                        }
                    }
                else
                    CLessonInList {
                        lesson = lessons
                    }
                button {
                    +" ✂ "
                    onClick = {
                        props.deleteLesson(lessons._id)
                    }
                }
                button {
                    +" ✎ "
                    onClick = {
                        editedId = lessons._id
                    }
                }
            }
        }
    }
}
