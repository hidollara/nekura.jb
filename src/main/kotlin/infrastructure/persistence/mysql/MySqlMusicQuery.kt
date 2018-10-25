package infrastructure.persistence.mysql

import domain.*
import infrastructure.persistence.Schema
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

internal class MySqlMusicQuery(private val db: Database) : MusicQuery {
    override fun all() = transaction(db) {
        Schema.Musics
            .selectAll()
            .orderBy(Schema.Musics.order)
            .map { record -> Music(record[Schema.Musics.mid], record[Schema.Musics.title]) }
    }

    override fun find(musicId: MusicId) = transaction(db) {
        Schema.Musics
            .select { Schema.Musics.mid eq musicId }
            .first()
            .let { record -> Music(record[Schema.Musics.mid], record[Schema.Musics.title]) }
    }
}
