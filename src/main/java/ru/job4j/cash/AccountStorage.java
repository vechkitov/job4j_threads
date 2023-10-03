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
            boolean hasValue = accounts.containsKey(account.id());
            if (hasValue) {
                accounts.replace(account.id(), account);
            }
            return hasValue;
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
            if (!isPossibleToTransfer(fromId, toId, amount)) {
                return false;
            }
            accounts.replace(fromId, new Account(fromId, getById(fromId).get().amount() - amount));
            accounts.replace(toId, new Account(toId, getById(toId).get().amount() + amount));
            return true;
        }
    }

    private boolean isPossibleToTransfer(int fromId, int toId, int amount) {
        boolean isPossible = true;
        Optional<Account> accountFromOpt = getById(fromId);
        if (accountFromOpt.isEmpty()) {
            System.out.printf("Счета с id=%s не существует%n", fromId);
            isPossible = false;
        }
        if (isPossible) {
            int amountFrom = accountFromOpt.get().amount();
            if (amountFrom < amount) {
                System.out.printf("На счете с id=%s недостаточно средств для списания. "
                        + "В наличии: %s. Требуется: %s%n", fromId, amountFrom, amount);
                isPossible = false;
            }
        }
        if (getById(toId).isEmpty()) {
            System.out.printf("Счета с id=%s не существует%n", toId);
            isPossible = false;
        }
        return isPossible;
    }
}
