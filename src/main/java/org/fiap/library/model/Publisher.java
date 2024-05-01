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
import java.util.HashSet;
import java.util.Set;

@Entity(name = "Publisher")
@Table(name = "Publishers", uniqueConstraints = {@UniqueConstraint(columnNames = {"Id"})})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region = "Publishers")
@NaturalIdCache(region = "PublisherId")
@NoArgsConstructor
public final class Publisher implements Serializable {

    @Id
    @Column(name = "Id", updatable = false)
    @NaturalId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private int id;

    @Column(name = "FantasyName")
    @Getter
    @Setter
    private String fantasyName;

    @Column(name = "Cnpj")
    @Getter
    @Setter
    private String cnpj;

    @Getter
    @Setter
    @ManyToMany(mappedBy = "publishers")
    private Set<Author> authors = new HashSet<>();

    @Getter
    @Setter
    @ManyToMany(mappedBy = "publishers")
    private Set<Book> books = new HashSet<>();

    public Publisher(String fantasyName, String cnpj, Set<Author> authors, Set<Book> books) {
        this.fantasyName = fantasyName;
        this.cnpj = cnpj;
        this.authors = authors;
        this.books = books;
    }
}
