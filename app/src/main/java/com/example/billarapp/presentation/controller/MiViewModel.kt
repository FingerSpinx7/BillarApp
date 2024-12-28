package com.example.billarapp.presentation.controller

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class MiViewModel: ViewModel() {
    var texto = mutableStateOf("")
        private set
    fun actualizarTexto(nuevoTexto:String){
        texto.value = nuevoTexto
    }
}