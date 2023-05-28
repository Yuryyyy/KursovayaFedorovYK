package component.audience

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.useState
import ru.altmanea.webapp.data.Audience

external interface AudienceListProps : Props {
    var audiences: List<Audience>
    var deleteAudience: (String) -> Unit
    var updateAudience: (Audience) -> Unit
}

val CAudienceList = FC<AudienceListProps>("AudienceList") { props ->
    var editedId by useState("")
    ol {
        props.audiences.forEach { audiences ->
            li {
                if (audiences._id == editedId)
                    CAudienceEdit {
                        audience = audiences
                        saveAudience = {
                            props.updateAudience(it)
                            editedId = ""
                        }
                    }
                else
                    CAudienceInList {
                        audience = audiences
                    }
                button {
                    +" ✂ "
                    onClick = {
                        props.deleteAudience(audiences._id)
                    }
                }
                button {
                    +" ✎ "
                    onClick = {
                        editedId = audiences._id
                    }
                }
            }
        }
    }
}
