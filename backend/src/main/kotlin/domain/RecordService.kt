package domain

internal interface RecordService {
    fun search(
        musicTitles: List<String>?,
        diffs: List<Difficulty>?,
        modes: List<Mode>?,
        playerNames: List<String>?,
        rivalIds: List<RivalId>?
    ): List<Record>
}
