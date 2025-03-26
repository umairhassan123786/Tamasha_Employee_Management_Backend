package com.example.demo.Repository;
import com.example.demo.Enum.Role;
import com.example.demo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByemail(String email);

    @Query("SELECT COALESCE(MAX(u.user_Id), 0) FROM User u")
    Long findMaxUserId();
//    @Query("SELECT u FROM User u WHERE u.team = :team AND u.role IN :roles")
//    List<User> findByTeamAndRoleIn(@Param("team") Integer team, @Param("roles") List<String> roles);
@Query("SELECT u FROM User u WHERE u.team = :teamId AND u.role IN :roles")
List<User> findByTeamAndRoleIn(@Param("teamId") Long teamId, @Param("roles") List<Role> roles);

    @Query("SELECT u FROM User u WHERE u.team = :team AND u.role IN :roles")
    List<User> findByTeamAndRoleIn(@Param("team") Integer team, @Param("roles") List<String> roles);
    List<User> findByRoleIn(List<String> roles);

    List<User> findByRole(Role role);  // ✅ Correct!
    List<User> findByRoleInAndTeam(List<Role> roles, Integer team);


}