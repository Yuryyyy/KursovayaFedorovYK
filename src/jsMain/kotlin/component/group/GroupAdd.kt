package component.group

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Group
import web.html.InputType

external interface AddGroupProps : Props {
    var addGroup: (Group) -> Unit
}

val CGroupAdd = FC<AddGroupProps>("GroupAdd") { props ->
    var name by useState("")
    span {
        p {
            +"Группа "
            input {
                type = InputType.text
                value = name
                onChange = { name = it.target.value }
            }
        }
    }
    button {
        +"Добавить группу"
        onClick = {
            props.addGroup(Group(name, ""))
        }
    }
}
