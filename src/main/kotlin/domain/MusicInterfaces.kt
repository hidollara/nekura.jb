package domain

internal interface MusicFetcher {
    fun fetch(): Musics
}

internal interface MusicCommand {
    fun pull(fetcher: MusicFetcher)
}

internal interface MusicQuery {
    fun all(): Musics
    fun find(musicId: MusicId): Music
}

internal interface RecordHeaderQuery {
    fun find(mid: MusicId, diff: Difficulty, mode: Mode): RecordHeader
    fun findEarliestUpdated(): RecordHeader
}
