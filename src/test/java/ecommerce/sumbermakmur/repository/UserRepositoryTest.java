package ecommerce.sumbermakmur.repository;

import ecommerce.sumbermakmur.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void UserRepository_SaveAll_ReturnUser() {

        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();

        User save = repository.save(user);

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getId()).isGreaterThan("");
    }

    @Test
    void UserRepository_GetAll_ReturnMoreOneThanUser() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        User user2 = User.builder()
                .email("test2@gmail.com")
                .password("test")
                .build();

        repository.save(user);
        repository.save(user2);

        List<User> all = repository.findAll();

        Assertions.assertThat(all).hasSize(2).isNotNull();
    }

    @Test
    void UserRepository_FindById_ReturnUser() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        repository.save(user);

        User getUser = repository.findById(user.getId()).orElseThrow();

        Assertions.assertThat(getUser).isNotNull();
    }

    @Test
    void UserRepository_FindByEmail_ReturnUser() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        repository.save(user);

        User getUser = repository.findByEmail(user.getEmail()).orElseThrow();

        Assertions.assertThat(getUser).isNotNull();
    }

    @Test
    void UserRepository_Update_ReturnUser() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        repository.save(user);

        User userSaved = repository.findById(user.getId()).orElseThrow();
        userSaved.setEmail("TEST@gmail.com");
        userSaved.setPassword("TEST");

        User updated = repository.save(userSaved);

        Assertions.assertThat(updated.getEmail()).isNotNull();
        Assertions.assertThat(updated.getPassword()).isNotNull();
    }

    @Test
    void UserRepository_Delete_ReturnUserIsEmpty() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test")
                .build();
        repository.save(user);

        repository.deleteById(user.getId());
        Optional<User> byId = repository.findById(user.getId());

        Assertions.assertThat(byId).isEmpty();
    }
}