package com.java8.demo.model;

public class EqualsUser extends User {

    public EqualsUser(long id, String name, Role role) {
        super(id, name, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;

        return getId() == user.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
