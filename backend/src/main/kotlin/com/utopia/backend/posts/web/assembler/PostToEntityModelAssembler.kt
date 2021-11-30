//package com.utopia.backend.posts.web.assembler
//
//import com.utopia.backend.posts.model.post.Post
//import com.utopia.backend.posts.web.PostController
//import org.slf4j.LoggerFactory
//import org.springframework.hateoas.EntityModel
//import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
//import org.springframework.stereotype.Service
//
//@Deprecated("Use EntityModelAssembler instead")
//@Service
//class PostToEntityModelAssembler {
//    init {
//        LoggerFactory.getLogger(this::class.java.name).info(this::class.java.name)
//    }
//    fun assemble(post: Post): EntityModel<Post> {
//        return EntityModel.of(post,
//                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController::class.java).getOne(post.id)).withSelfRel(),
//                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PostController::class.java).getAll()).withRel("post"))
//    }
//}