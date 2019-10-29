import config.Context
import interfaces.api.ApiServer
import io.ktor.locations.KtorExperimentalLocationsAPI

@KtorExperimentalLocationsAPI
fun main(args: Array<String>) {
    Context.rankingAutoUpdateService.start()
    ApiServer.start()
}
