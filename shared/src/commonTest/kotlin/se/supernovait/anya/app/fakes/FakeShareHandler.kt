package se.supernovait.anya.app.fakes

import se.supernovait.anya.core.domain.sharing.ShareHandler

class FakeShareHandler : ShareHandler {
    var lastType: String? = null
    var lastData: String? = null

    override fun shareData(type: String, data: String) {
        lastType = type
        lastData = data
    }
}
