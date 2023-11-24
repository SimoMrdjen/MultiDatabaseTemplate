package two.databases.dbs.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:3000")

public class UserController {

    private final UserService userService;

    @GetMapping(value = "/{kvartal}")
    public ResponseEntity<List<UserDto>> getAllUsers(@PathVariable(name = "kvartal") Integer kvartal) {
        return ResponseEntity.ok(userService
                .getAllUsers(kvartal));
    }

    @GetMapping(value = "one/{id}/{kvartal}")
    public ResponseEntity<UserDto> getUser(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "kvartal") Integer kvartal
    ) {
        return ResponseEntity.ok(userService
                .getUser(id, kvartal));
    }

    @PutMapping(value = "/{id}/{kvartal}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Integer id,
            @PathVariable(name = "kvartal") Integer kvartal
    ) {
        return ResponseEntity.ok(userService
                .updateUser(id, kvartal));
    }

    @PostMapping(value = "/{kvartal}")
    public ResponseEntity<UserDto> addUser( @RequestBody UserDto userDto,
                                            @PathVariable(name = "kvartal") Integer kvartal) throws Exception {

        return ResponseEntity.ok(userService
                .createUser(userDto, kvartal));
    }


}
