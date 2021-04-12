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

import neuralNetwork.TrainExample;

import java.util.Random;


public class DeepQLearningStrategy extends QLearningStrategy {


	int nEpochs;
	int batchSize;
	int range;
	
	int d,n,s;
	
	NeuralNetWorkDL4J nn;
	
	public DeepQLearningStrategy(double epsilon, double gamma, double alpha, int range, int nEpochs, int batchSize) {
		
		
		super(epsilon, gamma, alpha);
		this.nEpochs = nEpochs;
		this.batchSize = batchSize;
		this.range = range;
		s=4; // nb sorties possibles
		d=3; //nb layers pour l'encoded state
		n=range*range*d;
		
		this.nn = new NeuralNetWorkDL4J(alpha,0,this.n, s);
		
	}
	
	@Override
	public void update(PacmanGame state, PacmanGame nextState, AgentAction action, double reward,
			boolean isFinalState) {
		
		double[] X = getEncodedState(state);
		double[] Y = this.nn.predict(X);

		double maxQvalue_nextState=-999999;		
		if(isFinalState){
			maxQvalue_nextState = 0;
		}else{
			maxQvalue_nextState = getMaxQNext(nextState);
		}

		Y[action.get_direction()] = reward + gamma*maxQvalue_nextState;
		
		TrainExample trainExample = new TrainExample(X,Y);
		this.trainExamples.add(trainExample);
		
	}
	
	@Override
	public AgentAction play(PacmanGame state) {
		ArrayList<AgentAction> legalActions = new ArrayList<AgentAction>();
		
		Maze maze = state.getMaze();
		
		AgentAction actionChoosen = new AgentAction(0);
		
		for(int i =0; i < 4; i++) {
			
			AgentAction action = new AgentAction(i);
			
			if(!maze.isWall(state.pacman.get_position().getX() + action.get_vx(),
					state.pacman.get_position().getY() + action.get_vy())) {
				
				legalActions.add(action);
			}
			
		}
		
		if(Math.random() < this.epsilon){
			
			actionChoosen = legalActions.get((int) Math.floor(Math.random() * legalActions.size()));
					
		} else {
			
			double maxQvalue = -9999;
			int trouve=1;
			
			for(AgentAction action : legalActions) {
				double[] encodedState=getEncodedState(state);
				double[] output = this.nn.predict(encodedState);
				if(maxQvalue<output[action.get_direction()]) {
					maxQvalue=output[action.get_direction()];
					actionChoosen=action;
					trouve=1;
				}
					else if(maxQvalue==output[action.get_direction()]) {
						trouve+=1;
						if(Math.floor(trouve*Math.random())== 0) {
							maxQvalue = output[action.get_direction()];
							actionChoosen = action;
						}
					}
				}
			
			}
		
		return actionChoosen;
	}
	
	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		nn.fit(trainExamples, nEpochs, batchSize,alpha);
		
	}
	
	public double[] getEncodedState(PacmanGame game){
		double[] result=new double[this.n];
		int iWall=0;
		int iFood=range*range;
		int iGhost=range*range*2;
		
		int x = game.pacman._position.getX();
		int y = game.pacman._position.getY();
		Maze maze=game.getMaze();
		
		for(int i=x-(range/2);i<=x+(range/2);i++) {
			for(int j=y-(range/2);j<=x+(range/2);j++) {
				
				//ajout des murs
				if(maze.isWall(i, j)) {
					result[iWall]=1;
					result[iFood]=0;
					result[iGhost]=0;
				}else{
					result[iWall]=0;
					
					//ajout des foods & capsules
					double isFood = maze.isFood(i, j)? (double)1 : (double)0;
					result[iFood]=maze.isCapsule(i, j)? -1 : isFood;
					
					//ajout des fantômes
					boolean ghostSeen=false;
					for(PositionAgent gPos:game.getPostionFantom()) {
						if(gPos.getX()==i && gPos.getY()==j) {
							ghostSeen=true;
							if(game.isGhostsScarred()) result[iGhost]=-1;
							else result[iGhost]=1;
							break;
						}
					}
					if(!ghostSeen)result[iGhost]=0;
				}
				iWall++;
				iFood++;
				iGhost++;
			}
		}
		String affichage=" result :";
		for(int i=0;i<result.length;i++) {
			affichage+=result[i]+" ";
		}
		System.out.println(affichage);
		return result;
	}
	
	public double getMaxQNext(PacmanGame game){
		double maxQvalue = -99999;
		double[] nextQValue = this.nn.predict(getEncodedState(game));
		
		for(int i =0; i < 4; i++) {
			if(nextQValue[i]>maxQvalue) maxQvalue=nextQValue[i];	
		}
		
		return maxQvalue;
	}
	
	
}
