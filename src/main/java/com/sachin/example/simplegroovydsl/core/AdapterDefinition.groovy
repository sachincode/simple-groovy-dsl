package com.sachin.example.simplegroovydsl.core

import groovy.time.Duration
import groovy.time.TimeCategory

class AdapterDefinition {

    Closure givenCall
    // 定时任务执行频率
    long rate
    // 定时任务执行cron表达式
    String cronExp
    Closure scheduleCall
    Closure executeCall


    def given(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        givenCall = closure
    }

    def every = { Duration time ->
        this.rate = time.toMilliseconds()
    }

    def cron = { String expression ->
        this.cronExp = expression
    }

    def run = { Closure c ->
        this.scheduleCall = c
    }

    def schedule(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        use TimeCategory, {
            closure()
        }
    }

    def execute(Closure closure) {
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        executeCall = closure
    }

}
