package cz.vse.java.funwithjpa.controller;

import cz.vse.java.funwithjpa.gui.TableRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.List;

import cz.vse.java.funwithjpa.model.Author;
import cz.vse.java.funwithjpa.model.Book;
import cz.vse.java.funwithjpa.model.Loan;

/**
 * Main application controller handling business logic and database interactions.
 * It manages EntityManagers and provides transaction management for CRUD operations.
 */
public class Controller {
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

    private EntityManagerFactory emf;

    public Controller(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    /**
     * Executes the given action within a JPA transaction.
     * Handles transaction lifecycle and optimistic locking exceptions.
     *
     * @param action The function to execute inside the transaction.
     * @param <T>    The return type of the function.
     * @return The result of the function.
     * @throws DatabaseException If a database error or optimistic locking failure occurs.
     */
    public <T> T executeInTransaction(Function<EntityManager, T> action) throws DatabaseException {
        if (emf == null) {
            throw new DatabaseException("Database connection is not available.", null);
        }

        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            T result = action.apply(em);

            tx.commit();
            return result;
        } catch (OptimisticLockException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            LOG.error("Optimistic locking failure: Data was modified by another user.", e);
            throw new DatabaseException("Data was modified by another user. Please reload and try again.", e);
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            LOG.error("Database error occurred.", e);
            throw new DatabaseException("Database error: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void executeInTransaction(Consumer<EntityManager> action) throws DatabaseException {
        executeInTransaction(em -> {
            action.accept(em);
            return null;
        });
    }

    public <T> T executeQuery(Function<EntityManager, T> query) throws DatabaseException {
        if (emf == null) {
            throw new DatabaseException("Database connection is not available.", null);
        }

        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            return query.apply(em);
        } catch (Exception e) {
            LOG.error("Database error occurred during query.", e);
            throw new DatabaseException("Database error: " + e.getMessage(), e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Author> getAllAuthors() throws DatabaseException {
        return executeQuery(em -> em.createQuery("SELECT a FROM Author a", Author.class).getResultList());
    }

    public void createAuthor(Author author) throws DatabaseException {
        executeInTransaction((Consumer<EntityManager>) em -> em.persist(author));
    }

    public void updateAuthor(Author author) throws DatabaseException {
        executeInTransaction((Consumer<EntityManager>) em -> em.merge(author));
    }

    public void deleteAuthor(Author author) throws DatabaseException {
        executeInTransaction((Consumer<EntityManager>) em -> {
            Author managedAuthor = em.find(Author.class, author.getId());
            if (managedAuthor != null) {
                em.remove(managedAuthor);
            }
        });
    }

    public List<Book> getAllBooks() throws DatabaseException {
        return executeQuery(em -> em.createQuery("SELECT b FROM Book b", Book.class).getResultList());
    }

    public void createBook(Book book) throws DatabaseException {
        executeInTransaction((Consumer<EntityManager>) em -> em.persist(book));
    }

    public void updateBook(Book book) throws DatabaseException {
        executeInTransaction((Consumer<EntityManager>) em -> em.merge(book));
    }

    public void deleteBook(Book book) throws DatabaseException {
        executeInTransaction((Consumer<EntityManager>) em -> {
            Book managedBook = em.find(Book.class, book.getId());
            if (managedBook != null) {
                em.remove(managedBook);
            }
        });
    }

    public List<Loan> getAllLoans() throws DatabaseException {
        return executeQuery(em -> em.createQuery("SELECT l FROM Loan l", Loan.class).getResultList());
    }

    public void createLoan(Loan loan) throws DatabaseException {
        executeInTransaction((Consumer<EntityManager>) em -> em.persist(loan));
    }

    public void updateLoan(Loan loan) throws DatabaseException {
        executeInTransaction((Consumer<EntityManager>) em -> em.merge(loan));
    }

    public void deleteLoan(Loan loan) throws DatabaseException {
        executeInTransaction((Consumer<EntityManager>) em -> {
            Loan managedLoan = em.find(Loan.class, loan.getId());
            if (managedLoan != null) {
                em.remove(managedLoan);
            }
        });
    }
}
