package com.example.globalweather.view.Home

interface HomeView <S : HomeViewState> {
    fun render(state: S)
}