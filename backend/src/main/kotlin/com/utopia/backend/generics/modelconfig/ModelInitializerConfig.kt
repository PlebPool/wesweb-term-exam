package com.utopia.backend.generics.modelconfig

import com.thedeanda.lorem.Lorem
import com.thedeanda.lorem.LoremIpsum
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import kotlin.reflect.KClass

abstract class ModelInitializerConfig<T : Any, K, ID>(obj: KClass<T>) {
    val log: Logger = LoggerFactory.getLogger(obj::class.java.name)
    val lorem: Lorem = LoremIpsum.getInstance()
    abstract fun init(repo: ReactiveCrudRepository<K, ID>): CommandLineRunner
}