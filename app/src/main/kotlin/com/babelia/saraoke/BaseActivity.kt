package com.babelia.saraoke

import mini.android.FluxActivity
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI

/**
 * Base activity, for implementing functionality shared by all activities.
 */
abstract class BaseActivity : FluxActivity(), DIAware {
    override val di: DI by closestDI()

    // Implement here common functionality as request permissions or implement
    // onActivityResult
}