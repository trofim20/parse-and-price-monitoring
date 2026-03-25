package org.example.util;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class HumanBehaviorUtils {

    private static final Random RANDOM = new Random();

    /**
     * Случайная пауза в диапазоне [minMs, maxMs].
     * Имитирует время чтения/реакции реального пользователя.
     */
    public static void randomSleep(long minMs, long maxMs) {
        try {
            long delay = ThreadLocalRandom.current().nextLong(minMs, maxMs);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    /** Средняя пауза (1.5–4 с) — между страницами */
    public static void mediumPause() {
        randomSleep(1500, 4000);
    }

    /** Длинная пауза (5–12 с) — иногда "читаем" страницу */
    public static void longPause() {
        randomSleep(5000, 12000);
    }

    /**
     * Человекоподобный скролл: несколько шагов вниз с паузами,
     * иногда чуть прокручиваем обратно вверх.
     */
    public static void humanScroll(WebDriver driver) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        int steps = 3 + RANDOM.nextInt(5); // 3–7 шагов

        for (int i = 0; i < steps; i++) {
            long scrollAmount = 150 + RANDOM.nextInt(350);

            if (i > 0 && RANDOM.nextInt(10) < 2) {
                scrollAmount = -(50 + RANDOM.nextInt(150));
            }

            js.executeScript("window.scrollBy({top: " + scrollAmount + ", behavior: 'smooth'});");
            randomSleep(200, 600);
        }
    }
}
