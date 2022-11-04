package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static AtomicInteger beautyThree = new AtomicInteger(0);
    private static AtomicInteger beautyFour = new AtomicInteger(0);
    private static AtomicInteger beautyFive = new AtomicInteger(0);


    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        List<Thread> threadList = new ArrayList<>();

        Thread palindrome = new Thread(() -> {
            for (String text : texts) {
                if (checkPalindrome(text)) {
                    atomicIncrement(text);
                }
            }
        });
        threadList.add(palindrome);

        Thread allCharactersSame = new Thread(() -> {
            for (String text : texts) {
                if (checkAllCharactersSame(text)) {
                    atomicIncrement(text);
                }
            }
        });
        threadList.add(allCharactersSame);

        Thread ascending = new Thread(() -> {
            for (String text : texts) {
                if (checkAscending(text)) {
                    atomicIncrement(text);
                }
            }
        });
        threadList.add(ascending);

        for (Thread t: threadList){
            t.start();
        }

        for (Thread t: threadList){
            t.join();
        }

        System.out.format("Красивых слов с длиной 3: %s \n" +
                "Красивых слов с длиной 4: %s \n" +
                "Красивых слов с длиной 5: %s",
                beautyThree, beautyFour, beautyFive);
    }

    public static void atomicIncrement(String text) {
        switch (text.length()){
            case 3:
                beautyThree.incrementAndGet();
                break;
            case 4:
                beautyFour.incrementAndGet();
                break;
            case 5:
                beautyFive.incrementAndGet();
                break;
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean checkPalindrome(String letter){
        return letter.equals(new StringBuilder(letter).reverse().toString());
    }

    public static boolean checkAllCharactersSame(String letter){
        int n = letter.length();
        char first = letter.charAt(0);
        for (int i = 1; i < n; i++){
            if (letter.charAt(i) != first){
                return false;
            }
        }
        return true;
    }

    public static boolean checkAscending(String letter){
        int n = letter.length();
        for (int i = 1; i < n; i++){
            if (letter.charAt(i - 1) > letter.charAt(i)){
                return false;
            }
        }
        return true;
    }
}