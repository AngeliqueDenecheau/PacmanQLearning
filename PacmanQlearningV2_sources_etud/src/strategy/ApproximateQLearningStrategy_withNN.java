package strategy;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;
import agent.AgentAction;

import agent.PositionAgent;
import agent.typeAgent;
import motor.Maze;
import motor.PacmanGame;
import neuralNetwork.NeuralNetWorkDL4J;
import neuralNetwork.NeuralNetworkBasicJava;
import neuralNetwork.TrainExample;

import java.util.Random;


public class ApproximateQLearningStrategy_withNN extends QLearningStrategy {

	int nEpochs;
	int batchSize;
	
	public ApproximateQLearningStrategy_withNN(double epsilon, double gamma, double alpha,  int nEpochs, int batchSize) {
		super(epsilon, gamma, alpha);
		this.nEpochs = nEpochs;
		this.batchSize = batchSize;
	}

	
	@Override
	public void update(PacmanGame state, PacmanGame nextState, AgentAction action, double reward,
			boolean isFinalState) {
		
				//double[] features = getFeatures(state);
				//this.trainExamples.add(new TrainExample(features,targetQ));


	}

	@Override
	public AgentAction play(PacmanGame state) {
		// TODO Auto-generated method stub
		return this.play(state);
	}

	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		//nn.fit(trainExamples);
		
	}

	


}
