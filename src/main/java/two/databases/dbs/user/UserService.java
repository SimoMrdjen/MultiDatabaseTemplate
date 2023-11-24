package two.databases.dbs.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import two.databases.dbs.db.ClientNames;
import two.databases.dbs.db.DBContextHolder;
import two.databases.dbs.db.DBSwitching;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService implements DBSwitching {

    private final UserRepository repository;
    private final UserMapper mapper;

    public List<UserDto> getAllUsers(Integer kvartal) {
        if ((kvartal == 4 || kvartal == 5)) {
            DBContextHolder.setCurrentDb(ClientNames.PGODINA);
        } else {
            DBContextHolder.setCurrentDb(ClientNames.APV);
        }


        List<UserDto> dtos = repository.findAll()
                .stream()
                .map(mapper::mappUserToDto)
                .collect(Collectors.toList());

        DBContextHolder.clear();

            return dtos;

        }


    public UserDto getUser(Integer id, Integer kvartal) {
        if ((kvartal == 4 || kvartal == 5)) {
            DBContextHolder.setCurrentDb(ClientNames.PGODINA);
        } else {
            DBContextHolder.setCurrentDb(ClientNames.APV);
        }
        return mapper.mappUserToDto(
                repository.findById(id).orElseThrow());
    }

   // @Transactional
    public UserDto createUser(UserDto userDto, Integer kvartal) {
                if ((kvartal == 4 || kvartal == 5)) {
            DBContextHolder.setCurrentDb(ClientNames.PGODINA);
        } else {
            DBContextHolder.setCurrentDb(ClientNames.APV);
        }
        var user = repository.save(mapper.mapDtoToUser(userDto));
        return  mapper.mappUserToDto(user);
    }

    public UserDto updateUser(Integer id, Integer kvartal) {
        this.switchDB(kvartal);
//        if ((kvartal == 4 || kvartal == 5)) {
//            DBContextHolder.setCurrentDb(ClientNames.PGODINA);
//        } else {
//            DBContextHolder.setCurrentDb(ClientNames.APV);
//        }
        var user = findUserById(id, kvartal);
        var userSaved = repository.save(user);
        return  mapper.mappUserToDto(userSaved);
    }

    public User findUserById(Integer id, Integer kvartal) {
//        if ((kvartal == 4 || kvartal == 5)) {
//            DBContextHolder.setCurrentDb(ClientNames.PGODINA);
//        } else {
//            DBContextHolder.setCurrentDb(ClientNames.APV);
//        }
        var user = repository.findById(id).get();
        user.setLozinka("IymenaDEF");
        user.setEmail("em123");
        user.setPassword("em123");

        return user;
    }
}
