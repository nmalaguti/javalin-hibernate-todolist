package com.github.nmalaguti.todolist.repositories;

import com.github.nmalaguti.todolist.config.HibernateUtil;
import com.github.nmalaguti.todolist.models.Todo;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TodoRepository {
    private static final Logger logger = LoggerFactory.getLogger(TodoRepository.class);

    public void save(Todo todo) {
        executeTransaction(session -> session.persist(todo));
    }

    public List<Todo> getAllTodos() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Todo", Todo.class).list();
        } catch (Exception e) {
            logger.error("Failed to fetch todos", e);
            return List.of();
        }
    }

    public Todo getById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Todo.class, id);
        } catch (Exception e) {
            logger.error("Failed to fetch todo with ID: {}", id, e);
            return null;
        }
    }

    public void markAsCompleted(Long id) {
        executeTransaction(session -> {
            Todo todo = session.get(Todo.class, id);
            if (todo != null) {
                todo.setCompleted(true);
                session.merge(todo);
            }
        });
    }

    public void delete(Long id) {
        executeTransaction(session -> {
            Todo todo = session.get(Todo.class, id);
            if (todo != null) {
                session.remove(todo);
            }
        });
    }

    @FunctionalInterface
    private interface TransactionConsumer {
        void accept(Session session);
    }

    private void executeTransaction(TransactionConsumer action) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            action.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error("Transaction failed", e);
        }
    }
}
