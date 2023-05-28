package component.group

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Group
import web.html.InputType

external interface EditGroupProps : Props {
    var group: Group
    var saveGroup: (Group) -> Unit
}

val CGroupEdit = FC<EditGroupProps>("GroupEdit") { props ->
    var name by useState(props.group.name)
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
            props.saveGroup(Group(name, props.group._id))
        }
    }
}

