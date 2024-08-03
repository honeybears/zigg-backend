package soma.achoom.zigg.v0.service

import soma.achoom.zigg.v0.exception.LowSpacePermission
import soma.achoom.zigg.v0.exception.NoExistsSpaceUser
import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser
import soma.achoom.zigg.v0.model.User
import soma.achoom.zigg.v0.model.enums.SpaceRole

abstract class SpaceAsset() : BaseService() {
    protected fun validateSpaceUser(user: User, space: Space): SpaceUser {
        space.spaceUsers.find {
            it.user == user
        }?.let {
            return it
        }
        throw NoExistsSpaceUser()
    }
    protected fun validateSpaceUserRoleIsAdmin(user: User, space: Space): SpaceUser {
        space.spaceUsers.find {
            it.user == user && it.spaceRole == SpaceRole.ADMIN
        }?.let {
            return it
        }
        throw LowSpacePermission()
    }
}