package gr.jkapsouras.butterfliesofgreece.base.schedulers

import io.reactivex.rxjava3.core.Scheduler

interface IBackgroundThread{
    val scheduler: Scheduler
}

class BackgroundThreadScheduler(override val scheduler: Scheduler) : IBackgroundThread