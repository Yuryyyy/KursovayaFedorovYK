package component.audience

import react.FC
import react.Props
import react.dom.html.ReactHTML.span
import ru.altmanea.webapp.data.Audience

external interface AudienceInListProps : Props {
    var audience: Audience
}

val CAudienceInList = FC<AudienceInListProps>("AudienceInList") { props ->
    span {
        +props.audience.classroom.toString()
    }
}