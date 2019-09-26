package infrastructure.persistence.mysql

import domain.core.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlMusicCommander(private val db: Database) : MusicCommander {
    override fun save(musics: List<Music>) {
        transaction(db) {
            Schema.Musics
                .batchInsertOnDuplicateKeyUpdate(
                    musics.asSequence().withIndex().toList(),
                    Schema.Musics.columns
                ) { batch, music ->
                    batch[mid] = music.value.mid
                    batch[title] = music.value.title
                    batch[order] = music.index
                }
            Schema.Charts
                .batchInsertOnDuplicateKeyUpdate(
                    musics.map { it.charts.values }.flatten(),
                    Schema.Charts.columns
                ) { batch, chart ->
                    batch[mid] = chart.mid
                    batch[diff] = chart.diff
                    batch[level] = chart.level.id
                }
        }
    }
}
