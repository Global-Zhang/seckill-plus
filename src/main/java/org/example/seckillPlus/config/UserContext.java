package org.example.seckillPlus.config;

import org.example.seckillPlus.pojo.User;

/**
 * @author zhoubin
 * @since 1.0.0
 */
public class UserContext {
    private static ThreadLocal<User> userHolder = new ThreadLocal<>();
    public static void setUser(User user) {
        userHolder.set(user);
    }
    public static User getUser() {
        return userHolder.get();
    }
}
