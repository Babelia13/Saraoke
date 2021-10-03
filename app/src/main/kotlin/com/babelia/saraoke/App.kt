package com.babelia.saraoke

import android.app.Application
import android.content.Context
import androidx.annotation.RestrictTo
import androidx.lifecycle.ViewModelProvider
import com.babelia.saraoke.lyrics.LyricsModule
import com.babelia.saraoke.lyrics.ui.LyricsViewModel
import com.babelia.saraoke.lyrics.ui.MediaPlaybackViewModel
import com.babelia.saraoke.network.NetworkModule
import mini.Dispatcher
import mini.LoggerMiddleware
import mini.Mini
import mini.Store
import mini.kodein.android.DIViewModelFactory
import mini.kodein.android.bindViewModel
import org.kodein.di.*
import org.kodein.di.conf.ConfigurableDI
import timber.log.Timber
import java.io.Closeable
import kotlin.properties.Delegates

private var appInstance: App by Delegates.notNull()
val app: App get() = appInstance

/**
 * Base [Application] object used in the app.
 */
open class App : Application(), DIAware {

    override val di = ConfigurableDI(mutable = true)

    private lateinit var dispatcher: Dispatcher
    private lateinit var stores: List<Store<*>>
    private lateinit var storeSubscriptions: Closeable

    companion object {
        const val KODEIN_APP_TAG = "AppTag"
    }

    override fun onCreate() {
        appInstance = this
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        initializeInjection(shouldInitializeStores())
    }

    /**
     * Initializes dependency injection.
     */
    private fun initializeInjection(initializeStores: Boolean) {
        if (this::storeSubscriptions.isInitialized) {
            storeSubscriptions.close()
        }

        if (this::stores.isInitialized) {
            stores.forEach { it.close() }
        }

        di.clear()

        di.addImports(
            false,
            AppModule.create(),
            NetworkModule.create(),
            LyricsModule.create(),
            ViewModelsModule.create()
        )

        stores = di.direct.instance<Set<Store<*>>>().toList()
        dispatcher = di.direct.instance()

        dispatcher.addMiddleware(
            LoggerMiddleware(stores, logger = { priority, tag, msg ->
                Timber.tag(tag).d("[$priority] $msg")
            })
        )

        storeSubscriptions = Mini.link(dispatcher, stores)
        if (initializeStores) {
            stores.forEach { store ->
                store.initialize()
            }
        }
    }

    /**
     * Whether or not stores should be initialized.
     *
     * Only useful in UI tests.
     */
    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    open fun shouldInitializeStores() = true
}

/**
 * Kodein module that provides app dependencies.
 */
object AppModule {
    @Suppress("UndocumentedPublicFunction")
    fun create() = DI.Module("AppModule", true) {
        bind<Application>(App.KODEIN_APP_TAG) with singleton { app }
        bind<Context>() with singleton { app }
        bind<Dispatcher>() with singleton { Dispatcher() }

        bind<ViewModelProvider.Factory>() with singleton { DIViewModelFactory(di.direct) }

        bindSet<Store<*>>()
    }
}

/**
 * Kodein module for View Models.
 */
object ViewModelsModule {
    @Suppress("UndocumentedPublicFunction")
    fun create() = DI.Module("HomeViewModelsModule", true) {
        bindViewModel { MediaPlaybackViewModel(instance(App.KODEIN_APP_TAG)) }
        bindViewModel { LyricsViewModel(instance(App.KODEIN_APP_TAG)) }
    }
}

private fun ConfigurableDI.addImports(allowOverride: Boolean, vararg moduleInfo: DI.Module) {
    moduleInfo.forEach { addImport(it, allowOverride) }
}