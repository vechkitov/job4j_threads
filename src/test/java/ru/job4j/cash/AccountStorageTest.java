package ru.job4j.cash;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccountStorageTest {

    @Test
    void whenAdd() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertThat(firstAccount.amount()).isEqualTo(100);
    }

    @Test
    void whenUpdate() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        boolean result = storage.update(new Account(1, 200));
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        assertAll(
                () -> assertThat(firstAccount.amount()).isEqualTo(200),
                () -> assertTrue(result)
        );
    }

    @Test
    void whenDelete() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.delete(1);
        assertThat(storage.getById(1)).isEmpty();
    }

    @Test
    void whenTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 100));
        storage.transfer(1, 2, 100);
        var firstAccount = storage.getById(1)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
        var secondAccount = storage.getById(2)
                .orElseThrow(() -> new IllegalStateException("Not found account by id = 2"));
        assertThat(firstAccount.amount()).isEqualTo(0);
        assertThat(secondAccount.amount()).isEqualTo(200);
    }

    @Test
    void whenAccountFromIsNotExistsThenDoNotTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(2, 100));
        assertThrows(IllegalArgumentException.class, () -> storage.transfer(1, 2, 100));
        assertThat(storage.getById(2).get().amount()).isEqualTo(100);
    }

    @Test
    void whenAccountToIsNotExistsThenDoNotTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        assertThrows(IllegalArgumentException.class, () -> storage.transfer(1, 2, 100));
        assertThat(storage.getById(1).get().amount()).isEqualTo(100);
    }

    @Test
    void whenExistingAmountIsLessToRequiredAmountThenDoNotTransfer() {
        var storage = new AccountStorage();
        storage.add(new Account(1, 100));
        storage.add(new Account(2, 200));
        assertThrows(IllegalArgumentException.class, () -> storage.transfer(1, 2, 500));
        assertAll(
                () -> assertThat(storage.getById(1).get().amount()).isEqualTo(100),
                () -> assertThat(storage.getById(2).get().amount()).isEqualTo(200)
        );
    }
}
