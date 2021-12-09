package com.slots.game.service;

import java.util.Random;
import java.util.Scanner;

public class Test {
    public static void main(String...args) {
        Random generator = new Random();
        Scanner console = new Scanner(System.in);

        int input;
        int iTokens= 100;
        int iSlot1, iSlot2, iSlot3;
        do {
            System.out.println("Slot Machine");
            System.out.println("Token:" + iTokens);
            System.out.println("Press 1 to pull, 2 to quit");
            input = console.nextInt();

            iSlot1 = generator.nextInt(3) + 1;
            iSlot2 = generator.nextInt(3) + 1;
            iSlot3 = generator.nextInt(3) + 1;

            System.out.println(iSlot1 + " " + iSlot2 + " " + iSlot3);

            if (iSlot1 == iSlot2 && iSlot1 == iSlot3) {
                System.out.println("You win 10 tokens");
                iTokens += 10;
            } else if (iSlot1 == iSlot2 || iSlot1 == iSlot3 || iSlot2 == iSlot3) {
                System.out.println("You win 5 tokens");
                iTokens += 5;
            } else {
                System.out.println("You lost a token");
                iTokens -= 1;
            }
        } while (input == 1);

    }
}
