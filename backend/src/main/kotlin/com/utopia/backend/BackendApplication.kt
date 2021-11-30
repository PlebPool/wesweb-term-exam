package com.utopia.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(excludeFilters = [ComponentScan.Filter(Deprecated::class)])
class BackendApplication
// https://spring.io/guides/gs/accessing-data-r2dbc/
// https://www.baeldung.com/spring-webflux
// https://www.baeldung.com/spring-5-functional-web
fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}
