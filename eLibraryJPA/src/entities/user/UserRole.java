package entities.user;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class UserRole {
    @EmbeddedId
    private Id id = new Id();

    public UserRole() {
    }

    public UserRole(String roleName) {
        id.role = Role.valueOf(roleName);
    }

    public Long getUserId() {
        return id.userId;
    }

    public void setUserId(Long id) {
        this.id.userId = id;
    }

    public Role getRole() {
        return id.role;
    }

    @Embeddable
    private static class Id implements Serializable {
        private static final long serialVersionUID = 1322120000551624359L;

        @Column(name = "USER_ID")
        private Long userId;

        @Enumerated(EnumType.STRING)
        @Column(name = "ROLE")
        private Role role;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Id id = (Id) o;

            if (userId != null ? !userId.equals(id.userId) : id.userId != null) return false;
            return role == id.role;
        }

        @Override
        public int hashCode() {
            int result = userId != null ? userId.hashCode() : 0;
            result = 31 * result + (role != null ? role.hashCode() : 0);
            return result;
        }
    }
}
