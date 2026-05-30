package cz.vse.java.funwithjpa.model;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Entity representing a book loan record.
 * Tracks the borrower, loan date, and an optional return date.
 */
@Entity
@Table(name = "LOAN")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @Column(name = "BORROWER_NAME", nullable = false)
    private String borrowerName;

    @Column(name = "LOAN_DATE", nullable = false)
    private LocalDate loanDate;

    @Column(name = "RETURN_DATE")
    private LocalDate returnDate;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
}
