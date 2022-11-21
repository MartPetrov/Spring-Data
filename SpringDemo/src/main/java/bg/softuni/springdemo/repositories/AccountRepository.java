package bg.softuni.springdemo.repositories;

import bg.softuni.springdemo.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

}
