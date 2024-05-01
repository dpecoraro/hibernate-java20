package org.fiap.library.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Table(name = "Authors", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@Entity(name = "Author")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "Authors")
@NoArgsConstructor
public class Author implements Serializable {
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "PublishedBooks")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
    Set<Book> books;
    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "Author_Publisher",
            joinColumns = {@JoinColumn(name = "Author_Id")},
            inverseJoinColumns = {@JoinColumn(name = "Publisher_Id")}
    )
    Set<Publisher> publishers = new HashSet<>();
    @Id
    @Column(name = "Id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;
    @Column(name = "Name")
    @Getter
    @Setter
    private String name;
    //O nullable por padrão é setado como true
    @Column(name = "InsertDate", nullable = false)
    @Getter
    @Setter
    private Date insertDate;

    public Author(String name, Date insertDate, Set<Book> books, Set<Publisher> publishers) {
        this.name = name;
        this.insertDate = insertDate;
        this.books = books;
        this.publishers = publishers;
    }

    @Override
    public String toString() {
        return String.format("id: %d \n Name: %s", id, name.toUpperCase());
    }
}
