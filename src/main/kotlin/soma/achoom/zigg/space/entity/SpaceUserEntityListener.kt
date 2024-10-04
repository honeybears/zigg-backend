package soma.achoom.zigg.space.entity

import jakarta.persistence.PostPersist
import jakarta.persistence.PostUpdate

class SpaceUserEntityListener {
    @PostPersist
    @PostUpdate
    fun postPersist(spaceUser: SpaceUser) {
        spaceUser.space?.let{
            if (!it.users.contains(spaceUser)) {
                it.users.add(spaceUser)
            }
        }
        spaceUser.user?.let {
            if (!it.spaces.contains(spaceUser)) {
                it.spaces.add(spaceUser)
            }
        }
    }
}