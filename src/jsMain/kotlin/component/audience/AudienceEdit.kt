package component.audience

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Audience
import web.html.InputType

external interface EditAudienceProps : Props {
    var audience: Audience
    var saveAudience: (Audience) -> Unit
}

val CAudienceEdit = FC<EditAudienceProps>("AudienceEdit") { props ->
    var name by useState(props.audience.classroom.toString())
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
            props.saveAudience(Audience(name.toInt(), props.audience._id))
        }
    }
}

