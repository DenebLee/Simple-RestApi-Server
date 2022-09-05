//package kr.nanoit.data;
//
//
//import kr.nanoit.domain.User;
//import kr.nanoit.domain.UserRepository;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class InMemoryUserRepository implements UserRepository {
//    private static final Map USERS_STORE = new ConcurrentHashMap();
//
//    @Override
//    public String create(User user) {
//        String id = "test";
//        String password = "123123";
//        String email = "test@test.com";
//
//        user = User.builder()
//                .id(id)
//                .password(user.getPassword())
//                .email(user.getEmail())
//                .build();
//
//        USERS_STORE.put( user);
//
//        return id;
//    }
//}
//
