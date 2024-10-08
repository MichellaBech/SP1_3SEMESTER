package dat.persistence;

import java.util.List;
import java.util.Optional;

public interface IDAO<T> {

    List<T> getAll();

    void save(T t);

    void update(T entity);

    void delete(T entity);
}

