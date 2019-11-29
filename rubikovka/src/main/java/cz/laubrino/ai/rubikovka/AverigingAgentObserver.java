package cz.laubrino.ai.rubikovka;


public class AverigingAgentObserver  {
        long millis = System.currentTimeMillis();
        int counter = 0;
        long percentageSum = 0;


        public void qValueChanged(float deltaQ, int deltaQInPercent) {
            percentageSum += deltaQInPercent;
            counter++;
            long now = System.currentTimeMillis();
            if (now - millis > 1000) {
                millis = now;
                System.out.println("+++ " + percentageSum/counter);
                percentageSum = 0;
                counter = 0;
            }
        }
    }