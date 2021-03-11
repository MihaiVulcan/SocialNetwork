package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.paging.Page;
import socialnetwork.repository.paging.Pageable;
import socialnetwork.repository.paging.Paginator;
import socialnetwork.repository.paging.PagingRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements PagingRepository<ID,E> {

    private Validator<E> validator;
    protected Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            return entities.get(entity.getId());
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException("entity must be not null");
        if(entities.get(id) == null)
            return null;

        return entities.remove(id);
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        Paginator<E> paginator = new Paginator<E>(pageable, this.findAll());
        return paginator.paginate();
    }

//    @Override
//    public E update(E entity) {
//
//        if(entity == null)
//            throw new IllegalArgumentException("entity must be not null!");
//        validator.validate(entity);
//
//        entities.put(entity.getId(),entity);
//
//        if(entities.get(entity.getId()) != null) {
//            entities.put(entity.getId(),entity);
//            return null;
//        }
//        return entity;
//
//    }

}
