package interfaces.api

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.datatype.joda.JodaModule
import config.Context
import domain.core.Difficulty
import domain.core.Mode
import domain.core.MusicId
import domain.core.RivalId
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
            }
        }
    }

    fun start() = server.start(true)
}

@KtorExperimentalLocationsAPI
@Location("/musics") internal class Musics

@KtorExperimentalLocationsAPI
@Location("/rankers") internal class Rankers {
    @Location("/{rivalId}") internal data class Ranker(val rivalId: Long)
}
