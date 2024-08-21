package soma.achoom.zigg.spaceuser.service

import org.springframework.stereotype.Service
import soma.achoom.zigg.space.entity.Space
import soma.achoom.zigg.spaceuser.repository.SpaceUserRepository

@Service
class SpaceUserService constructor(
    private val spaceUserRepository: SpaceUserRepository
) {
    fun isValidSpaceUser(space: Space,userName:String) : Boolean {
        return spaceUserRepository.existsSpaceUserBySpaceAndUserName(space,userName)
    }
}