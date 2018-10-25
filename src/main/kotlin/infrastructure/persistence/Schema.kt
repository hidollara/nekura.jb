package infrastructure.persistence

import domain.Difficulty
import domain.Mode
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

internal object Schema {
    object Musics : Table() {
        val mid = integer("mid").primaryKey()
        val title = varchar("title", 64)
        val order = integer("order").uniqueIndex()
    }

    object Players : Table() {
        val rivalId = long("rival_id").primaryKey()
        val name = varchar("name", 8)
    }

    object Charts : Table() {
        val mid = (integer("mid") references Musics.mid).primaryKey(0)
        val mode = enumeration("mode", Mode::class).primaryKey(1)
        val diff = enumeration("diff", Difficulty::class).primaryKey(2)
        val lastUpdatedAt = datetime("last_updated_at")
    }

    object Records : Table() {
        val mid = (integer("mid") references Musics.mid).primaryKey(0)
        val mode = enumeration("mode", Mode::class).primaryKey(1)
        val diff = enumeration("diff", Difficulty::class).primaryKey(2)
        val rivalId = (long("rival_id") references Players.rivalId).primaryKey(3)
        val recordedAt = datetime("recorded_at")
        val bestScore = integer("best_score")
    }

    fun drop(db: Database) = transaction(db) {
        drop(Musics, Players, Charts, Records)
    }

    fun create(db: Database) = transaction(db) {
        create(Musics, Players, Charts, Records)
    }
}
