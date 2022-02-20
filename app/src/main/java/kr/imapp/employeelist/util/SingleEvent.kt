package kr.imapp.employeelist.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 * Source -> https://gist.github.com/JoseAlcerreca/5b661f1800e1e654f07cc54fe87441af#file-event-kt
 * reference -> https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150
 */
open class SingleEvent<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

/**
 * Wraps an event into a SingleEvent object
 */
fun <T> MutableLiveData<SingleEvent<T>>.setSingleEvent(event: T) {
    value = SingleEvent(event)
}

fun <T> LiveData<SingleEvent<T>>.observeSingleEvent(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(
        owner,
        Observer { event ->
            event.getContentIfNotHandled()?.let(observer)
        }
    )
}
