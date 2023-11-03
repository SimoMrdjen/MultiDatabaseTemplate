package two.databases.dbs.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import two.databases.dbs.db.ClientNames;
import two.databases.dbs.db.DBContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public List<UserDto> getAllUsers(Integer kvartal) {
        if ((kvartal == 4 || kvartal == 5)) {
            DBContextHolder.setCurrentDb(ClientNames.PGODINA);
        } else {
            DBContextHolder.setCurrentDb(ClientNames.APV);
        }


            return
                    repository.findAll()
                            .stream()
                            .map(mapper::mappUserToDto)
                            .collect(Collectors.toList());
        }


    public UserDto getUser(Integer id, Integer kvartal) {
//        if ((kvartal == 4 || kvartal == 5)) {
//            DBContextHolder.setCurrentDb(ClientNames.PGODINA);
//        } else {
//            DBContextHolder.setCurrentDb(ClientNames.APV);
//        }
        return mapper.mappUserToDto(
                repository.findById(id).orElseThrow());
    }

   // @Transactional
    public UserDto createUser(UserDto userDto, Integer kvartal) {
//                if ((kvartal == 4 || kvartal == 5)) {
//            DBContextHolder.setCurrentDb(ClientNames.PGODINA);
//        } else {
//            DBContextHolder.setCurrentDb(ClientNames.APV);
//        }
        var user = repository.save(mapper.mapDtoToUser(userDto));
        return  mapper.mappUserToDto(user);
    }
}
