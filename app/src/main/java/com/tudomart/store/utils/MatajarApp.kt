package com.tudomart.store.utils

import com.tudomart.store.helpers.network.RequestController

class MatajarApp : com.tudomart.store.helpers.network.RequestController() {
    companion object {
        private lateinit var instance: MatajarApp

        fun get() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
