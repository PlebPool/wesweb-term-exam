//package com.utopia.backend.posts.web
//
//import com.utopia.backend.generics.errors.NotFoundException
//import com.utopia.backend.posts.model.Post
//import com.utopia.backend.generics.assemblers.EntityModelAssembler
//import com.utopia.backend.generics.assemblers.ReactiveIdQueryableController
//import com.utopia.backend.generics.assemblers.Vessel
//import com.utopia.backend.generics.beans.SpringManaged
//import com.utopia.backend.posts.model.Liked
//import com.utopia.backend.posts.model.PostRepository
//import org.springframework.hateoas.EntityModel
//import org.springframework.web.bind.annotation.*
//import reactor.core.publisher.Flux
//import reactor.core.publisher.Mono
//import javax.validation.Valid
//
//private const val LINK_REFERENCE_NAME = "posts"
///*
//    IMPORTANT CONTEXT
//    Mono<T> is a stream object where you can expect ONE object.
//    Flux<T> is a stream object where you can expect ONE or MORE objects.
//
//    EntityModel<T> is a class that makes it easier to follow HATEOAS. https://en.wikipedia.org/wiki/HATEOAS
//*/
//
///*
//    @RestController tells Spring that this is a controller. So that spring knows where to route requests.
//    @RequestMapping tells Spring what path this controller manages.
//*/
//@Deprecated("Use functional implementation instead backend/src/main/kotlin/com/utopia/backend/posts/web/functional")
//@RestController
//@RequestMapping("/posts")
//class PostController(
//        private val postRepository: PostRepository<Long>,
//        private val postToEntityModelAssembler: EntityModelAssembler<Post, PostController, Long>
//        // This controller implements Reactive IdQueryableController. Which Is required in order for the
//        // Generic EntityModelAssembler to work.
//) : ReactiveIdQueryableController<Post, Long>, SpringManaged() {
//    /*
//        Example request...
//        {
//            alias: "example alias", (Set to "Anonymous" if empty)
//            title: "example title",
//            content: "example content"
//        }
//        The above JSON (RequestBody) is turned into a Post object.
//        Which is then validated (check @NotNull in Post.kt)
//
//        Which we then pass to our ReactiveCrudRepository. (postRepository)
//        postRepository then converts and inserts the object into our mysql database.
//
//        Returns a Mono stream with a Post Object.
//    */
//    @PostMapping
//    override fun createOne(@Valid @RequestBody body: Post): Mono<EntityModel<Post>> {
//        return postRepository.save(body).map {
//            postToEntityModelAssembler
//                    .assembleEntityModel(Vessel(it.id, LINK_REFERENCE_NAME, it, PostController::class.java))
//        }
//    }
//    /*
//        Takes a Liked object.
//        Updates targeted Post if it exists and returns it.
//    */
//    @PutMapping("/like/{id}")
//    fun like(@PathVariable id: Long, @RequestBody liked: Liked): Mono<EntityModel<Post>> {
//        return postRepository.existsById(id).flatMap {
//            if(it) { // Checks if the row we want to update exists
//                if(liked.isLike) { // Checks whether we want to increment or reduce.
//                    return@flatMap postRepository.like(id).map { post ->
//                        postToEntityModelAssembler // Assembles the updated post into an EntityModel
//                                .assembleEntityModel(Vessel(post.id, LINK_REFERENCE_NAME, post, PostController::class.java))
//                    }
//                } else { // TODO LOOK OVER THIS LATER
//                    return@flatMap postRepository.unlike(id).map { post ->
//                        postToEntityModelAssembler // Assembles the updated post into an EntityModel
//                                .assembleEntityModel(Vessel(post.id, LINK_REFERENCE_NAME, post, PostController::class.java))
//                    }
//                }
//            } else {
//                throw NotFoundException(id, "Post") // Throws and exception that results in a 404 response.
//                // if the row does not exist.
//            }
//        }
//    }
//    /*
//        Gets all the rows from the database via postRepository. And then assembles them into EntityModels
//        that is in accordance with HATEOAS.
//    */
//    @GetMapping
//    override fun getAll(): Flux<EntityModel<Post>> {
//        return postRepository.findAll().map {
//            postToEntityModelAssembler
//                    .assembleEntityModel(Vessel(it.id, LINK_REFERENCE_NAME, it, PostController::class.java))
//        }
//    }
//    /*
//        Gets a post by ID.
//        steps:
//            checks if row exists.
//            gets the row, turns it into a Post object.
//            assembles Post into EntityModel
//        throws NotFoundException if row does not exist. Which produces a 404 response.
//    */
//    @GetMapping("/{id}")
//    override fun getOne(@PathVariable id: Long): Mono<EntityModel<Post>> {
//        return postRepository.existsById(id).flatMap { exists ->
//            if(exists) {
//                return@flatMap postRepository.findById(id).map {
//                    postToEntityModelAssembler
//                            .assembleEntityModel(Vessel(it.id, LINK_REFERENCE_NAME, it, PostController::class.java))
//                }
//            } else {
//                throw NotFoundException(id, "Post")
//            }
//        }
//    }
//}