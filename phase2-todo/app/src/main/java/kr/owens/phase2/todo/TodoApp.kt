package kr.owens.phase2.todo

import android.app.Application
import kr.owens.phase2.todo.di.AppContainer
import kr.owens.phase2.todo.di.DefaultAppContainer

class TodoApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
