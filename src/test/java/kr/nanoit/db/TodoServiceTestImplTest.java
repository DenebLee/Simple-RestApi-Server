package kr.nanoit.db;

import kr.nanoit.db.impl.todoservice.TodoService;
import kr.nanoit.object.entity.TodoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TodoService 테스트")
class TodoServiceTestImplTest {

    @Test
    @DisplayName("TodoServiceTest 구현체가 정상적으로 생성되는지 테스트")
    void should_created() {
        // given, when
        TodoService todoService = TodoService.createTest();

        // then
        assertThat(todoService).isNotNull();
    }

    @Test
    @DisplayName("TodoServiceTest TodoEntity가 정상적으로 저장되는지 확인")
    void should_saved() {
        // given
        TodoService service = TodoService.createTest();
        TodoEntity expected = new TodoEntity(0, OffsetDateTime.now().toString(), OffsetDateTime.now().toString(), "First Test Todo");

        // when
        TodoEntity actual = service.save(expected);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getTodoId()).isEqualTo(1);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("TodoServiceTest TodoEntity가 정상적으로 가져와 지는지 테스트")
    void should_get() {
        // given
        TodoService todoService = TodoService.createTest();
        TodoEntity testData = new TodoEntity(0, OffsetDateTime.now().toString(), OffsetDateTime.now().toString(), "First Test Todo");
        TodoEntity expected = todoService.save(testData);

        // when
        TodoEntity actual = todoService.findById(expected.getTodoId());

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getTodoId()).isEqualTo(1);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("TodoServiceTest TodoEntity가 정상적으로 삭제 되는지")
    void should_delete() {
        // given
        TodoService todoService = TodoService.createTest();
        TodoEntity testData = new TodoEntity(0, OffsetDateTime.now().toString(), OffsetDateTime.now().toString(), "First Test Todo");
        TodoEntity expected = todoService.save(testData);

        // when
        boolean actual = todoService.deleteById(expected.getTodoId());

        // then
        assertThat(actual).isTrue();
        assertThat(todoService.findById(expected.getTodoId())).isNull();
    }

    @Test
    @DisplayName("TodoServiceTest TodoEntity가 정상적으로 수정 되는지")
    void should_update() {
        // given
        TodoService todoService = TodoService.createTest();
        TodoEntity originalData = new TodoEntity(0, OffsetDateTime.now().toString(), OffsetDateTime.now().toString(), "First Test Todo");

        TodoEntity originalExpected = todoService.save(originalData); // testdata save
        TodoEntity updateExpected = new TodoEntity(originalExpected.getTodoId(), OffsetDateTime.now().plusDays(1).toString(), OffsetDateTime.now().plusDays(1).toString(), "Second Test Todo");

        // when
        TodoEntity actual = todoService.update(updateExpected);

        // then
        assertThat(actual).isNotNull(); // null이 아니여야 함
        assertThat(actual).usingRecursiveComparison().isEqualTo(updateExpected); // expected 의 값과 return 받은 값이 일치 하지 아니 해야함
        assertThat(actual).usingRecursiveComparison().isNotEqualTo(originalExpected); // expected 의 값과 return 받은 값이 일치 하지 아니 해야함
    }

    @Test
    @DisplayName("TodoServiceTest TodoEntity의 key값이 포함되어 있는지")
    void should_contain_key() {
        // given
        TodoService todoService = TodoService.createTest();
        TodoEntity testData = new TodoEntity(0, OffsetDateTime.now().toString(), OffsetDateTime.now().toString(), "First Test Todo");
        TodoEntity expected = todoService.save(testData);

        // when
        boolean actual = todoService.containsById(expected.getTodoId());

        // then
        assertThat(actual).isTrue();
    }
}