package soma.achoom.zigg.v0.service

import soma.achoom.zigg.v0.model.Space
import soma.achoom.zigg.v0.model.SpaceUser
import soma.achoom.zigg.v0.model.User

abstract class SpaceAsset():BaseService() {
    protected fun validateSpaceUser(user: User, space:Space):SpaceUser{
        space.spaceUsers.forEach{
            if(it.user == user){
                return it
            }
        }
        throw IllegalArgumentException("User is not in the space")
    }
}