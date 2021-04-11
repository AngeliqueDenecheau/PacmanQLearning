package strategy;

import java.io.Serializable;

import agent.AgentAction;
import agent.PositionAgent;
import motor.PacmanGame;


public class RandomStrategy implements Strategy, Serializable{

	@Override
	public AgentAction play(PacmanGame state, PositionAgent positionAgent, PositionAgent objectif) {
		
		return new AgentAction((int) Math.floor(Math.random() * 4));
	}

	@Override
	public void update(PacmanGame state, PacmanGame nextState, AgentAction action, double reward,
			boolean isFinalState) {
		// TODO Auto-generated method stub
		
	}



}
