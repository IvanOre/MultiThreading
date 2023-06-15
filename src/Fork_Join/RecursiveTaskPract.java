package Fork_Join;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 *ForkJoinPool разделят работу по процессорам.
 * System.out.println(Runtime.getRuntime().availableProcessors()); посмотреть сколько на пк их. 12 у меня
 *
 * Не очень понятный пример. от другого автора
 *
 */
public class RecursiveTaskPract {
    public static void main(String[] args) {

        int[] list = new int[10000];// массив из рандомных 10000 чисел. ищем самое большое значение в нем
        int max = 0;
        for (int i = 0; i < 10000; i++) {
            list[i] = (int) (Math.random() * 10000);
            max = Math.max(max, list[i]);
        }
        System.out.println("Max is: " + max);
        System.out.println("Max is using ForkJoin is: " + findMax(list));

    }

    private static int findMax(int[] list) {// ищем максимальный элемент по значению
        RecursiveTask<Integer> recursiveTask = new FindingMax(0,list.length,list);// начнем с 0 и до последнего элемента в list.

        ForkJoinPool forkJoinPool = new ForkJoinPool();
       return forkJoinPool.invoke(recursiveTask);// передали потокам нашу задачу/ invoke ошидает завершения исполнения
    }
}

class FindingMax extends RecursiveTask<Integer>{// разделяет рекурсию на процессоры и передает fork/join,аон уже дает работу потокам
    int l;
    int r;
    int [] list;

    public FindingMax(int l, int r, int[] list) {
        this.l = l;
        this.r = r;
        this.list = list;
    }

    @Override
    protected Integer compute() {// делаем все расчеты в этом методе compute
        if (r - l < 1000){// если число уже дошло до 1000 между r и l
            int curr_max = 0;
            for (int i = l; i < r; i++) // то значение равно левому числу и оно меньше чем правое,конкатенируем
                curr_max = Math.max(curr_max,list[i]);
            return curr_max;
        }else {// иначе у нас больше чем 1000 элементов
            int mid = (r + l) / 2;// рекурсивно разделили
            RecursiveTask<Integer> left = new FindingMax(l,mid,list);//  с начала до мида в массиве
            RecursiveTask<Integer> right = new FindingMax(mid,r,list);// с мида до конца в массиве.

            left.fork();// начинает ассинхронное выполнение задачи.
            right.fork();

            return Math.max(left.join(),right.join());

        }


    }
}
