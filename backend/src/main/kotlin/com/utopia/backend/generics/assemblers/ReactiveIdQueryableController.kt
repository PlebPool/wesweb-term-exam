package com.utopia.backend.generics.assemblers

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.swing.text.html.parser.Entity

/*
    See EntityModelAssembler and Vessel for clues as to what this does.
 */
interface ReactiveIdQueryableController<T, ID> {
    fun getAll(): Flux<EntityModel<T>>
    fun getOne(@PathVariable id: ID): Mono<EntityModel<T>>
    fun createOne(@RequestBody body: T): Mono<EntityModel<T>>
}