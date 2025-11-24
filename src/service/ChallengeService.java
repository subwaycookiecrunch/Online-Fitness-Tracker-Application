package service;

import dao.ChallengeDAO;
import util.CustomExceptions.DatabaseException;

public class ChallengeService {
    private ChallengeDAO challengeDAO;

    public ChallengeService() {
        this.challengeDAO = new ChallengeDAO();
    }

    public void joinChallenge(int userId, int challengeId) {
        try {
            challengeDAO.joinChallenge(userId, challengeId);
            System.out.println("User " + userId + " successfully joined challenge " + challengeId);
        } catch (DatabaseException e) {
            System.err.println("Failed to join challenge: " + e.getMessage());
        }
    }
}
