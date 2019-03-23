package interfaces.api

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.joda.JodaModule
import config.Context
import domain.Difficulty
import domain.Mode
import domain.MusicId
import domain.RivalId
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.locations.*
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.route
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

@KtorExperimentalLocationsAPI
object ApiServer {
    private val server = embeddedServer(Netty, port = 8080) {
        install(CallLogging)
        install(Locations)
        install(ContentNegotiation) {
            jackson {
                configure(SerializationFeature.INDENT_OUTPUT, true)
                registerModule(JodaModule())
                dateFormat = StdDateFormat()
            }
        }
        install(Routing) {
            route("/api") {
                get<Musics> { call.respond(Context.musicService.all()) }
                get<Rankers> { call.respond(Context.rankerService.all()) }
                get<Rankers.Ranker> { ranker ->
                    call.respond(Context.rankerService.records(ranker.rivalId))
                }
                get<Ranking> { ranking ->
                    call.respond(Context.rankingService.ranking(ranking.mid, ranking.diff, ranking.mode))
                }
                get<Record> { record ->
                    call.respond(Context.recordService.latestRecords(record.range))
                }
            }
        }
    }

    fun start() = server.start(true)
}

@KtorExperimentalLocationsAPI
@Location("/musics") internal class Musics

@KtorExperimentalLocationsAPI
@Location("/rankers") internal class Rankers {
    @Location("/{rivalId}") internal data class Ranker(val rivalId: RivalId)
}

@KtorExperimentalLocationsAPI
@Location("/rankings")
internal data class Ranking(val mid: MusicId, val mode: Mode, val diff: Difficulty)

@KtorExperimentalLocationsAPI
@Location("/records")
internal data class Record(val range: Int = 7)
