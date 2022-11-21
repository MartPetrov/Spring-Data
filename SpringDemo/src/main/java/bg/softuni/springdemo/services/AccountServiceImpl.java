package bg.softuni.springdemo.services;

import bg.softuni.springdemo.exceptions.EntityMissingException;
import bg.softuni.springdemo.models.Account;
import bg.softuni.springdemo.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void withdrawMoney(BigDecimal amount, Long id) {
        Account account =
                this.accountRepository.findById(id)
                        .orElseThrow(() -> new EntityMissingException("Account does not exist"));


        BigDecimal current = account.getBalance();

        if (amount.compareTo(current) > 0) {
            throw new RuntimeException("Cannot withdraw");
        }
        account.setBalance(current.subtract(amount));
        this.accountRepository.save(account);

    }

    @Override
    public void depositMoney(BigDecimal amount, Long id) {
        Account account = this.accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Sorry no account"));

        BigDecimal balance = account.getBalance().add(amount);
        account.setBalance(balance);
        this.accountRepository.save(account);
    }
}
