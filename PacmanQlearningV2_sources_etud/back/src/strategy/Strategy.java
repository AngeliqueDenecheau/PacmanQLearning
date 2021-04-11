package strategy;

import java.util.ArrayList;

import agent.AgentAction;
import agent.MemoryAgent;
import agent.PositionAgent;
import agent.typeAgent;
import motor.Maze;
import motor.PacmanGame;
import neuralNetwork.TrainExample;

public interface Strategy {
	

	
	public AgentAction jouer(PositionAgent position, PacmanGame game, PositionAgent objectif, MemoryAgent memoryAgent);
	

	public void update(PacmanGame game, PositionAgent newPos, AgentAction action, double reward, boolean isFinalState, MemoryAgent memoryAgent);
	
}
