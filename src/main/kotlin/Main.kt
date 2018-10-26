import config.Context
import interfaces.api.ApiServer

fun main(args: Array<String>) {
    Context.rankingAutoUpdateService.start()
    ApiServer.start()
}
