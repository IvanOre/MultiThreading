package Deadlock;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Deadlock взаимная блокировка.
 * <p>
 * В примере симулируем работу с банковскими счетами.
 * Код не оптимальный. Тк вложенность не приветствуется и увеличивается сложность кода.
 * <p>
 * public class Test {
 * public static void main(String[] args) throws InterruptedException {
 * Runner runner = new Runner();
 * <p>
 * Thread thread1 = new Thread(new Runnable() {
 *
 * @Override public void run() {
 * runner.firstThread();
 * <p>
 * }
 * });
 * Thread thread2 = new Thread(new Runnable() {
 * @Override public void run() {
 * runner.secondThread();
 * <p>
 * }
 * });
 * <p>
 * thread1.start();
 * thread2.start();
 * <p>
 * thread1.join();
 * thread2.join();
 * <p>
 * runner.finished();
 * <p>
 * }
 * }
 * <p>
 * class Runner {
 * private Account account1 = new Account();// баланс аккаунтов 10000
 * private Account account2 = new Account();
 * <p>
 * public void firstThread() {
 * Random random = new Random();
 * for (int i = 0; i < 10000; i++) {
 * synchronized (account1) {
 * synchronized (account2) {
 * // потоки могут безопасно осуществлять операции между акками без гонки
 * Account.transfer(account1, account2, random.nextInt(100));// переводим с 1 на 2 акк рандомную сумму от 0 до 99
 * }
 * }
 * }
 * }
 * <p>
 * public void secondThread() {
 * Random random = new Random();
 * for (int i = 0; i < 10000; i++) {
 * synchronized (account1) {
 * synchronized (account2) {
 * // потоки могут безопасно осуществлять операции между акками без гонки
 * Account.transfer(account2, account1, random.nextInt(100));// переводим с 2 на 1 акк рандомную сумму от 0 до 99
 * }
 * }
 * }
 * }
 * <p>
 * public void finished() {
 * System.out.println(account1.getBalance());
 * System.out.println(account2.getBalance());
 * System.out.println("Total balance is: " + (account1.getBalance() + account2.getBalance()));
 * <p>
 * <p>
 * }
 * }
 * <p>
 * class Account {// наш аккаунт банковский где счет-переводы между счетами-пополнение-снятие
 * private int balance = 10000;
 * <p>
 * public void deposit(int amount) {// пополнение баланса
 * balance += amount;
 * <p>
 * }
 * <p>
 * public void withdraw(int amount) {// снятие с баланса
 * balance -= amount;
 * }
 * <p>
 * public int getBalance() {// инфо о балансе счета
 * return balance;
 * }
 * <p>
 * // делаем static и метод оперирует не на объекте, а на классе Account
 * public static void transfer(Account acc1, Account acc2, int amount) {// перемещаем средства от одного счета к другому
 * acc1.withdraw(amount);
 * acc2.deposit(amount);
 * <p>
 * <p>
 * }
 * }
 * <p>
 * Теперь перепишем этот код с применением ReentrantLock
 * Поможет избежать вложенности.
 * private Lock lock1 = new ReentrantLock();// добавили lock1 для 1 акка и lock2 для 2 акка
 * private Lock lock2 = new ReentrantLock();
 * <p>
 * lock.unlock(); должен всегда вызываться в finally блоке. тк нам нужно в любом случае отпускать потоки без разницы что
 * произошло выше
 * <p>
 * public void firstThread() {
 * Random random = new Random();
 * for (int i = 0; i < 10000; i++) {
 * lock1.lock();
 * lock2.lock();
 * try {
 * Account.transfer(account1, account2, random.nextInt(100));// переводим с 1 на 2 акк рандомную сумму от 0 до 99
 * }finally {
 * lock1.unlock();
 * lock2.unlock();
 * }
 * }
 * }
 *
 *
 * В случае когда мы вызываем в разном порядке lock на наших объектах ReentrantLock,то у нас произойдет deadlock.
 * Все зависнет и ничего не выполнится.
 *
 *
 * Способы избежать deadlock
 *
 * 1. не забирать локи в разных порядках.
 *
 *
 * 2.создаем конструкцию по управлению локами и избежим тем самым дедлоков
 *
 * private void takeLocks(Lock lock1,Lock lock2){// создали отдельный метод для локов
 * boolean firstLockTaken = false;
 * boolean secondLockTaken = false;
 * while (true) {// крутимся пока не заберем 2 лока сразу
 * try {
 * firstLockTaken = lock1.tryLock();
 * secondLockTaken = lock2.tryLock();
 * } finally {
 * if (firstLockTaken && secondLockTaken) {// если 2 лока забрали успешно то дальше не идем.
 * return;
 * }
 * if (firstLockTaken) {// если забрали 1 лок, то отпускаем
 * lock1.unlock();
 * }
 * if (secondLockTaken) {// если забрали 2 лок, то отпускаем
 * lock2.unlock();
 * }
 *
 * }
 * try {
 * Thread.sleep(1);// спим 1 мс что бы другие потоки успели отдать локи
 * } catch (InterruptedException e) {
 * throw new RuntimeException(e);
 * }
 * }
 * }
 * Если не забираем  2 лока одновременно,то освобождаем тот лок который забрали. Тем самым не будет ситуаций,когда один
 * лок забрали, второй забрал другой поток и ждем пока освободим друг другу их. бесконечно
 *
 * Дедлоки так же случаются и при использовании  ключевого слова synchronized.
 * если в блоке 1 потока так же указать в 1 синхронизацию на 1акк,2акк.
 * А во 2 потоке указать 2акк,1акк. то будет Дедлок тоже. И в отличии от Reentrantlock мы с этим ничего поделать не можем.
 * Только указать правильный порядок забора локов.
 */

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Runner runner = new Runner();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                runner.firstThread();

            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                runner.secondThread();

            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        runner.finished();

    }
}

class Runner {
    private Account account1 = new Account();// баланс аккаунтов 10000
    private Account account2 = new Account();

    private Lock lock1 = new ReentrantLock();// добавили lock1 для 1 акка и lock2 для 2 акка
    private Lock lock2 = new ReentrantLock();

    private void takeLocks(Lock lock1, Lock lock2) {// создали отдельный метод для локов
        boolean firstLockTaken = false;
        boolean secondLockTaken = false;
        while (true) {// крутимся пока не заберем 2 лока сразу
            try {
                firstLockTaken = lock1.tryLock();
                secondLockTaken = lock2.tryLock();
            } finally {
                if (firstLockTaken && secondLockTaken) {// если 2 лока забрали успешно то дальше не идем.
                    return;
                }
                if (firstLockTaken) {// если забрали 1 лок, то отпускаем
                    lock1.unlock();
                }
                if (secondLockTaken) {// если забрали 2 лок, то отпускаем
                    lock2.unlock();
                }

            }
            try {
                Thread.sleep(1);// спим 1 мс что бы другие потоки успели отдать локи
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void firstThread() {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            takeLocks(lock1, lock2);
            try {
                Account.transfer(account1, account2, random.nextInt(100));// переводим с 1 на 2 акк рандомную сумму от 0 до 99
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void secondThread() {
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            takeLocks(lock2, lock1);
            try {
                Account.transfer(account2, account1, random.nextInt(100));// переводим с 2 на 1 акк рандомную сумму от 0 до 99
            } finally {
                lock1.unlock();
                lock2.unlock();
            }
        }
    }

    public void finished() {
        System.out.println(account1.getBalance());
        System.out.println(account2.getBalance());
        System.out.println("Total balance is: " + (account1.getBalance() + account2.getBalance()));


    }
}

class Account {// наш аккаунт банковский где счет-переводы между счетами-пополнение-снятие
    private int balance = 10000;

    public void deposit(int amount) {// пополнение баланса
        balance += amount;

    }

    public void withdraw(int amount) {// снятие с баланса
        balance -= amount;
    }

    public int getBalance() {// инфо о балансе счета
        return balance;
    }

    // делаем static и метод оперирует не на объекте, а на классе Account
    public static void transfer(Account acc1, Account acc2, int amount) {// перемещаем средства от одного счета к другому
        acc1.withdraw(amount);
        acc2.deposit(amount);


    }
}
