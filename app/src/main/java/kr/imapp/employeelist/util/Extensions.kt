package kr.imapp.employeelist.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.properties.ObservableProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun FragmentActivity.fragmentTransaction(
    backStackName: String? = null,
    block: FragmentTransaction.() -> Unit
) {
    val transaction = supportFragmentManager.beginTransaction().apply(block)
    if (backStackName != null) {
        transaction.addToBackStack(backStackName)
    }
    transaction.commit()
}

inline fun <reified T> AppCompatActivity.getIntentExtra(key: String, default: T? = null): T {
    val isNullable = null is T
    var value = intent?.extras?.get(key)
    if (value == null || value !is T) {
        value = default
    }
    if (value == null && !isNullable) {
        throw Exception("Unable to get $key from intent")
    }
    return value as T
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(this, InputMethodManager.SHOW_FORCED)
}

fun View.hideKeyBoard() {
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

inline fun <reified T> Fragment.getFragmentArgument(key: String, default: T? = null): T {
    val isNullable = null is T
    var value = arguments?.get(key)
    if (value == null || value !is T) {
        value = default
    }
    if (value == null && !isNullable) {
        throw Exception("Unable to get $key from bundle argument")
    }
    return value as T
}

inline fun <reified T> savedState(
    key: String,
    savedStateHandle: SavedStateHandle
): ReadWriteProperty<Any?, T?> {
    val initialValue = savedStateHandle.get<T?>(key)
    return object : ObservableProperty<T?>(initialValue) {
        override fun afterChange(property: KProperty<*>, oldValue: T?, newValue: T?) {
            savedStateHandle.set(key, newValue)
        }
    }
}

inline fun <reified T> Flow<T>.saveState(
    key: String,
    savedStateHandle: SavedStateHandle
): Flow<T> {
    var state: T? by savedState(key, savedStateHandle)
    return distinctUntilChanged()
        .onEach { state = it }
        .onStart { state?.let { emit(it) } }
}

inline fun <T : ViewModel?> Fragment.viewModelFactory(crossinline provider: ((savedStateHandle: SavedStateHandle)->T)) =
    object : AbstractSavedStateViewModelFactory(this, null) {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel?> create(
            key: String,
            modelClass: Class<VM>,
            handle: SavedStateHandle
        ): VM {
            return provider(handle) as VM
        }
    }
