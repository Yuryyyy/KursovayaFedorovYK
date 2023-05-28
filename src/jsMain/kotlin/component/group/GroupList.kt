package component.group

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.useState
import ru.altmanea.webapp.data.Group

external interface GroupListProps : Props {
    var groups: List<Group>
    var deleteGroup: (String) -> Unit
    var updateGroup: (Group) -> Unit
}

val CGroupList = FC<GroupListProps>("GroupList") { props ->
    var editedId by useState("")
    ol {
        props.groups.forEach { groups ->
            li {
                if (groups._id == editedId)
                    CGroupEdit {
                        group = groups
                        saveGroup = {
                            props.updateGroup(it)
                            editedId = ""
                        }
                    }
                else
                    CGroupInList {
                        group = groups
                    }
                button {
                    +" ✂ "
                    onClick = {
                        props.deleteGroup(groups._id)
                    }
                }
                button {
                    +" ✎ "
                    onClick = {
                        editedId = groups._id
                    }
                }
            }
        }
    }
}
