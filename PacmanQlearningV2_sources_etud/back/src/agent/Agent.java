package agent;

import java.util.ArrayList;

import motor.Maze;
import motor.PacmanGame;
import neuralNetwork.TrainExample;
import strategy.Strategy;

public class Agent {
	
	private typeAgent _type;
	private PositionAgent _position;
	private Strategy _behavior_ghostScarred;
	private Strategy _behavior_normal;
	private int next_move;
	private boolean _alive;
	private PositionAgent _first_position;


	public MemoryAgent memoryAgent;
	
	
	public Agent(PositionAgent pa, typeAgent type) {
		set_position(pa);
		set_type(type);
		_first_position = pa;
		_alive = true;
		memoryAgent = new MemoryAgent();
	}

	public AgentAction play(PacmanGame game, boolean fantom_scared, PositionAgent objectif) {
		
		if (_behavior_normal == null) {
			return new AgentAction(next_move);
		} else {
			if (fantom_scared) {
				return _behavior_ghostScarred.jouer(_position, game, objectif, this.memoryAgent);
			} else {
				return _behavior_normal.jouer(_position, game, objectif, this.memoryAgent);
			}
		}
	}

	
	public void update(PacmanGame game, PositionAgent newPos, AgentAction action, double reward, boolean finJeu) {
		
		this._behavior_normal.update(game, newPos, action, reward, finJeu, this.memoryAgent);
		
	}
	
	
	public void BackToOriginalPosition() {
		_position = _first_position;
	}

	public typeAgent get_type() {
		return _type;
	}

	public void set_type(typeAgent _type) {
		this._type = _type;
	}

	public PositionAgent get_position() {
		return _position;
	}

	public void set_position(PositionAgent _position) {
		this._position = _position;
	}

	public String toString() {
		return "Hey! I'm "
				+ (_type == typeAgent.FANTOM ? "a fantom! Pacman gonna die!" : "Pacman! I'm gonna eat them all!")
				+ " My position is " + _position;
	}

	public void setNextMove(int next) {
		next_move = next;
	}


	public boolean is_alive() {
		return _alive;
	}

	public void set_alive(boolean _alive) {
		this._alive = _alive;
	}

	public void setBeheviorNormal(Strategy b) {
		_behavior_normal = b;
	}

	public void setBeheviorScared(Strategy b) {
		_behavior_ghostScarred = b;
	}
	
	public PositionAgent get_first_position(){
		return _first_position;
	}
	
	

}