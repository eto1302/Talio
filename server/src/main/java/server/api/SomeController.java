package server;

import com.fasterxml.jackson.databind.util.ClassUtil;
import commons.User;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import server.database.UserRepository;

@Controller
@RequestMapping("/")
public class SomeController {
    CounterService counterService;
    UserRepository users;
    public SomeController(CounterService cs, UserRepository userRepository){
        this.counterService = cs;
        this.users = userRepository;
    }
    @GetMapping("/{name}")
    @ResponseBody
    public String index(@PathVariable String name) {
        if(!this.users.existsById(name)){
            User u = new User();
            u.name = name;
            this.users.save(u);
            return name + " added!";
        }
        return name + " exists already!";
    }
}