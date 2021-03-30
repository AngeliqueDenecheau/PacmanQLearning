package strategy;

import agent.AgentAction;
import agent.MemoryAgent;
import agent.PositionAgent;
import motor.PacmanGame;


public class RandomStrategy implements Strategy {

	@Override
	public AgentAction jouer(PositionAgent position, PacmanGame game, PositionAgent objectif, MemoryAgent memoryAgent) {
		
		return new AgentAction((int) Math.floor(Math.random() * 4));
	}

	@Override
	public void update(PacmanGame game, PositionAgent newPos, AgentAction action, double reward, boolean isFinalState, MemoryAgent memoryAgent) {
		// TODO Auto-generated method stub
		
	}




}
