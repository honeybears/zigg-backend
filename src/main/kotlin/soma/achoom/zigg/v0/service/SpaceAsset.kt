package soma.achoom.zigg.v0.service

import soma.achoom.zigg.v0.exception.LowSpacePermissionException
import soma.achoom.zigg.v0.exception.SpaceUserNotFoundInSpaceException
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
        throw SpaceUserNotFoundInSpaceException()
    }
    protected fun validateSpaceUserRoleIsAdmin(user: User, space: Space): SpaceUser {
        space.spaceUsers.find {
            it.user == user && it.spaceRole == SpaceRole.ADMIN
        }?.let {
            return it
        }
        throw LowSpacePermissionException()
    }
}