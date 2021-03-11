package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.FriendshipRequest;

public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest entity) throws ValidationException {
        if(entity.getId().getLeft() == entity.getId().getRight())
            throw new ValidationException("Doesn't matter how awesome your pet is cannot befriend himself");
    }
}

