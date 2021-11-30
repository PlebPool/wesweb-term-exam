package com.utopia.backend.generics.beans

import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class SpringManaged {
    val log: Logger = LoggerFactory.getLogger(this::class.java.name)
    init {
        log.info("Initialized: ${this::class.java.name}")
    }
}