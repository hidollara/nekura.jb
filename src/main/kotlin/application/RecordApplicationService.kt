package application

import domain.*

internal class RecordApplicationService(
    private val recordService: RecordService
) {
    fun all(
        musicTitles: List<String>,
        diffs: List<String>,
        modes: List<String>,
        playerNames: List<String>,
        rivalIds: List<Long>
    ) = recordService.search(
        musicTitles,
        diffs.map { Difficulty.valueOf(it) },
        modes.map { Mode.valueOf(it) },
        playerNames,
        rivalIds
    )
}
