package socialnetwork.domain.validators;

import socialnetwork.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getId().getLeft() == entity.getId().getRight())
            throw new ValidationException("Doesn't matter how awesome your pet is cannot befriend himself");
    }
}
