package se.supernovait.anya.app.fakes

import se.supernovait.anya.app.domain.repository.InsultCensorRepository
import se.supernovait.anya.core.domain.model.error.NetworkError
import se.supernovait.anya.core.domain.util.Result

class FakeInsultCensorRepository : InsultCensorRepository {
    private var counter = 0
    var shouldReturnError = false
    var censoredResponse = "censored"

    override suspend fun censorWords(uncensored: String): Result<String, NetworkError> {
        return if (shouldReturnError) {
            Result.Failure(NetworkError.SERVER_ERROR)
        } else {
            Result.Success(censoredResponse)
        }
    }

    override suspend fun getCounter(): Int = counter

    override suspend fun incrementCounter(): Int {
        counter++
        return counter
    }
}
