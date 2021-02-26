package com.vietthang;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


/*
    It is said that 90% of frosh expect to be above average in their class. You are to provide a reality check.
    ### Input
        The first line of standard input contains an integer 1≤C≤50, the number of test cases. C data sets follow. Each data set begins with an integer, N, the number of people in the class (1≤N≤1000). N integers follow, separated by spaces or newlines, each giving the final grade (an integer between 0 and 100) of a student in the class.
    ### Output
        For each case you are to output a line giving the percentage of students whose grade is above average, rounded to exactly 3 decimal places.
    ### Sample Input 1
        5
        5 50 50 70 80 100
        7 100 95 90 80 70 60 50
        3 70 90 80
        3 70 90 81
        9 100 99 98 97 96 95 94 93 91
    ### Sample Output 1
        40.000%
        57.143%
        33.333%
        66.667%
        55.556%
 */
public class AboveAverage {
    static final String SPACE = " ";

    public static void main(String[] args) {
        // input
        Scanner in = new Scanner(System.in);
        int numCases = in.nextInt();
        in.nextLine();
        for (int i = 0; i < numCases; i++) {
            String input = in.nextLine();
            String[] data = input.split(SPACE);
            double numOfPeople = Double.parseDouble(data[0]);
            List<Double> scoreList = new ArrayList<>();

            double totalScores = 0;
            for (int j = 0; j < numOfPeople; j++) {
                double score = Double.parseDouble(data[j + 1]);
                scoreList.add(score);
                totalScores += score;
            }
            double average = totalScores / numOfPeople;
            double aboveAverage = 0;
            for (double score : scoreList) {
                if(score> average){
                    aboveAverage++;
                }
            }
            double percentage =100 * (aboveAverage) / numOfPeople;
            BigDecimal bd = BigDecimal.valueOf(percentage);
            bd = bd.setScale(3, RoundingMode.HALF_UP);
            System.out.println(String.format("%.3f", bd.doubleValue()) + "%");
        }
    }
}
