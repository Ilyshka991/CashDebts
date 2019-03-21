package com.pechuro.cashdebts.di.component

import com.pechuro.cashdebts.App
import com.pechuro.cashdebts.di.annotations.AppScope
import com.pechuro.cashdebts.di.module.*
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(
    dependencies = [DataComponent::class],
    modules = [
        ApplicationModule::class,
        ManagerModule::class,
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

        fun dataComponent(component: DataComponent): Builder

        fun build(): AppComponent
    }
}