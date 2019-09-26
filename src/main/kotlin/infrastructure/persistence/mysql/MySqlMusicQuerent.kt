package infrastructure.persistence.mysql

import domain.core.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

val bscLv = Expression.build {
    Max(case().When(Schema.Charts.diff eq Difficulty.BASIC, Schema.Charts.level).Else(intLiteral(0)), Schema.Charts.level.columnType)
}
val advLv = Expression.build {
    Max(case().When(Schema.Charts.diff eq Difficulty.ADVANCED, Schema.Charts.level).Else(intLiteral(0)), Schema.Charts.level.columnType)
}
val extLv = Expression.build {
    Max(case().When(Schema.Charts.diff eq Difficulty.EXTREME, Schema.Charts.level).Else(intLiteral(0)), Schema.Charts.level.columnType)
}

internal class MySqlMusicQuerent(private val db: Database) : MusicQuerent {
    override fun all() = transaction(db) {
        (Schema.Musics innerJoin Schema.Charts)
            .slice(
                Schema.Musics.mid,
                Schema.Musics.title,
                Schema.Musics.order,
                bscLv, advLv, extLv
            )
            .selectAll()
            .groupBy(Schema.Musics.mid)
            .orderBy(Schema.Musics.order)
            .map { record ->
                buildMusic(
                    record[Schema.Musics.mid], record[Schema.Musics.title],
                    record[bscLv], record[advLv], record[extLv]
                )
            }
    }

    override fun find(mid: MusicId) = transaction(db) {
        (Schema.Musics innerJoin Schema.Charts)
            .select { Schema.Musics.mid eq mid }
            .first()
            .let { record ->
                buildMusic(
                    record[Schema.Musics.mid], record[Schema.Musics.title],
                    record[bscLv], record[advLv], record[extLv]
                )
            }
    }
}
