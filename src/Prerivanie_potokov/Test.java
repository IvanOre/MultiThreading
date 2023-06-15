package Prerivanie_potokov;

import java.util.Random;

/**
 *Прерывание потока.
 * Из одного потока хотим вызвать прерывание другого потока.
 * thread.interrupt(); Но он по сути не прерывает выполнение в таком виде.
 *
 * Так задаем прерывание правильно Thread.currentThread().isInterrupted():
 * Thread thread = new Thread(new Runnable() {
 *             @Override
 *             public void run() {
 *                 Random random = new Random();
 *                 for(int i = 0; i < 1000_000_000;i++){// миллиард раз вычисляем синус рандомного числа
 *                    if (Thread.currentThread().isInterrupted()){// поток.текущийПоток.прерываем
 *                        System.out.println("Thread was interrupted");
 *                         break;
 *                    }
 *                     Math.sin(random.nextDouble());
 *                 }
 *             }
 *         });
 *
 *         Можно через try catch block который выбрасываем например через Thread.sleep()
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                for(int i = 0; i < 1000_000_000;i++){// миллиард раз вычисляем синус рандомного числа
                   if (Thread.currentThread().isInterrupted()){// поток.текущийПоток.прерываем
                       System.out.println("Thread was interrupted");
                        break;
                   }
                    Math.sin(random.nextDouble());
                }
            }
        });

        System.out.println("Starting thread");

        thread.start();

        Thread.sleep(1000);//спим ждем секунду после начала выполнения работы потоком и прерываем работу
        thread.interrupt();// вызывается до join(). иначе не прервется тк ждем сначала завершения работы в join

        thread.join();

        System.out.println("Thread has finished");

    }
}
