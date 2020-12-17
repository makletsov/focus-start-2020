package ru.makletsov.focusstart.s.server;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
    private final Set<String> userNames = ConcurrentHashMap.newKeySet();
    private final Set<UserHandler> userHandlers = ConcurrentHashMap.newKeySet();

    void addUserName(String userName) {
        userNames.add(userName);
    }

    void removeUser(String userName, UserHandler aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userHandlers.remove(aUser);
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
