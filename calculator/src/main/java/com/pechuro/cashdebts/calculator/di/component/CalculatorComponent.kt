package com.pechuro.cashdebts.calculator.di.component

import com.pechuro.cashdebts.calculator.Calculator
import com.pechuro.cashdebts.calculator.di.module.CalculatorModule
import dagger.Component

@Component(modules = [CalculatorModule::class])
interface CalculatorComponent {

    fun calculator(): Calculator
}
