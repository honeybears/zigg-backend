package soma.achoom.zigg.global.infra.laodbalancer.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {
    @GetMapping("/")
    fun health() : ResponseEntity<Any> {
        return ResponseEntity.ok().build()
    }
}