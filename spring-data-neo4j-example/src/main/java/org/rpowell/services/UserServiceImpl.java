package org.rpowell.services;

import org.rpowell.domain.User;
import org.rpowell.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User getUserByEmail(String email) {
        Assert.notNull(email, "The email must not be null");

        return userRepository.findByEmail(email);
    }

    @Override
    public void deleteUser(String email) {
        Assert.notNull(email, "The email must not be null");

        User user = userRepository.findByEmail(email);

        if (user == null) {
            log.info("No user found with the email " + email);
        } else {
            userRepository.delete(user);
            log.info("Deleted user: " + user.toString());
        }
    }

    @Override
    public void updateUser(String email, User updatedUser) {
        Assert.notNull(email, "The email name must not be null");
        Assert.notNull(updatedUser, "The updated user must not be null");

        User originalUser = userRepository.findByEmail(email);

        if (originalUser == null) {
            log.info("No user found with the email " + email);
        } else {
            updatedUser.setId(originalUser.getId());
            userRepository.save(updatedUser);
            log.info("User: " + originalUser.toString() + " updated to " + updatedUser.toString());
        }
    }

    @Override
    public void createUser(String firstName, String lastName, String email) {
        Assert.notNull(firstName, "The first name must not be null");
        Assert.notNull(lastName, "The last name must not be null");
        Assert.notNull(email, "The email name must not be null");

        User user = userRepository.findByEmail(email);

        if (user != null) {
            log.info("User already exists: " + user.toString());
        } else {
            user = new User(firstName, lastName, email);

            userRepository.save(user);
            log.info("User created: " + user.toString());
        }
    }

    public void addFriendship(String email1, String email2) {
        Assert.notNull(email1, "The first email must not be null");
        Assert.notNull(email2, "The second email must not be null");

        User user1 = userRepository.findByEmail(email1);
        User user2 = userRepository.findByEmail(email2);

        if (user1 == null) {
            log.info("No user found with the email " + email1);
        } else if (user2 == null) {
            log.info("No user found with the email " + email2);
        } else {
            user1.addFriend(user2);
            user2.addFriend(user1);

            userRepository.save(user1); // This will save both friends
            log.info("Added friendship between " + user1 + " and " + user2);
        }
    }
}
