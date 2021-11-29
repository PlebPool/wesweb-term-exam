package com.utopia.backend.posts.model.config

import com.utopia.backend.generics.beans.SpringManaged
import com.utopia.backend.generics.modelconfig.ModelInitializerConfig
import com.utopia.backend.posts.model.Post
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit
/*
    Creates a @Configuration bean which Spring uses to initialize the @Bean's it configures.
    We only have one @Bean here.
*/
@Suppress("unused")
@Configuration
class PostConfig: ModelInitializerConfig<PostConfig, Post, Long>(PostConfig::class) {
    val init = true
    @Bean
    override fun init(repo: ReactiveCrudRepository<Post, Long>): CommandLineRunner {
        if(init) {
            var content = "<h2>Lorem ipsum dolor sit amet, consectetur adipiscing elit.</h2> Etiam condimentum neque non neque scelerisque commodo. Maecenas finibus, diam sed auctor accumsan, diam libero tempus ante, sed cursus dolor neque mollis lacus. Phasellus non ultrices libero. Nam placerat suscipit pharetra. Nam semper sit amet nulla eu ultrices. Nullam ut erat sed dolor rutrum aliquet ut non lacus. Pellentesque eleifend efficitur lacus sit amet cursus. Pellentesque at enim justo. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Vestibulum quis ex eu enim ullamcorper tristique vitae varius sapien. Sed massa nisl, posuere eu ex id, tempus gravida lacus. Nulla facilisi. Mauris aliquam ex purus, at condimentum nisi faucibus porttitor.\n" +
                    "\n" +
                    "<h2>Sed sem purus, euismod sit amet orci in, pellentesque malesuada turpis.</h2> Praesent pulvinar, massa at cursus fermentum, sem mi dictum sapien, at aliquet urna elit et eros. Sed convallis posuere fermentum. Sed non tempus mauris, ac semper ex. Interdum et malesuada fames ac ante ipsum primis in faucibus. Sed commodo tellus dignissim mauris aliquam rhoncus. Nam tincidunt accumsan dapibus. Nulla eros turpis, facilisis eu imperdiet vel, tincidunt sed mi. Donec porta vitae arcu ut porta.\n" +
                    "\n" +
                    "<h2>Aliquam erat diam, iaculis nec nibh sit amet, porta auctor magna.</h2> Curabitur sed mi nec ante facilisis suscipit. Nullam fringilla erat in odio egestas suscipit. Maecenas non ultricies metus, ac volutpat turpis. Morbi molestie sollicitudin nisi a pulvinar. Curabitur lacus magna, semper vitae libero vitae, lacinia pellentesque felis. Nulla vestibulum ante in mattis fringilla. Vestibulum ligula urna, faucibus ut enim nec, eleifend porta felis. Quisque ut aliquet neque. Sed sollicitudin libero non nulla rhoncus volutpat.\n" +
                    "\n" +
                    "Nam nec auctor ex. Phasellus non ligula dictum, sagittis elit nec, placerat odio. Morbi a metus semper, sagittis dui sed, hendrerit felis. Duis consequat quam posuere mattis interdum. Nunc a orci risus. Aliquam erat volutpat. Aliquam risus purus, placerat sed pulvinar vitae, dignissim vitae metus. In hac habitasse platea dictumst. Nam molestie ut justo eget egestas. Vestibulum volutpat diam mi, sed dictum augue eleifend volutpat. Interdum et malesuada fames ac ante ipsum primis in faucibus. Nam vitae venenatis est. Donec ut finibus est, quis blandit diam. Etiam interdum at dolor ut convallis. Proin sodales tincidunt urna quis interdum. Aliquam erat volutpat.\n" +
                    "\n" +
                    "Curabitur rhoncus tortor sit amet leo porttitor, sed tempus est euismod. Donec cursus ornare odio, ut tempus orci tristique eget. Nullam euismod massa a nisl tincidunt lacinia. Donec lobortis odio vel rutrum blandit. Morbi euismod consequat leo sed ultricies. Etiam fermentum ornare congue. Donec finibus blandit ex, in auctor sem commodo eu. Nam at varius est. Cras a lacinia nunc, in malesuada turpis. Quisque risus est, consectetur et lectus vel, placerat posuere ex. Maecenas ullamcorper enim eget augue semper vulputate. Integer molestie lacus cursus, pulvinar eros nec, blandit tellus. Ut at ipsum nibh. Fusce gravida vitae diam non ultrices. In mi dolor, ornare et volutpat eget, consequat vitae augue."

            content = content.replace("\n", "\\n")
            for(i in 1..6) {
                log.info("Preloading: " + repo.save(Post(0,
                        lorem.name,
                        lorem.getTitle(2, 4),
                        content))
                        .block())
            }
        }
        return CommandLineRunner {  }
    }
}