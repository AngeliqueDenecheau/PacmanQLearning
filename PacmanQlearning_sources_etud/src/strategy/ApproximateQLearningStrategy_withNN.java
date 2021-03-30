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
import neuralNetwork.NeuralNetworkBasicJava;
import neuralNetwork.TrainExample;

import java.util.Random;


public class ApproximateQLearningStrategy_withNN extends QLearningStrategy {

	int d;
	NeuralNetWorkDL4J nn;
	
	int nEpochs;
	int batchSize;
	
	
	
	public ApproximateQLearningStrategy_withNN(double epsilon, double gamma, double alpha,  int nEpochs, int batchSize) {
		
		super(epsilon, gamma, alpha);
		
		
		this.nEpochs = nEpochs;
		this.batchSize = batchSize;
		
		// A compléter
		
	}
	
	@Override
	public AgentAction jouer(PositionAgent position, PacmanGame game, PositionAgent objectif, MemoryAgent memoryAgent) {
		
		// A compléter
		
		return new AgentAction((int) Math.floor(Math.random() * 4));
		
	}
	
	
	

	public void update(PacmanGame game, PositionAgent newPos, AgentAction action, double reward, boolean isFinalState, MemoryAgent memoryAgent) {
		
		// A compléter
		
		
	
	}
	

	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		
		
		// A compléter
		
	}



}
