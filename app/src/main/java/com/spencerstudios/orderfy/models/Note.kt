package com.spencerstudios.orderfy.models

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Note(
    @Id var id: Long = 0,
    var noteBody: String = "",
    var timestamp: Long = System.currentTimeMillis()
)
