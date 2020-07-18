package nl.wilcomenge.timekeeper.cli.service;

import nl.wilcomenge.timekeeper.cli.model.UserProfile;
import nl.wilcomenge.timekeeper.cli.model.UserProfileRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserProfileService {

    @Resource
    UserProfileRepository userProfileRepository;

    public UserProfile getProfile() {
        if (userProfileRepository.count() == 0) {
            return new UserProfile();
        }

        return userProfileRepository.findAll().get(0);
    }

    public void save(UserProfile profile) {
        userProfileRepository.save(profile);
    }

}
