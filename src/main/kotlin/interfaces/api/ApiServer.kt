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
import io.ktor.features.CORS
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
        install(CORS) {
            host("localhost:8000")
        }
        install(Routing) {
            route("/api") {
                get<Players> {
                    call.respond(Context.playerApplicationService.all())
                }
                get<Players.Records> { player ->
                    call.respond(Context.playerApplicationService.records(player.rivalId))
                }
                get<Records> { conditions ->
                    call.respond(
                        Context.recordApplicationService.all(
                            conditions.musicTitles,
                            conditions.diffs,
                            conditions.modes,
                            conditions.playerNames,
                            conditions.rivalIds
                        )
                    )
                }
            }
        }
    }

    fun start() = server.start(true)
}

@KtorExperimentalLocationsAPI
@Location("/players") internal class Players {
    @Location("/{rivalId}/records") internal data class Records(val rivalId: Long)
}

@KtorExperimentalLocationsAPI
@Location("/records") internal class Records(
    val musicTitles: List<String> = listOf(),
    val diffs: List<String> = listOf(),
    val modes: List<String> = listOf(),
    val playerNames: List<String> = listOf(),
    val rivalIds: List<Long> = listOf()
)
