package edu.system;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LearningService {

    private static final Logger logger =
            LogManager.getLogger(LearningService.class);

    public String welcome(String username) {
        logger.info("Generating welcome message for user {}", username);
        return "Welcome to Interactive Learning System, " + username;
    }

    public int calculateScore(int completedTasks, int totalTasks) {
        logger.debug("Calculating score: completed={}, total={}",
                completedTasks, totalTasks);

        if (totalTasks == 0) {
            logger.error("Total tasks is zero");
            throw new IllegalArgumentException("Total tasks cannot be zero");
        }

        int score = (completedTasks * 100) / totalTasks;
        logger.info("Calculated score: {}", score);
        return score;
    }
}
