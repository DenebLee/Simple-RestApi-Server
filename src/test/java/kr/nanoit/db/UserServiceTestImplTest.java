package kr.nanoit.db;

import kr.nanoit.object.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserService Test")
class UserServiceTestImplTest {

    @Test
    @DisplayName("Test if the UserServiceTest implementation is created successfully")
    void should_created() {
        // given, when
        UserService userService = UserService.createTest();

        // then
        assertThat(userService).isNotNull();
    }

    @Test
    @DisplayName("UserServiceTest UserEntity 정상적으로 저장되는지 확인")
    void should_saved() {
        // given
        UserService userService = UserService.createTest();
        UserEntity expected = new UserEntity(0, "lee", "123123", "test@test.com");

        // when
        UserEntity actual = userService.save(expected);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("UserServiceTest UserEntity 정상적으로 조회 되는지")
    void should_get() {
        // given
        UserService userService = UserService.createTest();
        UserEntity testData = new UserEntity(0, "lee", "123123", "test@test.com");
        UserEntity expected = userService.save(testData);

        // when
        UserEntity actual = userService.findById(expected.getId());

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(1);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("UserServiceTest UserEntity 정상적으로 삭제되는지")
    void should_delete(){
        // given
        UserService userService = UserService.createTest();
        UserEntity testData = new UserEntity(0,"lee","123123", "test@test.com");
        UserEntity expected = userService.save(testData);

        // when
        boolean actual = userService.deleteById(expected.getId());

        // then
        assertThat(actual).isTrue();
        assertThat(userService.findById(expected.getId())).isNull();
    }

    @Test
    @DisplayName("UserService UserEntity 정상적으로 수정 되는지")
    void should_update(){
        // given
        UserService userService = UserService.createTest();
        UserEntity originalUserData = new UserEntity(0,"lee", "123123", "test@test.com");
        UserEntity originalUserDataExpected = userService.save(originalUserData);
        UserEntity updateExpected = new UserEntity(originalUserDataExpected.getId(), "leejeongseob", "123123", "test@test.com");

        // when
        UserEntity actual = userService.update(updateExpected);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(updateExpected);
        assertThat(actual).isNotEqualTo(originalUserDataExpected);
    }
}
