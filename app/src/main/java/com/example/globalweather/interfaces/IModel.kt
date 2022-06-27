package com.example.globalweather.interfaces

import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.Channel

interface IModel<S: IState, I: IIntent> {
    val intent: Channel<I>
    val state: LiveData<S>
}