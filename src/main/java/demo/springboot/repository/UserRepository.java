package demo.springboot.repository;

import demo.springboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    //User findByUserName(String userName);
    User findUserByUserName(String userName);
    @Query(value = "select count(*) from user where user_name = :userName and password = md5(concat(salt, :password))", nativeQuery = true)
    Long countByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);
}
