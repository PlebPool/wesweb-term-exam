package com.utopia.backend.generics.assemblers

import com.utopia.backend.generics.beans.SpringManaged
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn
import org.springframework.stereotype.Component

@Component
class EntityModelAssembler<T : Any, K : ReactiveIdQueryableController<T, ID>, ID> : SpringManaged() {
    /*
        Turns a Vessel<T, K> into an EntityModel<T> with proper linking.
        <K> is the controller we're linking <T> to. <K> needs to implement/extend IdQueryableController<T>
        Look at Vessel class for more info.
     */
    fun assembleEntityModel(vessel: Vessel<T, K, ID>): EntityModel<T> {
        return EntityModel.of(vessel.content,
                linkTo(methodOn(vessel.controllerClass).getOne(vessel.id)).withSelfRel(),
                linkTo(methodOn(vessel.controllerClass).getAll()).withRel(vessel.parentRelString))
    }
}