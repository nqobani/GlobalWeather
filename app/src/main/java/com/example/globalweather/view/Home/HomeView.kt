package com.example.globalweather.view.home

interface HomeView <S : HomeViewState> {
    fun render(state: S)
}