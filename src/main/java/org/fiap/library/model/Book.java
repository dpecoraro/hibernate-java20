package org.fiap.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Books")
@Table(name = "Books", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "Books")
@NaturalIdCache(region = "BookId")
@NoArgsConstructor
public final class Book implements Serializable {
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "Book_Publisher",
            joinColumns = {@JoinColumn(name = "Book_Id")},
            inverseJoinColumns = {@JoinColumn(name = "Publisher_Id")}
    )
    Set<Publisher> publishers = new HashSet<>();
    @Id
    @Column(name = "Id", updatable = false)
    @NaturalId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int bookId;
    @Column(name = "Title")
    @Getter
    @Setter
    private String title;
    //O nullable por padrão é setado como true
    @Column(name = "PublishedAt", nullable = false)
    @Getter
    @Setter
    private Date publishedAt;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "Author_Id", referencedColumnName = "Id", nullable = false)
    private Author author;

    public Book(String title, Date publishedAt, Author author) {
        this.title = title;
        this.publishedAt = publishedAt;
        this.author = author;
    }
}
