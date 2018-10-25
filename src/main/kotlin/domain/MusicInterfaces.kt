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
