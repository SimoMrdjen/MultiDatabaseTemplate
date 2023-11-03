package two.databases.dbs.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Integer sifraradnika;
    private Integer za_sif_sekret;
    private Integer sif_oblast;
    private Integer sifra_pp;
    private String ime;
    private String lozinka;
    private String email;
    private String password;
}
