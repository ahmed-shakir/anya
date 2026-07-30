package se.supernovait.anya.core.domain.handler

interface EventHandler<T> {
    fun onEvent(event: T)
}
