package edu.vinu.springboot_websocket.controller;

import edu.vinu.springboot_websocket.model.User;
import edu.vinu.springboot_websocket.response.ApiResponse;
import edu.vinu.springboot_websocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@CrossOrigin
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerNewUser(@RequestBody User user){
        try {
            userService.registerUser(user);
            return ResponseEntity.status(200).body(new ApiResponse("User Registered Successfully", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("User Registration Failed", false));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUsers(){
        try {
            List<User> userList=userService.getAllUsers();
            if (!userList.isEmpty()){
                return ResponseEntity.status(200).body(new ApiResponse("Users Found", userList));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse("No Users Found", userList));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error Occurred", null));
        }
    }

    @GetMapping("/all/except/{username}")
    public ResponseEntity<ApiResponse> getAllOtherUsers(@PathVariable String username){
        try {
            List<User> userList=userService.getAllOtherUsers(username);
            if (!userList.isEmpty()){
                return ResponseEntity.status(200).body(new ApiResponse("Users Found", userList));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse("No Users Found", userList));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error Occurred", null));
        }
    }

    @GetMapping("/by-username/{userName}")
    public ResponseEntity<ApiResponse> getUserByUsername(@PathVariable String userName){
        try {
            User user=userService.getUserByUsername(userName);
            if (user!=null){
                return ResponseEntity.status(200).body(new ApiResponse("User Found", user));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse("User Not Found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error Occurred", null));
        }
    }

    @PostMapping("/user/join")
    public ResponseEntity<ApiResponse> join(@RequestBody String username){
        try {
            userService.userJoin(username);
            return ResponseEntity.status(200).body(new ApiResponse("User Joined Successfully", true));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("User Join Failed", false));
        }
    }
}
