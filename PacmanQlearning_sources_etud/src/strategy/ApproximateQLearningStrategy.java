package strategy;

import java.util.ArrayList;

import agent.AgentAction;
import agent.MemoryAgent;
import agent.PositionAgent;
import motor.PacmanGame;
import neuralNetwork.TrainExample;

public class ApproximateQLearningStrategy extends QLearningStrategy{

	
	public ApproximateQLearningStrategy(double epsilon, double gamma, double alpha) {
		super(epsilon, gamma, alpha);
		// TODO Auto-generated constructor stub
	}

	@Override
	public AgentAction jouer(PositionAgent position, PacmanGame game, PositionAgent objectif, MemoryAgent memoryAgent) {
		// TODO Auto-generated method stub
		return new AgentAction((int) Math.floor(Math.random() * 4));
	}

	@Override
	public void update(PacmanGame game, PositionAgent newPos, AgentAction action, double reward, boolean isFinalState,
			MemoryAgent memoryAgent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		// TODO Auto-generated method stub
		
	}

}
