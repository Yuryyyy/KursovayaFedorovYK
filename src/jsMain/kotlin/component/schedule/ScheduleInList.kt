package component.schedule

import react.FC
import react.Props
import react.dom.html.ReactHTML.td
import ru.altmanea.webapp.data.Schedule

external interface ScheduleInListProps : Props {
    var schedule: Schedule
}

val CScheduleInList = FC<ScheduleInListProps>("ScheduleInList") { props ->
    td { +props.schedule.name }
    td { +props.schedule.typeoflesson }
    td { +props.schedule.time }
    td { +props.schedule.dayofweek }
    td { +props.schedule.typeofweek }
    td { +props.schedule.group }
    td { +props.schedule.teacher }
    td { +props.schedule.audience.toString() }
}