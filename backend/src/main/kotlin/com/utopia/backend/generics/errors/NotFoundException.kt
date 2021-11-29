package com.utopia.backend.generics.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import kotlin.reflect.KClass
/*
    When a controller method throws this exception, spring generates a 404 response.
    Because of @ResponseStatus(value = HttpStatus.NOT_FOUND)
*/
@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NotFoundException(id: Any, type: String): RuntimeException("Could not find $type: $id")
