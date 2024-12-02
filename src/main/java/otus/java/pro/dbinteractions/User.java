package otus.java.pro.dbinteractions;

import lombok.*;
import otus.java.pro.dbinteractions.annotation.RepositoryField;
import otus.java.pro.dbinteractions.annotation.RepositoryIdField;
import otus.java.pro.dbinteractions.annotation.RepositoryTable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@RepositoryTable("users")
public class User {

    @RepositoryIdField("id")
    private Long id;

    @RepositoryField("login")
    private String login;

    @RepositoryField("password")
    private String password;

    @RepositoryField("nickname")
    private String nickname;
}
