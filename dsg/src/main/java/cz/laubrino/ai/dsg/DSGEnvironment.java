package cz.laubrino.ai.dsg;

import cz.laubrino.ai.framework.ActionResult;
import cz.laubrino.ai.framework.Environment;

/**
 * @author tomas.laubr on 10.12.2019.
 */
public class DSGEnvironment implements Environment<Action> {
    private static final int F_TO_SHORT = 1000;
    private static final int TARGET_REWARD = 500 * F_TO_SHORT;

    private final int xDim = 5;
    private final int yDim = 5;  //these should be the same!!!
    //(Exercise: fix this restriction (look at SGameGUI too))
    private int numberOfSteps = 0;
    private double totalReward = 0.0;

    private double minReward = 0.0;

    private int currX = (int) (Math.random() * xDim);  // current X position
    private int currY = (int) (Math.random() * yDim);  // current Y position

    /**
     * Prize Location.
     * <ul>
     * <li> 4 means there is no prize
     * <li> 0 means prize is at the top left
     * <li> 1 means the prize is at the top right
     * <li> 2 means the prize is ar the bottom left
     * <li> 3 means the prize is at the bottom right
     * </ul>
     */
    private int prize = 4;  // 4 means no prize
    // Damage
    private boolean damaged = false;
    // Rewards
    private static double crashReward = -1.0 * F_TO_SHORT;

    private static double m21appearsProb = 0.4;
    private static double m42appearsProb = 0.4;
    private static double m03appearsProb = 0.4;
    private static double m13appearsProb = 0.4;
    private static double m33appearsProb = 0.4;
    private static double prizeAppearsProb = 0.3;
    private static double prizeReward = 10 * F_TO_SHORT;
    private static double rewardMonsterWhenDamaged = -10 * F_TO_SHORT;

    /**
     * does one step.
     *
     * <p>
     * ctions are
     * <ul>
     * <li> 0 is up
     * <li> 1 is right
     * <li> 2 is down
     * <li> 3 is left
     * </ul>
     *
     * @param action the action that the agent does
     * @return reward
     */
    @Override
    public ActionResult step(Action action) {
        Action actualDirection;
        double reward = 0.0;
        int newX, newY;

        // Determine monster appearances

        // Monsters
        boolean m21 = Math.random() < m21appearsProb;
        boolean m42 = Math.random() < m42appearsProb;
        boolean m03 = Math.random() < m03appearsProb;
        boolean m13 = Math.random() < m13appearsProb;
        boolean m33 = Math.random() < m33appearsProb;

        // Determine if prize appears

        if (prize == 4  // no prize was previously present
                && Math.random() < prizeAppearsProb)  // a prize appears
            prize = (int) (Math.random() * 4);   // the corner is chosen

        // determine actual direction
        int rand = (int) (Math.random() * 10);
        if (rand < 4) actualDirection = Action.VALUES[rand];
        else actualDirection = action;


        // Determine where the agent ends up (plus crash reward)

        if (actualDirection == Action.RIGHT   // going right
                && ((currY == 0 && currX < 2) || (currY == 1 && currX == 0)))
        // hit internal wall
        {
            reward = crashReward;
            newX = currX;
            newY = currY;
        } else if (actualDirection == Action.LEFT   // going left
                && ((currX < 3 && currY == 0) || (currX == 1 && currY == 1)))
        // hit internal wall (or going left from 0,0)
        {
            reward = crashReward;
            newX = currX;
            newY = currY;
        } else {
            switch (actualDirection) {
                case UP: // Up
                    if (currY == 0) {
                        newY = currY;
                        newX = currX;
                        reward = crashReward;
                    } else {
                        newY = currY - 1;
                        newX = currX;
                    }
                    break;
                case RIGHT: // Right
                    if (currX == xDim - 1) {
                        newY = currY;
                        newX = currX;
                        reward = crashReward;
                    } else {
                        newY = currY;
                        newX = currX + 1;
                    }
                    break;
                case DOWN: // Down
                    if (currY == yDim - 1) {
                        newY = currY;
                        newX = currX;
                        reward = crashReward;
                    } else {
                        newY = currY + 1;
                        newX = currX;
                    }
                    break;
                case LEFT: // Left
                    if (currX == 0) {
                        newY = currY;
                        newX = currX;
                        reward = crashReward;
                    } else {
                        newY = currY;
                        newX = currX - 1;
                    }
                    break;
                default:   // should never occur
                {
                    throw new RuntimeException();
                }
            }
        }

        // Determine if monster got the agent
        if ((newX == 2 && newY == 1 && m21) ||
                (newX == 4 && newY == 2 && m42) ||
                (newX == 0 && newY == 3 && m03) ||
                (newX == 1 && newY == 3 && m13) ||
                (newX == 3 && newY == 3 && m33)) {
            if (damaged)
                reward += rewardMonsterWhenDamaged;
            else
                damaged = true;
        }

        // Determine if agent gets repaired
        if (damaged && newX == 1 && newY == 0)
            damaged = false;

        // Determine if agent gets the prize
        if (prize < 4 &&
                newX == (prize % 2) * (xDim - 1) &&
                newY == (prize / 2) * (yDim - 1)) {
            reward += prizeReward;
            prize = 4;
        }

        numberOfSteps++;
        totalReward += reward;
        if (totalReward < minReward) {
            minReward = totalReward;
        }

        currX = newX;
        currY = newY;

        if (totalReward > TARGET_REWARD) {
            return new ActionResult(new State(currX, currY, damaged, prize), (float)reward, ActionResult.Type.OK);
        } else {
            return new ActionResult(new State(currX, currY, damaged, prize), (float)reward, ActionResult.Type.CONTINUE);
        }
    }

    @Override
    public boolean isFinalStateAchieved() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public Action[] getAvailableActions() {
        return Action.VALUES;
    }

    @Override
    public State getState() {
        return new State(currX, currY, damaged, prize);
    }
}
