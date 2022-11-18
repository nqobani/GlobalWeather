package com.example.globalweather.interfaces

import androidx.lifecycle.LiveData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.StateFlow

interface IModel<S: IState, I: IIntent> {
    val intent: Channel<I>
    val state: StateFlow<S>
}