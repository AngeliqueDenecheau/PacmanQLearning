package strategy;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;
import agent.AgentAction;
import agent.MemoryAgent;
import agent.PositionAgent;
import agent.typeAgent;
import motor.Maze;
import motor.PacmanGame;
import neuralNetwork.NeuralNetWorkDL4J;

import neuralNetwork.TrainExample;

import java.util.Random;


public class DeepQLearningStrategy extends QLearningStrategy {

	int nEpochs;
	int batchSize;
	
	public DeepQLearningStrategy(double epsilon, double gamma, double alpha, int range, int nEpochs, int batchSize) {
		
		
		super(epsilon, gamma, alpha);

		
		this.nEpochs = nEpochs;
		this.batchSize = batchSize;
		
	}
	
	@Override
	public AgentAction jouer(PositionAgent position, PacmanGame game, PositionAgent objectif, MemoryAgent memoryAgent) {
		
		
		// A compléter
		
		return new AgentAction((int) Math.floor(Math.random() * 4));
		
		
	}


	@Override
	public void update(PacmanGame game, PositionAgent newPos, AgentAction action, double reward, boolean isFinalState, MemoryAgent memoryAgent) {
		
		// A compléter
		
		
	}


	
	public void learn(ArrayList<TrainExample> trainExamples) {
		
		// A compléter
	}
	
	
}
