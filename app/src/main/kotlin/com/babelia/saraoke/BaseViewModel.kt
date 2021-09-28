package com.babelia.saraoke

import android.app.Application
import mini.android.FluxViewModel
import mini.kodein.android.FluxTypedViewModel
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

/**
 * Base ViewModel which implements [DIAware].
 */
abstract class BaseViewModel(app: Application) : FluxViewModel(), DIAware {
    override val di by closestDI(app)
}

/**
 * Base [TypedViewModel] which implements [DIAware].
 */
abstract class BaseTypedViewModel<T>(app: Application, params: T) : FluxTypedViewModel<T>(params), DIAware {
    override val di by closestDI(app)
}