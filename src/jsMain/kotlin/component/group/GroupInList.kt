package component.group

import react.FC
import react.Props
import react.dom.html.ReactHTML.span
import ru.altmanea.webapp.data.Group

external interface GroupInListProps : Props {
    var group: Group
}

val CGroupInList = FC<GroupInListProps>("GroupInList") { props ->
    span {
        +props.group.name
    }
}