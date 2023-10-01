package com.example.hard_task2.model

import com.google.gson.annotations.SerializedName

data class City( @SerializedName("Text") val name:String, @SerializedName("Value") val stateValue:String){
    override fun toString(): String {
        return name
    }
}
