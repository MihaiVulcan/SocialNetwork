package socialnetwork.repository.database;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.memory.InMemoryRepository;

public class AbstractDatabaseRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private String url;
    private String name;
    private String password;

    public AbstractDatabaseRepository(String url, String name, String password, Validator<E> validator) {
        super(validator);
        this.url = url;
        this.name = name;
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
