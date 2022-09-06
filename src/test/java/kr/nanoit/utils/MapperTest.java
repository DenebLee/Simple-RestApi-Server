package kr.nanoit.utils;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.nanoit.object.dto.UserDto;
import org.junit.jupiter.api.Test;

class MapperTest {

    @Test
    void should_write() throws JsonProcessingException {
        // given
        UserDto userDto = new UserDto(1, "test", "pwd", "testUser@test.com");

        // when
        String actual = Mapper.write(userDto);

        // then
        assertThat(actual).isEqualTo("{\"id\":1,\"username\":\"test\",\"password\":\"pwd\",\"email\":\"testUser@test.com\"}");
    }

    @Test
    void shoud_read() throws JsonProcessingException {
        // given
        UserDto expectedDto = new UserDto(1, "test", "pwd", "testUser@test.com");
        String expectedRaw = Mapper.write(expectedDto);

        // when
        UserDto actual = Mapper.read(expectedRaw, UserDto.class);

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedDto);
    }
}