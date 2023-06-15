package ThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * Пул потоков. Создаем сразу несколько штук.
 *
 * ExecutorService executorService = Executors.newFixedThreadPool(2); создаем пул потоков через ексекуторс
 *
 * Грубо говоря в нашем примере мы создали 2 работника которые будут реализовать метод из класса Work
 * ExrcutorService сервис по выполнению. В аргументы передаем кол-во потоков которые хотим юзать
 * submit() выдает задания(кол-во задали в цикле for) а shutdown() не выключает как может показаться, а
 * говорит о том,что больше не принимаем задания, и начинаем выполнение текущих 5 заданных в for
 *
 * executorService.awaitTermination(1, TimeUnit.DAYS);   ожидание завершения
 * Указываем сколько хотим ждать пока наши потоки выполнят все задания. Например 1 day. ждем 1 day и если потоки
 * выполнят наши задачи раньше,то хорошо,если нет то мейн просыпается, идем дальше и завершаем программу.Если нет
 * других инструкций.
 * Что то вроде join(). тк поток останавливается и не идет дальше.
 *
 *
 * Метафора. 2 работника. Дали задание перенести 5 коробок. Они идут и берут по одной и начинают носить.Какой-то
 * работник быстрее носит,какой-то медленнее.
 * Дали им на это 1 день. Выполнят раньше то хорошо. Нет -то просыпается главный и завершает их работу
 * или дает новую,если были прописаны инструкции.
 *
 * ThreadPool удобен когда нужно выполнить задание много раз. Создаем потоки и выполняем. Причем потоки не будут
 * хвататься за один и тот же объект или переменную задачи. Берут разные и выполняют постепенно.
 *
 *
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);// создаем пул потоков через ексекуторс

        for (int i = 0; i < 5; i++)// в метод submit мы должны поставить объект класса реализующий Runnable
            executorService.submit(new Work(i));// дали 5 заданий с id от 0 до 4

        executorService.shutdown();// shutdown(),не принимаем новые задания, а беремся за выполнение текущих
        System.out.println("All tasks submitted");// сообщение о начале выполнения задач для наглядности

        executorService.awaitTermination(1, TimeUnit.DAYS);
    }
}
class Work implements Runnable{
    private int id;//  у каждой работы будет свой id
    public Work(int id){
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Work " + id + " was completed");
    }
}
