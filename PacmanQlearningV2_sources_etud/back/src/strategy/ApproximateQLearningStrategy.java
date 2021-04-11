package strategy;

import java.util.ArrayList;
import java.util.Random;

import agent.Agent;
import agent.AgentAction;
import agent.MemoryAgent;
import agent.PositionAgent;
import motor.PacmanGame;
import neuralNetwork.TrainExample;

public class ApproximateQLearningStrategy extends QLearningStrategy{

	double[] weights;
	int nb_features=4;
	/* 0 : =1
	 * 1 : {0,1} si mange pacgomme
	 * 2: {int} nb_coups pour atteindre la prochaine pacgomme
	 * 3 : {int} nb_fantômes autour de la case cible
	 * 
	 * ajout de isGhostScared ?
	 * ajout du compte de murs?
	 */
	
	public ApproximateQLearningStrategy(double epsilon, double gamma, double alpha) {
		super(epsilon, gamma, alpha);
		weights=new double[nb_features+1];
	}

	@Override
	public AgentAction jouer(PositionAgent position, PacmanGame game, PositionAgent objectif, MemoryAgent memoryAgent) {
		
		//cas de selection aléatoire dépendant de Epsilon
		if(Math.random() < epsilon) {
			return new AgentAction((int) Math.floor(Math.random() * 4));
		} else { // cas de choix du meilleur coup
			
			double maxQvalue = -999;
			int best_a = -1;
			
			for(int a=0; a < 4; a++) {
				if(game.isLegalMove(game.get_agentsPacman(),new AgentAction(a))) {
					double[] features = extractFeatures(position,game, a);

					double qValue = scalarProduct(weights, features);
					
					if(qValue > maxQvalue) {
						maxQvalue = qValue;
						System.out.println("best qValue:"+qValue+" for "+a);
						best_a = a;
					}
				}
				
			}
		
			return new AgentAction(best_a);
		}
		
	}

	@Override
	public void update(PacmanGame game, PositionAgent newPos, AgentAction action, double reward, boolean isFinalState,
			MemoryAgent memoryAgent) {
		
		double maxQvalue_nextState = -999;
		
		for(int a=0; a < nb_features; a++) {
			double[] features = extractFeatures(newPos,game, a );
			double qValue_nextState = scalarProduct(weights, features);
			if(qValue_nextState > maxQvalue_nextState) {
				maxQvalue_nextState = qValue_nextState;
			}
		}
		
		double targetQ = reward + gamma*maxQvalue_nextState;
		
		
		double[] features = extractFeatures(game.get_agentsPacman().get_position(),game, action.get_direction());
		double qValue = scalarProduct(weights, features);

		for(int i=0; i < nb_features; i++) {
			
			this.weights[i] = this.weights[i] - 2*this.alpha*features[i]*(qValue - targetQ);
		
		}
		
	}

	@Override
	public void learn(ArrayList<TrainExample> trainExamples) {
		// no need for learning here	
	}

public double scalarProduct(double[] weights, double[] features) {
		
		double res = 0;
				
		for(int i = 0; i < weights.length; i++) {
			res += weights[i]*features[i];
		}
		
		return res;	
		
	}
	
	
	public double[] extractFeatures(PositionAgent new_pos, PacmanGame game,int a) {
		/*
		//creation d'un autre Game pour simuler le tour
		PacmanGame game=real_game;
		game.get_agentsPacman().setNextMove(a);
		game.takeTurn();
		PositionAgent new_pos=game.get_agentsPacman().get_position();
		*/
		//calcul des features
		
		
		
		double[] features = new double[nb_features+1];
		features[0] = 1;
		
		if(game.getMaze().isFood(new_pos.getX(),new_pos.getY()) || game.getMaze().isCapsule(new_pos.getX(), new_pos.getY())) {
			features[1]=1;
		}else features[1]=0;
		
		features[2]=0; // temporaire en attendant de trouver comment la coder
		//TODO features[2]
		
		// PEUT ETRE LE SEPARER EN DEUX FEATURES : UNE GHOST SCARED, UNE GHOST NORMAL. 
		// LES GARDER FUSIONNEE SI ON EN A BEAUCOUP
		if(game.isGhostsScarred()) {
			features[3]=-countGhostsAround(new_pos,game);
		}else{
			features[3]=countGhostsAround(new_pos,game);
		}
				
		return features;
	}
	
	private double countGhostsAround(PositionAgent pos, PacmanGame game) {
		double result=0;
		
		//on verifie pour chaque fantôme s'il est dans une des case autour de la position
		for(Agent ghost : game.get_agentsFantom()) {
			if(ghost.get_position().getX()==pos.getX()){
				if((ghost.get_position().getY()==pos.getY()+1)||(ghost.get_position().getY()==pos.getY()-1)||(ghost.get_position().getY()==pos.getY())) {
					result++;
				}
			} else if((ghost.get_position().getX()==pos.getX()+1) || (ghost.get_position().getX()==pos.getX()-1)) {
				if(ghost.get_position().getY()==pos.getY()) result++;
			} 
		}
		System.out.println("si je vais à la position "+pos+" je serai entouré de "+result+" fantômes");
		
		return result;
		
	}
	
}
