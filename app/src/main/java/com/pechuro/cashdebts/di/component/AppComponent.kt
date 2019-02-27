package com.pechuro.cashdebts.di.component

import com.pechuro.cashdebts.App
import com.pechuro.cashdebts.di.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AppActivitiesModule::class,
        AppViewModelsModule::class,
        RxModule::class,
        DatabaseModule::class,
        FirestoreModule::class]
)
interface AppComponent {

    fun inject(app: App)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(app: App): Builder

        fun build(): AppComponent
    }
}