package com.javarush.task.task20.task2025;

import java.util.*;

/* 
Алгоритмы-числа
*/

public class Solution {

    public static long[] getNumbers(long N) {

        if (N <= 0) return new long[0];
        TreeSet<Long> res = new TreeSet<>(); // создаем контейнер для подходящих чисел
        long[][] table = getPowerTable();

        String stringNum = String.valueOf(N);
        int power = stringNum.length(); // выясняем степень в которую нужно возводить числа

        //Создаем генератор чисел________________________________________________________________________
        for (int i = 1; i <= power; i++) {

            int [] digits = new int[i]; //  создаем массив для генератора чисел размера (от 1 до разрядности N)
            int digit = 0; // определяем и инициализируем цифру, которая будет изменяться для получения комбинаций
            int len = digits.length; // создаем переменную длины массива
            if(len == 1) // обрабатываем частный случай, когда длина массива равна 1
            {
                for (long j = 1; j < 10; j++) {
                    res.add(j);
                }
            }
            else // обрабатываем остальные случаи, когда длина массива > 1
            {
                int pivot = 0; // создаем "итератор" для перемещения по массиву, устанавливаем его на начало

                boolean condition = true;  // digits[0] != 9 && digits[len-1] != 9
                while(condition)  // digits[0] != 9 && digits[len-1] != 9
                {
                    digit++; // увеличиваем цифру
                    if (digit == 10) // если цифра дошла до 10ти
                    {
                        if (pivot == 0) { digits[pivot] = 1; pivot++; digit = digits[pivot-1]; } // если pivot в начале
                        else // если это не первый элемент массива
                        {
                            int steps = 0; // заводим счетчик количества сдвигов итератора
                            while ((pivot != 0) && (digits[pivot] == 9)) // спускаем итератор в сторону начала массива,
                                                                // пока он не станет не равным 9 или не придет к началу
                            {
                                pivot--;
                                steps++;
                            }
                            if (pivot == 0 && digits[pivot] == 9)    // если дошли до первого элемента массива
                            {                                        // и он равен 9, то сбрасываем его до 1
                                digit = 1;                           //
                                for (int j = 0; j < steps+2; j++) {  // заполняем массив единицами количеством steps+2
                                    digits[pivot] = digit;
                                    pivot++;
                                }
                                pivot--; // делаем шаг назад после цикла
                                long tmp = isArmstrong(digits,table); // проверяем,
                                if (tmp != -1) res.add(tmp);         // заносим при совпадении
                                if ((digits[0] == 9) && (digits[len-1] == 9)){ condition = false;}
                                continue;                             // и выходим
                            }
                            digits[pivot]++; // увеличиваем на единицу значение  !!!! проверить на последней итерации
                            digit = digits[pivot]; // устанавливаем значение цифры по значению предыдущей
                            for (int j = 0; j < steps; j++) { // поднимаемся обратно используя значение счетчика
                                pivot++;                      //
                                digits[pivot] = digit;        // устанавливаем значение элемента массива
                            }
                            long tmp = isArmstrong(digits,table); // проверяем,
                            if (tmp != -1) res.add(tmp);         // заносим при совпадении
                            if ((digits[0] == 9) && (digits[len-1] == 9)){ condition = false;}
                            continue;                             // и выходим
                        }
                    }

                    digits[pivot] = digit; // присваиваем увеличенную цифру элементу массива с номером pivot("итератора")
                    long tmp = isArmstrong(digits,table);
                    if (tmp != -1) res.add(tmp);
                    if ((digits[0] == 9) && (digits[len-1] == 9)){ condition = false;}
                }
            }
        }

        long[] result = new long[res.size()];
        Iterator<Long> it = res.iterator();
        int i = 0;
        while (it.hasNext())
        {
            long value = it.next();
            if (value >= N) break;
            result[i] = value;
            i++;
        }

        return Arrays.copyOfRange(result,0,i);
    }

    public static void main(String[] args) {
        long a = System.currentTimeMillis();
        System.out.println(Arrays.toString(getNumbers(Long.MAX_VALUE))); // Long.MAX_VALUE
        long b = System.currentTimeMillis();
        System.out.println("memory " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (8 * 1024));
        System.out.println("time = " + (b - a) / 1000);

        a = System.currentTimeMillis();
        System.out.println(Arrays.toString(getNumbers(1000000)));
        b = System.currentTimeMillis();
        System.out.println("memory " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (8 * 1024));
        System.out.println("time = " + (b - a) / 1000);
    }

    private static long[][] getPowerTable() // функция создания таблицы степеней до 19й
    {
        long [][] table = new long[20][10];
        for (int i = 0; i < 10; i++) {
            table[1][i] = i;
        }
        for (int i = 2; i < 20; i++)
        {
           for (int j = 0; j < 10; j++)
           {
               table[i][j] = table[1][j]*table[i-1][j];
           }
        }
        return table;
    }


    private static long isArmstrong(int[] num, long [][] table)
    {
        long sum = 0; // создаем переменную для степенной суммы
        int len = num.length; // выясняем длину данного массива
        for (int i = 0; i < len; i++) {
            sum += table[len][num[i]]; // в цикле суммируем степени чисел используя таблицу степеней
        }
        if (sum < 0) sum = Long.MAX_VALUE;

        // Проверка на соответствие числу Армстронга___________________________________________________________________
        long CopySum = sum;
        String stringSum = String.valueOf(sum); // приводим получившееся число к строке
        int sumPower = stringSum.length();      // чтобы выяснить его длину
        long armstrSum = 0;
        for (int i = 0; i < sumPower; i++) {
            long temp = sum%10;
            armstrSum += table[sumPower][(int)temp]; // прибавляем к сумме значение из таблицы степеней числа;
            sum = sum/10; // уменьшаем число на разряд
            if (armstrSum > CopySum) return -1; // если сума больше проверяемого числа, то возвращаем -1
        }
        if (CopySum == armstrSum) return CopySum; // если усовие Армстронга выполняется - возвращаем число
        return -1; // если условие не выполняется, то возвращаем -1
    }
}