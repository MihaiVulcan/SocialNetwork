package socialnetwork.service;

import socialnetwork.domain.*;
import socialnetwork.repository.Repository;
import socialnetwork.service.datatrasfer.EventDTO;
import socialnetwork.utils.events.ChangeEventType;
import socialnetwork.utils.events.EventChangeEvent;
import socialnetwork.utils.observer.Observable;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EventService implements Observable<EventChangeEvent> {
    private Repository<String, User> userRepository;
    private Repository<Long, Event> eventRepository;
    private Repository<Tuple<String, Long>, EventUser> participantRepositry;
    private Repository<Tuple<Tuple<String, Long>, TimeNotification>, Notification> notificationRepository;

    public EventService(Repository<String, User> userRepository, Repository<Long, Event> eventRepository, Repository<Tuple<String, Long>, EventUser> participantRepositry, Repository<Tuple<Tuple<String, Long>, TimeNotification>, Notification> notificationRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.participantRepositry = participantRepositry;
        this.notificationRepository = notificationRepository;
    }

    /**
     * adds a new event
     * @param name
     * @param description
     * @param time
     * @return String done if is saved or "nu bine" if is not saved
     */
    public String addEvent(String name, String description, LocalDateTime time){
        String ret = "done";
        Event event = eventRepository.save(new Event(name, description, time));
        if(event == null)
            ret = "nu bine";
        else
            notifyObservers(new EventChangeEvent(ChangeEventType.ADD, null));
        return  ret;
    }

    /**
     * adds user to event
     * @param user
     * @param event
     * @return String done if is saved or "nu bine" if is not saved
     */
    public String participate(String user, Long event){
        String ret = "done";
        EventUser eventUser = participantRepositry.save(new EventUser(user, event));
        if(event == null)
            ret = "nu bine";
        else{
            notifyObservers(new EventChangeEvent(ChangeEventType.ADD, eventUser));
            addNotifications(user, eventRepository.findOne(event));
        }
        return  ret;
    }

    /**
     * removes user to event
     * @param user
     * @param event
     * @return String done if is removed or "nu bine" if is not removed
     */
    public String noParticipate(String user, Long event){
        String ret = "done";
        EventUser eventUser = participantRepositry.delete(new Tuple<>(user, event));
        if(event == null)
            ret = "nu bine";
        else{
            notifyObservers(new EventChangeEvent(ChangeEventType.DELETE, eventUser));
            deleteNotifications(user, event, TimeNotification.NOW);
        }

        return  ret;
    }

    public Iterable<EventDTO> userEvents(String user){
        List<EventDTO> rez = new ArrayList<>();
        StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                .forEach(event -> {
                    if(participantRepositry.findOne(new Tuple<>(user, event.getId()))!=null){
                        rez.add( new EventDTO(event.getId(), event.getName(), event.getDescription(), event.getTime(), true ));
                    }
                    else{
                        rez.add( new EventDTO(event.getId(), event.getName(), event.getDescription(), event.getTime(), false ));
                    }
                });
        Collections.sort(rez, new Comparator<EventDTO>() {
            @Override
            public int compare(EventDTO o1, EventDTO o2) {
               if(o1.isJoined()!=o2.isJoined())
                   return -Boolean.compare(o1.isJoined(), o2.isJoined());
               return o1.getTime().compareTo(o2.getTime());
            }
        });
        return rez;
    }

    public void addNotifications(String user, Event event){
        for(var time: TimeNotification.values()){
            switch(time){
                case NOW: notificationRepository.save(new Notification(user, event.getId(), time));
                    break;
                case FIVEMINUTES:
                    if(ChronoUnit.SECONDS.between(LocalDateTime.now(), event.getTime())>=120L)
                        notificationRepository.save(new Notification(user, event.getId(), time));
                    break;
                case ONEHOUR:
                    if(ChronoUnit.MINUTES.between(LocalDateTime.now(), event.getTime())>=10L)
                        notificationRepository.save(new Notification(user, event.getId(), time));
                    break;
                case TOMORROW:
                    if(ChronoUnit.HOURS.between(LocalDateTime.now(), event.getTime())>=2L)
                        notificationRepository.save(new Notification(user, event.getId(), time));
                    break;
            }
        }
        notifyObservers(new EventChangeEvent(ChangeEventType.ADD, null));
    }

    /**
     * delets notification older or equal that what time idicates
     * @param user
     * @param event
     * @param time
     */
    public void deleteNotifications(String user, Long event, TimeNotification time){
        switch (time){
            case NOW:
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.NOW));
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.FIVEMINUTES));
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.ONEHOUR));
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.TOMORROW));
                break;
            case FIVEMINUTES:
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.FIVEMINUTES));
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.ONEHOUR));
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.TOMORROW));
                break;
            case ONEHOUR:
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.ONEHOUR));
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.TOMORROW));
                break;
            case TOMORROW:
                notificationRepository.delete(new Tuple<>(new Tuple<>(user, event), TimeNotification.TOMORROW));
                break;
        }
        notifyObservers(new EventChangeEvent(ChangeEventType.ADD, null));
    }

    public Iterable<NotificationDTO> notificationsToShow(String user){
        List<Notification> notifications = StreamSupport.stream(notificationRepository.findAll().spliterator(), false)
                .filter(not->not.getId().getLeft().getLeft().equals(user))
                .collect(Collectors.toList());

        Map<Long, Event> events = StreamSupport.stream(eventRepository.findAll().spliterator(), false)
                            .filter(event -> participantRepositry.findOne(new Tuple<>(user, event.getId()))!= null)
                            .collect(Collectors.toMap(Entity::getId, event->event));

        List<NotificationDTO> now = notifications.stream()
                                .filter(not-> not.getId().getRight().equals(TimeNotification.NOW))
                                .filter(not-> ChronoUnit.SECONDS.between(LocalDateTime.now(), events.get(not.getId().getLeft().getRight()).getTime())<=60)
                                .map(not -> new NotificationDTO(not.getId().getLeft().getRight(), events.get(not.getId().getLeft().getRight()).getName(), events.get(not.getId().getLeft().getRight()).getTime(), "Soon"))
                                .collect(Collectors.toList());

        now.forEach(e->deleteNotifications(user, e.getId(), TimeNotification.NOW));
        notifications = StreamSupport.stream(notificationRepository.findAll().spliterator(), false)
                .filter(not->not.getId().getLeft().getLeft().equals(user))
                .collect(Collectors.toList());

        List<NotificationDTO> minutes = notifications.stream()
                .filter(not-> not.getId().getRight().equals(TimeNotification.FIVEMINUTES))
                .filter(not-> ChronoUnit.MINUTES.between(LocalDateTime.now(), events.get(not.getId().getLeft().getRight()).getTime())<5)
                .map(not ->  new NotificationDTO(not.getId().getLeft().getRight(), events.get(not.getId().getLeft().getRight()).getName(), events.get(not.getId().getLeft().getRight()).getTime(), "In Five Minutes"))
                .collect(Collectors.toList());

        minutes.forEach(e->deleteNotifications(user, e.getId(), TimeNotification.FIVEMINUTES));
        notifications = StreamSupport.stream(notificationRepository.findAll().spliterator(), false)
                .filter(not->not.getId().getLeft().getLeft().equals(user))
                .collect(Collectors.toList());

        List<NotificationDTO> hour = notifications.stream()
                .filter(not-> not.getId().getRight().equals(TimeNotification.ONEHOUR))
                .filter(not-> ChronoUnit.MINUTES.between(LocalDateTime.now(), events.get(not.getId().getLeft().getRight()).getTime())<=60)
                .map(not ->  new NotificationDTO(not.getId().getLeft().getRight(), events.get(not.getId().getLeft().getRight()).getName(), events.get(not.getId().getLeft().getRight()).getTime(), "In One Hour"))
                .collect(Collectors.toList());

        hour.forEach(e->deleteNotifications(user, e.getId(), TimeNotification.ONEHOUR));
        notifications = StreamSupport.stream(notificationRepository.findAll().spliterator(), false)
                .filter(not->not.getId().getLeft().getLeft().equals(user))
                .collect(Collectors.toList());

        List<NotificationDTO> day = notifications.stream()
                .filter(not-> not.getId().getRight().equals(TimeNotification.TOMORROW))
                .filter(not-> ChronoUnit.DAYS.between(LocalDateTime.now(), events.get(not.getId().getLeft().getRight()).getTime())<=1)
                .map(not ->  new NotificationDTO(not.getId().getLeft().getRight(), events.get(not.getId().getLeft().getRight()).getName(), events.get(not.getId().getLeft().getRight()).getTime(), "Tomorrow"))
                .collect(Collectors.toList());

        day.forEach(e->deleteNotifications(user, e.getId(), TimeNotification.TOMORROW));

        List<NotificationDTO> rez = Stream.concat(now.stream(), minutes.stream()).collect(Collectors.toList());
        rez = Stream.concat(rez.stream(), hour.stream()).collect(Collectors.toList());
        rez = Stream.concat(rez.stream(), day.stream()).collect(Collectors.toList());
        return rez;
    }

    public boolean notificationsTurnOn(String user, Long event, LocalDateTime time){
        if(notificationRepository.findOne(new Tuple<>(new Tuple<>(user, event), TimeNotification.NOW))!=null || ChronoUnit.MINUTES.between(LocalDateTime.now(), time)==0)
            return true;
        return false;
    }

    private List<Observer<EventChangeEvent>> obs = new ArrayList<>();

    @Override
    public void addObserver(Observer<EventChangeEvent> e) {
        obs.add(e);
    }

    @Override
    public void removeObserver(Observer<EventChangeEvent> e) {
        obs.remove(e);
    }

    @Override
    public void notifyObservers(EventChangeEvent t) {
        obs.forEach(e->e.update(t));
    }
}

