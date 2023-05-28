package component.audience

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Audience
import web.html.InputType

external interface AddAudienceProps : Props {
    var addAudience: (Audience) -> Unit
}

val CAudienceAdd = FC<AddAudienceProps>("AudienceAdd") { props ->
    var name by useState("")
    span {
        p {
            +"Аудитория "
            input {
                type = InputType.text
                value = name
                onChange = { name = it.target.value }
            }
        }
    }
    button {
        +"Добавить аудиторию"
        onClick = {
            props.addAudience(Audience(name.toInt(), ""))
        }
    }
}
