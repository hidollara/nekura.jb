package application

import domain.RecordQuery
import org.joda.time.DateTime

internal class LatestUpdateService(private val recordQuery: RecordQuery) {
    fun latestUpdates(days: Int) =
        recordQuery.recordsBetween(DateTime.now().minusDays(days), DateTime.now())
}