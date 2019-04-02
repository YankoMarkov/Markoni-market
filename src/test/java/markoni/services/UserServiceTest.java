package markoni.services;

import markoni.domain.models.services.UserServiceModel;
import markoni.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    private UserService userService;
    private ModelMapper modelMapper;
    private UserServiceModel testUser;

    @Before
    public void init() {
        this.modelMapper = new ModelMapper();
        this.userService = new UserServiceImpl(this.userRepository, this.modelMapper);
        this.testUser = new UserServiceModel();
        this.testUser.setUsername("gosho");
        this.testUser.setPassword("1234");
        this.testUser.setAddress("Plovdiv");
        this.testUser.setEmail("gosho@test.test");
    }

    @Test
    public void saveUser_saveUserWithCorrectValues_returnCorrect() {
        UserServiceModel actual = this.userService.saveUser(this.testUser);
        UserServiceModel expected = this.modelMapper.map(this.userRepository.findAll().get(0), UserServiceModel.class);

        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
        Assert.assertEquals(expected.getAddress(), actual.getAddress());
        Assert.assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test(expected = Exception.class)
    public void saveUser_saveUserWithNullValues_returnException() {
        this.testUser.setUsername(null);
        this.testUser.setPassword(null);
        this.testUser.setAddress(null);
        this.testUser.setEmail(null);

        this.userService.saveUser(this.testUser);
    }
}
