package gr.jkapsouras.butterfliesofgreece.base.schedulers

import io.reactivex.rxjava3.core.Scheduler

interface IMainThread{
    val scheduler: Scheduler
}

class MainThreadScheduler(override val scheduler: Scheduler) : IMainThread