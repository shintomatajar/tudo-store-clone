package ae.tudomart.store.utils

import ae.tudomart.store.helpers.network.RequestController

class MatajarApp : RequestController() {
    companion object {
        private lateinit var instance: MatajarApp

        fun get() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
