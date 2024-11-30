package otus.java.pro.DBInteractions;

import lombok.*;
import otus.java.pro.DBInteractions.annotation.RepositoryField;
import otus.java.pro.DBInteractions.annotation.RepositoryIdField;
import otus.java.pro.DBInteractions.annotation.RepositoryTable;

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
