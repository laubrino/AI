package cz.laubrino.ai.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tomas.laubr on 29.11.2019.
 */
class Notifier {
    private final List<Observer> observers = new ArrayList<>();

    void notifyQValueChanged(float deltaQ, int deltaQInPercent) {
        for (Observer observer : observers) {
            observer.qValueChanged(deltaQ, deltaQInPercent);
        }
    }

    void notifyLearningEpisodeFinished(long episode, long steps) {
        for (Observer observer : observers) {
            observer.learningEpisodeFinished(episode, steps);
        }
    }

    void notifyTestingEpisodeFinished(ActionResult actionResult, long steps) {
        for (Observer observer : observers) {
            observer.testingEpisodeFinished(actionResult, steps);
        }
    }

    void notifyTestingBatchFinished(int successEpisodes, int allEpisodes, float minReward, float maxReward, float averageReward) {
        for (Observer observer : observers) {
            observer.testingBatchFinished(successEpisodes, allEpisodes, minReward, maxReward, averageReward);
        }
    }

    void notifyProcessEnd(){
        for (Observer observer : observers) {
            observer.end();
        }
    }

    void notifyProcessStart(Observer.StartConfiguration startConfiguration) {
        for (Observer observer : observers) {
            observer.start(startConfiguration);
        }
    }

    void addObserver(Observer observer) {
        observers.add(observer);
    }



}
