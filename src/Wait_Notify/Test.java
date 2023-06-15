package Wait_Notify;

import java.util.Scanner;

/**
 * Определены у любого объекта в Джаве. С помощью них можем достичь паттерна продюсер-консюмер без доп.методов
 * <p>
 * wait() может вызываться в синхронизованном блоке.
 * Может Принимать в себя разные аргументы. Такие как время в мс сколько этот поток будет ждать,пока где-то будет
 * вызван notify().
 *
 *
 * что происходит:
 * * 1--отдаем внутренний лок(intrinsic lock) и как будто бы в этом потоке вышли из этого синхронизованного блока.
 * * Поэтому если на этом объекте синхронизируются другие потоки они могут забрать lock(монитор) этого объекта и продолжить
 * * свою работу.
 * * 2--мы ждем пока будет вызван notify() на этом объекте.



 * notify() пробуждает 1 поток который ждет. notifyAll() пробуждает все потоки.
 * Когда вызовем notify() и выйдем из синхронизованного блока,отдадим монитор этого объекта,поток который ждал
 * wait() в продюсе,продолжит свою работу.



 * Поток остановился на методе wait() и отдал монитор-второй поток который синхронизируется на этом же объекте-
 * начал свое выполнение-вышло сообщение о нажмите ключевую клавишу для возврата-через сканер задано-жмем кнопку-
 * и после этого у нас выполнился метод notify()-выходим из блока- пробуждается первый поток-берет монитор опять себе
 * и выводится сообщение что продюсер резюмд


 * Важно что бы эти 2 потока синхронизировались на одном и том же объекте.
 *
 * notify() в отличии от wait() не возвращает монитор.Код после вызова метода notify() выполняется и только потом
 * освобождается монитор.
 *
 * Не привязываются к объекту на котором идет синхронизация, а идет првязка к тому контексту где мы находимся.
 * ПО умолчанию ,если не указываем конкретный объект,то идет вызов на объекте this
 * В случае когда вызываем this() и notify() в synchronized block, идет вызов this.wait() , this.notify()
 * Если хотим вызвать метод на определенном объекте то указываем его сначала и потом на нем метод
 * Пример Object lock = new Object();
 * lock.wait();    lock.notify()
 *
 * Объект на котором синхронизируются методы ,должен быть константой final
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        WaitAndNotify wn = new WaitAndNotify();

        Thread thread1 = new Thread(() -> {
            try {
                wn.produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                wn.consume();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

    }
}

class WaitAndNotify {
    public void produce() throws InterruptedException {
        synchronized (this) {
            System.out.println("Producer thread started...");
            wait();// может вызываться только в пределах синхронизованного блока на том объекте какой в аргументах
            System.out.println("Producer thread resumed...");

        }

    }

    public void consume() throws InterruptedException {
        Thread.sleep(2000);// спим для того что бы поток продюсер точно был первым
        Scanner scanner = new Scanner(System.in);


        synchronized (this) {// синхронизируемся на том же объекте что и продюсер
            System.out.println("Waiting to return key pressed");
            scanner.nextLine();
            notify();// что бы на объекте this,все потоки которые ждут,проснулись.
            Thread.sleep(5000);

        }


    }

}
