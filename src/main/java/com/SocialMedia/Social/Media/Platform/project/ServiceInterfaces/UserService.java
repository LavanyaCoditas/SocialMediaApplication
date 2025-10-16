package com.SocialMedia.Social.Media.Platform.project.ServiceInterfaces;

import com.SocialMedia.Social.Media.Platform.project.DTO.LoginDTO;
import com.SocialMedia.Social.Media.Platform.project.DTO.UserListDto;
import com.SocialMedia.Social.Media.Platform.project.DTO.UserSignupDTO;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;

import java.util.List;

public interface UserService {
    public User signup(UserSignupDTO userDTO);
    public String login(LoginDTO loginDTO);
    public User makeModerator(Long userId);
    public User removeModerator(Long userId);
    public User getUserById(Long id, String currentUsername);
    public void deleteUser(Long id, String currentUsername);
    public List<UserListDto> getAllUsers(String currentUsername);
    public List<UserListDto> getAllModerators();

}
