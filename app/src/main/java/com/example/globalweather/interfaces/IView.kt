package com.example.globalweather.interfaces

interface IView<S : IState> {
    fun render(state: S)
}