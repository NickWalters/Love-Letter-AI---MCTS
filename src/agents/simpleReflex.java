package agents;
import loveletter.*;
import java.util.Random;

/**
 * An interface for representing an agent in the game Love Letter
 * All agent's must have a 0 parameter constructor
 * */

// @author Nicholas Walters

public class simpleReflex implements Agent{

  private Random rand;
  private State current;
  private int myIndex;

  
  //0 place default constructor
  public simpleReflex(){
    rand  = new Random();
  }

  
  
  /**
   * Reports the agents name
   * */
  public String toString(){return "simpleReflexAgent";}


  
  
  /**
   * Method called at the start of a round
   * @param start the starting state of the round
   **/
  public void newRound(State start){
    current = start;
    myIndex = current.getPlayerIndex();
  }

  
  
  
  
  
  /**
   * Method called when any agent performs an action. 
   * @param act the action an agent performs
   * @param results the state of play the agent is able to observe.
   * **/
  public void see(Action act, State results){
    current = results;
  }
  
  

  
  /**
    ToDo : 1, Save values of other opponents cards when shown, and make decisions based on knowing other peoples deck
       
      2, Make decisions based on card ranges, and the amount of discarded cards
          e.g there is only 4 guards, there is only 1 countess
          if a countess has already been played, then it cannot occur again
  
  */
  
  /**
   * Perform an action after drawing a card from the deck
   * @param c the card drawn from the deck
   * @return the action the agent chooses to perform
   * @throws IllegalActionException when the Action produced is not legal.
   * */
  public Action playCard(Card c){
	  Action act = null;
	  Card play = null;
	  int drawnCard = c.value();
	  int handCard = current.getCard(myIndex).value();
	  boolean alreadyDecided = false;
	  
	  while(!current.legalAction(act, c)) {
		  
		  // this checks if you have a countess needing to be played
		  if((handCard == 7 || drawnCard == 7)) {
		        if(handCard == 6 || drawnCard == 6) {
		          alreadyDecided = true;
		          if(drawnCard == 7) play = c;
		          else play = current.getCard(myIndex);
		        }
		        if(handCard == 5 || drawnCard == 5) {
		          alreadyDecided = true;
		          if(drawnCard == 7) play = c;
		          else play = current.getCard(myIndex);
		        }
		      }
		  
		  // this checks if you can play a hand maid!
		  if((handCard == 4) || (drawnCard == 4) && alreadyDecided == false) {
			  if(drawnCard == 4) {
				  play = c;
				  alreadyDecided = true;
			  }
			  else {
				  play = current.getCard(myIndex);
				  alreadyDecided = true;
			  }
		  }
		  
		  else if (alreadyDecided == false) {
			  if(handCard <= drawnCard) {
				  play = c;
			  }
			  else {
				  play = current.getCard(myIndex);
			  }
			  // if you have a princess, play other card
			  if(handCard ==8 || drawnCard==8) {
				  if(handCard == 8) {
					  play = c;
				  }
				  else {
					  play = current.getCard(myIndex);
				  }
			  }
		  }
		  int target = rand.nextInt(current.numPlayers());
		  
		  try {
			  switch(play) {
			        case GUARD:
			        	act = Action.playGuard(myIndex, target, Card.values()[guess(current)]);
			        	break;
		            case PRIEST:
		            	act = Action.playPriest(myIndex, target);
		            	break;
		            case BARON:  
		            	act = Action.playBaron(myIndex, target);
		            	break;		          
		            case HANDMAID:
		            	act = Action.playHandmaid(myIndex);
		            	break;
		            case PRINCE:  
		            	act = Action.playPrince(myIndex, target);
		            	break;
		            case KING:
		            	act = Action.playKing(myIndex, target);
		            	break;
		            case COUNTESS:
		            	act = Action.playCountess(myIndex);
		            	break;
		            default:
		            	act = null;//never play princess
			  }
		  }catch(IllegalActionException e){/*do nothing, just try again*/} 
	  	}
	  return act;
	}
  
  
  // returns playerIndex of target player to try and eliminate
  public int topScorer(State current) {
	  int playerCount = current.numPlayers();
	  int maxScore = -1;
	  int maxPlayer = -1;
	  
	  for(int i=0; i<= playerCount-1; i++) {
		  if(current.score(i) > maxScore) {
			  if(i == myIndex) {
				  continue;
			  }
			  maxScore = current.score(i);
			  maxPlayer = i;
		  }
	  }
	  
	  if(maxScore == 0) {
		  return rand.nextInt(current.numPlayers());
	  }
	  return maxPlayer;
  }
  
  
  // returns a guess for the guard. the guess is in the form of a Card
  public int guess(State current) {
	  Card[] unseenCards = current.unseenCards();
	  int[] deck = {0,0,0,0,0,0,0,0};
	  
	  for(Card card: unseenCards){
		  try {
			  switch(card.value()) {
			    case 1:
			    	continue;
			  	//priest
			  	case 2:
			  		deck[1] = deck[1] + 1;
			  	//baron
			  	case 3:
			  		deck[2] = deck[2] + 1;
			  	//hand-maid
			  	case 4:
			  		deck[3] = deck[3] + 1;
			  	//prince
			  	case 5:
			  		deck[4] = deck[4] + 1;
			  	//king
			  	case 6:
			  		deck[5] = deck[5] + 1;
			  	//countess
			  	case 7:
			  		deck[6] = deck[6] + 1;
			  	// princess
			  	case 8:
			  		deck[7] = deck[7] + 1;
			  }
			  
		  }catch(Exception e){/*do nothing, just try again*/} 
	  }
	  // minus the agents current hand
	  int indexOfDeduction = current.getCard(myIndex).value()-1;
	  deck[indexOfDeduction] = deck[indexOfDeduction] - 1;
	  
	  int largest = 0;
	  for (int i=0; i<8; i++)
	  {
	      if ( deck[i] >= deck[largest] ) largest = i;
	  }
	  return largest;
  }
  
  
}





