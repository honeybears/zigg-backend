package soma.achoom.zigg.global.util

import soma.achoom.zigg.version.v0.space.entity.Space
import soma.achoom.zigg.version.v0.space.entity.SpaceRole
import soma.achoom.zigg.version.v0.space.entity.SpaceUser
import soma.achoom.zigg.version.v0.space.exception.LowSpacePermissionException
import soma.achoom.zigg.version.v0.space.exception.SpaceUserNotFoundInSpaceException
import soma.achoom.zigg.version.v0.user.entity.User


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