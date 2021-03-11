package socialnetwork.service;

import socialnetwork.domain.Friendship;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.User;
import socialnetwork.repository.Repository;
import socialnetwork.repository.database.FriendshipDatabaseRepository;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.PageableImplementation;
import socialnetwork.service.datatrasfer.FriendshipDTO;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.FriendshipChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipService implements Observable<FriendshipChangeEvent> {
    private FriendshipDatabaseRepository repoFriendship;
    private Repository<String, User> userDatabase;

    public FriendshipService(FriendshipDatabaseRepository repositoryFriendship, Repository<String, User> userDatabase) {
        this.repoFriendship = repositoryFriendship;
        this.userDatabase = userDatabase;
    }

    /**
     * adds timestamp to friendship and saves it
     * @param user1
     * @param user2
     * @return null if friendship is saved or the friendship if it alredy exists
     */
    public Friendship addFriendship(String user1, String user2){
        Friendship friendship = repoFriendship.save(new Friendship(LocalDateTime.now(), user1, user2));
        if(friendship!=null)
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.ADD, friendship));
        return friendship;
    }

    /**
     * deletes a friendship
     * @param user1
     * @param user2
     * @return null if friendship does not exists or the friendShip
     */
    public Friendship deleteFriendship(String user1, String user2){
        Friendship friendship = repoFriendship.delete(new Tuple<>(user1, user2));
        if(friendship!=null)
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, friendship));
        return friendship;
    }

    /**
     * returns a list of friends
     * @param id
     * @return list of an users friends
     */
    public Iterable<FriendshipDTO> getFriends(String id){
        List<FriendshipDTO> rez = new ArrayList<>();
        StreamSupport.stream(repoFriendship.findAll(id).spliterator(), false)
                .forEach(fr ->{
                        if(fr.getId().getRight().equals(id)){
                             rez.add(new FriendshipDTO(fr.getId().getLeft(), userDatabase.findOne(fr.getId().getLeft()).getName(), userDatabase.findOne(fr.getId().getLeft()).getBreed(), fr.getDate()));
                        }
                        if(fr.getId().getLeft().equals(id)){
                            rez.add(new FriendshipDTO(fr.getId().getRight(),userDatabase.findOne(fr.getId().getRight()).getName(), userDatabase.findOne(fr.getId().getRight()).getBreed(), fr.getDate()));
                        }
                });
        return rez;

    }

    /**
     * returns all friends from a month
     * @param id
     * @param month month must be vaild
     * @return returns a list of friends form a month
     */
    public Iterable<FriendshipDTO> getFriendsByMonth(String id, int month){
        return StreamSupport.stream(getFriends(id).spliterator(), false)
                .filter(friendshipDTO -> friendshipDTO.getLocalDateTime().getMonthValue()==month)
                .collect(Collectors.toList());
    }

    private List<Observer<FriendshipChangeEvent>> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipChangeEvent t) {
        observers.forEach(x->x.update(t));
    }

    private int pageSize = 6;
    public Iterable<FriendshipDTO> getFriendPage(int page, String user){
        Pageable pageable = new PageableImplementation(page*pageSize, pageSize);
        List<FriendshipDTO> rez = new ArrayList<>();
        StreamSupport.stream(repoFriendship.findByPage(user, pageable).spliterator(), false)
                .forEach(fr ->{
                    if(fr.getId().getRight().equals(user)){
                        rez.add(new FriendshipDTO(fr.getId().getLeft(), userDatabase.findOne(fr.getId().getLeft()).getName(), userDatabase.findOne(fr.getId().getLeft()).getBreed(), fr.getDate()));
                    }
                    if(fr.getId().getLeft().equals(user)){
                        rez.add(new FriendshipDTO(fr.getId().getRight(), userDatabase.findOne(fr.getId().getRight()).getName(), userDatabase.findOne(fr.getId().getRight()).getBreed(), fr.getDate()));
                    }
                });
        return rez;
    }

    public int pageCount(String user) {
        int nrOfEntries = repoFriendship.nrOfEntries(user);
        if(nrOfEntries == 0)
            return 1;
        return (int)Math.ceil(nrOfEntries*1.0/pageSize);
    }
}
