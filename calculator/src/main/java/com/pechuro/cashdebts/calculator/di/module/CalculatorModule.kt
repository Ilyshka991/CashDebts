package com.pechuro.cashdebts.calculator.di.module

import com.pechuro.cashdebts.calculator.Calculator
import com.pechuro.cashdebts.calculator.impl.CalculatorImpl
import dagger.Binds
import dagger.Module

@Module
internal interface CalculatorModule {

    @Binds
    fun provideCalculator(calculator: CalculatorImpl): Calculator
}