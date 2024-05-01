package org.fiap.library.utils;

import org.fiap.library.model.Author;
import org.fiap.library.model.Book;
import org.fiap.library.model.Publisher;
import org.fiap.library.utils.audit.AuthorObserver;
import org.fiap.library.utils.audit.BookInterceptor;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.Objects;

public class HibernateDataUtils {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                    .configure("hibernate.cfg.xml")
                    .build();
            try {
                MetadataSources sources = new MetadataSources(standardRegistry);
                sources.addAnnotatedClasses(
                        Author.class,
                        Publisher.class,
                        Book.class);
                MetadataBuilder metadataBuilder = sources.getMetadataBuilder();
                metadataBuilder.applyImplicitNamingStrategy(
                        ImplicitNamingStrategyJpaCompliantImpl.INSTANCE);
                Metadata metadata = metadataBuilder.build();
                SessionFactoryBuilder sessionFactoryBuilder = metadata.getSessionFactoryBuilder();

                // Supply a SessionFactory-level Interceptor
                sessionFactoryBuilder.applyInterceptor(new BookInterceptor());
                sessionFactoryBuilder.addSessionFactoryObservers(new AuthorObserver());
                sessionFactory = sessionFactoryBuilder.build();
                /*
                OLD WAY
                Configuration configuration = new Configuration()
                        .configure("hibernate.cfg.xml");
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/librarydb?useSSL=false");
                settings.put(Environment.USER, "admin");
                settings.put(Environment.PASS, "root");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "Update");
                configuration.setProperties(settings);

                configuration.addAnnotatedClass(Author.class)
                        .addAnnotatedClass(Publisher.class)
                        .addAnnotatedClass(Book.class);
                        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                 */
            } catch (Exception e) {
                e.printStackTrace();
                if (Objects.nonNull(sessionFactory))
                    sessionFactory.close();
            }
        }
        return sessionFactory;
    }
}
