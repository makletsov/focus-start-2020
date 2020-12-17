package ru.makletsov.focusstart.server;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final Set<String> userNames = ConcurrentHashMap.newKeySet();

    void addUserName(String userName) {
        userNames.add(userName);
    }

    boolean removeUser(String userName) {
        return userNames.remove(userName);
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    boolean hasUser(String userName) {
        return getUserNames().contains(userName);
    }
}
