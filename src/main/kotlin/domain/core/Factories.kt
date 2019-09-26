package domain.core

internal fun buildMusic(mid: MusicId, title: MusicTitle, bscLvId: Int?, advLvId: Int?, extLvId: Int?) =
    Music(
        mid,
        title,
        mapOf(
            Difficulty.BASIC to Chart(mid, Difficulty.BASIC, Level(bscLvId)),
            Difficulty.ADVANCED to Chart(mid, Difficulty.ADVANCED, Level(advLvId)),
            Difficulty.EXTREME to Chart(mid, Difficulty.EXTREME, Level(extLvId))
        )
    )