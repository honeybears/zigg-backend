package soma.achoom.zigg.space.service

import org.springframework.stereotype.Service
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.space.repository.SpaceUserRepository
import java.util.UUID

@Service
class SpaceUserService constructor(
    private val spaceUserRepository: SpaceUserRepository
) {
    fun isValidSpaceUser(space: Space,userName:String) : Boolean {
        return spaceUserRepository.existsSpaceUserBySpaceAndUserName(space,userName)
    }
}