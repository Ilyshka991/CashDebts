package com.pechuro.cashdebts.di.component

import com.pechuro.cashdebts.App
import com.pechuro.cashdebts.di.module.AppActivitiesModule
import com.pechuro.cashdebts.di.module.AppViewModelsModule
import com.pechuro.cashdebts.di.module.ApplicationModule
import com.pechuro.cashdebts.di.module.RxModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AppActivitiesModule::class,
        AppViewModelsModule::class,
        RxModule::class]
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