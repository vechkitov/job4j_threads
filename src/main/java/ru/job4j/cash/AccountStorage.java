package ru.job4j.cash;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Optional;

@ThreadSafe
public class AccountStorage {
    @GuardedBy("accounts")
    private final HashMap<Integer, Account> accounts = new HashMap<>();

    public boolean add(Account account) {
        synchronized (accounts) {
            return accounts.putIfAbsent(account.id(), account) == null;
        }
    }

    public boolean update(Account account) {
        synchronized (accounts) {
            return accounts.replace(account.id(), account) != null;
        }
    }

    public void delete(int id) {
        synchronized (accounts) {
            accounts.remove(id);
        }
    }

    public Optional<Account> getById(int id) {
        synchronized (accounts) {
            return Optional.ofNullable(accounts.get(id));
        }
    }

    public boolean transfer(int fromId, int toId, int amount) {
        synchronized (accounts) {
            Account accountFrom = getById(fromId).orElseThrow(
                    () -> new IllegalArgumentException("Счет с id=" + fromId + " не найден"));
            Account accoutnTo = getById(toId).orElseThrow(
                    () -> new IllegalArgumentException("Счет с id=" + toId + " не найден"));
            if (accountFrom.amount() < amount) {
                throw new IllegalArgumentException(String.format("На счете с id=%s недостаточно средств для списания. "
                        + "В наличии: %s. Требуется: %s%n", accountFrom.id(), accountFrom.amount(), amount));
            }
            accounts.replace(fromId, new Account(accountFrom.id(), accountFrom.amount() - amount));
            accounts.replace(toId, new Account(accoutnTo.id(), accoutnTo.amount() + amount));
            return true;
        }
    }
}
