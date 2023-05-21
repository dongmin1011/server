package capstone.server.controller;

import capstone.server.JWTToken.JwtTokenProvider;
import capstone.server.dto.UserDTO;
import capstone.server.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/user/save")
    public void save(@RequestBody UserDTO userDTO, HttpServletResponse response) {
//        System.out.println("MemberController.save");
//
//        System.out.println("param = " + userDTO.getLoginId());
//        System.out.println("param.getPassword() = " + userDTO.getPassword());
//        System.out.println("param.getName() = " + userDTO.getName());

        try{
            userService.save(userDTO);
            response.setStatus(200);
        }
        catch (Exception e){
            System.out.println("e = " + e);
            response.setStatus(400);
        }




    }
    @PostMapping("/user/login")
    @ResponseBody
    public UserDTO login(@RequestBody UserDTO userDTO, HttpServletResponse response){
        UserDTO loginResult = userService.login(userDTO);

        if(loginResult!=null){
            //성공
            loginResult.setToken(jwtTokenProvider.createToken(userDTO.getLoginId()));


            System.out.println("loginResult = " + loginResult);
            response.setStatus(200);

            return loginResult;

        }
        else{//실패
            response.setStatus(400);
            return null;
        }
    }
    @PostMapping("/user/update")
    @ResponseBody
    public UserDTO update(@RequestBody UserDTO userDTO) {
        return userService.update(userDTO);

    }

}
