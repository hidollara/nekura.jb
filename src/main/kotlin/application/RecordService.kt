package application

import domain.RecordQuery
import org.joda.time.DateTime

internal class RecordService(
    private val recordQuery: RecordQuery
) {
    fun latestRecords(days: Int) =
        recordQuery.recordsBetween(DateTime.now().minusDays(days), DateTime.now())
}
