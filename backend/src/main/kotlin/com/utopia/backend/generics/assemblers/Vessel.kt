package com.utopia.backend.generics.assemblers
/*
    This class is used together with EntityModelAssembler.kt. It means we do not have to make a new assembler
    for every controller we make.
    We can just use this vessel class like a... vessel. To carry our objects into the assembler,
    granting it the features required to pass.
    @TypeParam T = Class of the object we wish to turn into an EntityModel
    @TypeParam K = Class of the controller which handles <T>. (This is needed for linking)
    @TypeParam ID = This is the Class of the ID that is used by the <T>
    @Variable id = This is the id of <T>. That said, <T> needs to have an id for the linking to work.
    @Variable parentRelString = This is the string that will represent the link to the aggregate root of <T>
    @Variable content = This is just the object we want to turn into an EntityModel.
    @Variable controllerClass = This is the class of the controller which we need for linking.
    @Also <K> needs to implement/extend IdQueryableController.kt. This is because the controller needs to have
    one(), and all() methods.
 */
data class Vessel<T : Any, K, ID>(
        val id: ID,
        val parentRelString: String,
        val content: T,
        val controllerClass: Class<K>
) where K : ReactiveIdQueryableController<T, ID>