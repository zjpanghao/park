package demo.springboot.repository;
import demo.springboot.entity.ParkConf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkConfRepository  extends JpaRepository<ParkConf, Long> {
}
