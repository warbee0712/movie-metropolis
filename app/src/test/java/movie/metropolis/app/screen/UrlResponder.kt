package movie.metropolis.app.screen

import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respondOk
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Url

typealias Responder = suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData

class UrlResponder : Responder {

    private val responses = mutableMapOf<Url, String>()

    fun onUrlRespond(url: Url, name: String) {
        responses[url] = file(name).ifEmpty { name }
    }

    override suspend fun invoke(p1: MockRequestHandleScope, p2: HttpRequestData): HttpResponseData {
        return p1.respondOk(responses.getValue(p2.url))
    }

    private fun file(name: String) = Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(name)
        ?.use { it.readBytes() }
        ?.let(::String).orEmpty()

    companion object {

        private const val Domain = "https://www.cinemacity.cz"
        private const val DataService = "data-api-service"
        private const val CustomerService = "group-customer-service"

        val Cinema =
            Url("$Domain/mrest/cinema?lang=en")

        fun EventOccurrence(cinema: String, date: String) =
            Url("$Domain/cz/$DataService/v1/quickbook/10101/film-events/in-cinema/${cinema}/at-date/${date}?lang=en")

        fun CinemaLocation(lat: Double, lng: Double) =
            Url("$Domain/cz/$DataService/v1/10101/cinema/bylocation?lat=${lat}&long=${lng}&unit=KILOMETERS")

        fun Detail(id: String) =
            Url("$Domain/cz/$DataService/v1/10101/films/byDistributorCode/${id}?lang=en")

        fun MoviesByShowing(type: String) =
            Url("$Domain/cz/$DataService/v1/10101/films/by-showing-type/${type}?lang=en&ordering=asc")

        val Password =
            Url("$Domain/cz/$CustomerService/v1/password?reCaptcha=abc")

        val Register =
            Url("$Domain/cz/$CustomerService/v1/customers?reCaptcha=abc")

        val Customer =
            Url("$Domain/cz/$CustomerService/v1/customers/current")

        val CustomerPoints =
            Url("$Domain/cz/$CustomerService/v1/customer/points")

        val Auth =
            Url("$Domain/cz/$CustomerService/oauth/token")

        val Booking =
            Url("$Domain/cz/$CustomerService/v1/bookings")

    }

}