package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            var newId = session.createQuery("from User as user where user.login = :userLogin", User.class)
                    .setParameter("userLogin", user.getLogin()).uniqueResult().getId();
            user.setId(newId);
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE User SET login = :uLogin, password = :uPassword WHERE id = :uId")
                    .setParameter("uLogin", user.getLogin())
                    .setParameter("uPassword", user.getPassword())
                    .setParameter("uId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE User WHERE id = :uId")
                    .setParameter("uId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        var session = sf.openSession();
        List<User> result = new ArrayList<>();
        try {
            result = session.createQuery("from User ORDER BY id", User.class).list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        return result;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        var session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            Query<User> query = session.createQuery(
                    "from User as user where user.id = :userId", User.class);
            query.setParameter("userId", userId);
            result = query.uniqueResultOptional();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        return result;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        var session = sf.openSession();
        List<User> result = new ArrayList<>();
        try {
             result = session.createQuery("from User WHERE login LIKE :searchKey", User.class)
                    .setParameter("searchKey", "%" + key + "%")
                    .list();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        return result;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        var session = sf.openSession();
        Optional<User> result = Optional.empty();
        try {
            Query<User> query = session.createQuery(
                    "from User as user where user.login = :userLogin", User.class);
            query.setParameter("userLogin", login);
            result = query.uniqueResultOptional();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
        return result;
    }
}