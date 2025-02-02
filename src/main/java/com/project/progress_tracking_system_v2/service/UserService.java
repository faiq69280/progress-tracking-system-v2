package com.project.progress_tracking_system_v2.service;

import com.project.progress_tracking_system_v2.constants.ContactType;
import com.project.progress_tracking_system_v2.constants.ROLE;
import com.project.progress_tracking_system_v2.entity.ContactInformation;
import com.project.progress_tracking_system_v2.entity.Role;
import com.project.progress_tracking_system_v2.entity.User;
import com.project.progress_tracking_system_v2.repository.RoleRepository;
import com.project.progress_tracking_system_v2.repository.UserRepository;
import com.project.progress_tracking_system_v2.utility.Utils;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User save(User user) {
        Objects.requireNonNull(user);
        if (userRepository.findByName(user.getName()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(User updatedUser) {
        Objects.requireNonNull(updatedUser);
        User fetchedUser = userRepository.findByName(updatedUser.getName()).orElseThrow(() -> new IllegalArgumentException("User name doesn't exist"));
        fetchedUser.setName(Objects.requireNonNullElse(updatedUser.getName(), fetchedUser.getName()));
        if (!StringUtils.isEmpty(updatedUser.getPassword())) {
            fetchedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return userRepository.save(fetchedUser);
    }

    public ContactInformation addUserContactInformation(String userName, ContactInformation contactInformation) throws IllegalAccessException, NoSuchFieldException {
        Utils.requireFieldsNonNull(contactInformation, contactInformation.getClass().getField("uuid"));
        User fetchedUser = userRepository.findByName(userName).orElseThrow(() -> new IllegalArgumentException("username doesn't exist"));

        if (!StringUtils.isEmpty(contactInformation.getInformation())) {
            throw new IllegalArgumentException("Can't provide empty information, should include either of the following : %s".formatted(String.join(",",
                    Arrays.stream(ContactType.values()).map(Object::toString).toList())));
        }

        if (fetchedUser.getContactInformation() != null) {
            fetchedUser.getContactInformation().add(contactInformation);
        } else {
            fetchedUser.setContactInformation(Set.of(contactInformation));
        }

        return fetchAndSaveUserContactInformation(fetchedUser, contactInformation);
    }

    public ContactInformation updateSubscriptionToNotificationType(String userName, ContactInformation contactInformation) throws IllegalAccessException, NoSuchFieldException {
        Utils.requireFieldsNonNull(contactInformation, contactInformation.getClass().getField("uuid"));

        User fetchedUser = userRepository.findByName(userName)
                .orElseThrow(() -> new IllegalArgumentException("Couldn't find this username"));

        ContactInformation fetchedContactInformation = fetchedUser.getContactInformation().stream()
                .filter(contact -> contactInformation.getInformation().equals(contact.getInformation()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No contact information with this %s exists"
                        .formatted(contactInformation.getType().toString())));

        fetchedContactInformation.setNotify(contactInformation.notifiable());
        return fetchAndSaveUserContactInformation(fetchedUser, contactInformation);
    }

    public User updateUserRoles(String userName, Set<ROLE> roles) {
        if (ObjectUtils.isEmpty(roles)) {
            throw new IllegalArgumentException("Roles empty list provided");
        }
        User fetchedUser = userRepository.findByName(userName).orElseThrow(() -> new IllegalArgumentException("User name doesn't exist"));

        if (!ObjectUtils.isEmpty(fetchedUser.getRoles())) {
            fetchedUser.getRoles().clear();
        }
        Set<Role> rolesFetched = roleRepository.findByNameIn(roles);
        fetchedUser.setRoles(rolesFetched);
        return userRepository.save(fetchedUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User fetchedUser = userRepository
                .findByName(username).orElseThrow(() -> new UsernameNotFoundException("Couldn't find username"));
        return org.springframework.security.core.userdetails.User
                .withUsername(fetchedUser.getName())
                .password(fetchedUser.getPassword())
                .authorities(fetchedUser.getRoles().stream().map(Object::toString).toArray(String[]::new))
                .build();
    }

    public List<User> loadAllUsers() {
        return userRepository.findAll();
    }

    private ContactInformation fetchAndSaveUserContactInformation(User user, ContactInformation contactInformation) {
        return Objects.requireNonNull(userRepository.save(user))
                .getContactInformation()
                .stream()
                .filter(contact -> contact.getInformation().equals(contactInformation.getInformation()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Couldn't properly save the contact information"));
    }

}
