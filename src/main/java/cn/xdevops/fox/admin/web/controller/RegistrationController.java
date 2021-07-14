package cn.xdevops.fox.admin.web.controller;

import cn.xdevops.fox.admin.application.service.UserService;
import cn.xdevops.fox.admin.web.dto.RegistrationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
@Slf4j
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public RegistrationDto registrationDto(){
        return new RegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid RegistrationDto registrationDto,
                                    BindingResult bindingResult) {
        if (userService.findUserByLoginName(registrationDto.getLoginName()).isPresent()) {
            bindingResult.reject("email", null, "Login name already exist.");
        }
        if (bindingResult.hasErrors()) {
            log.error("Regisration error: ", bindingResult.getAllErrors());
            return "registration";
        }
        userService.save(registrationDto);
        return "redirect:/registration?success";
    }
}
