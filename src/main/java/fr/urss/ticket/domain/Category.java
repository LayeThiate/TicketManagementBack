package fr.urss.ticket.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.urss.annotations.Prohibited;
import fr.urss.annotations.Safe;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.annotations.NaturalId;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private long identifier;
    @NaturalId
    private String name;

    public Category(final String name) {
        this.name = name;
    }

    @Override
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Safe
    public static Category category(@NotNull final Session session, final String name) throws HibernateException {
        boolean nestedTransaction = session.getTransaction().isActive();
        try {
            if (!nestedTransaction) {
                session.getTransaction().begin();

                var category = session.bySimpleNaturalId(Category.class).load(name);
                if (category != null) return category;

                var createdCategory = new Category(name);
                session.save(createdCategory);
                //session.refresh(createdCategory);
                session.getTransaction().commit();
                return createdCategory;
            }
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw e;
        }
        //        finally {
        //            if (!nestedTransaction)
        //
        //        }
        return null;
    }

    @Prohibited
    public Category() {
    }

    public String get() {
        return name;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override /* perfect */ public boolean equals(Object object) {
        if (Objects.equals(this, object)) return true;
        if (object instanceof Category) {
            final Category category = (Category) object;
            return Objects.equals(this.name, category.name);
        }
        return false;
    }

    @Override /* perfect */ public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
