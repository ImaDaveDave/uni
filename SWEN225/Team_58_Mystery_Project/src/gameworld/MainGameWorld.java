package gameworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * Main GameWorld class
 *
 * @author Jaimar
 *
 * Contributors:
 * David
 * Tom
 *
 */
public class MainGameWorld {
  private List<GameObjects> gameObjects = new ArrayList<>();//keeps all objects of the game
  private List<Teleporter> activeTeleporters = new ArrayList<Teleporter>();

  private Random random = new Random();

  private Player player; //field for a player
  private Board board; //field for new board
  private int[][] map;

	Timer timer;

  //testing
  private final int L4TELECOUNT = 200;

  private double telX;
  private double telY;

  /**
   * new game only constructor, loads default LEVEL_ONE and creates new player
   * @author David
   */
  public MainGameWorld(Level lvl) {
    this.board = new Board(lvl);
    this.player = new Player(board.getLvl().getPosX(),board.getLvl().getPosY());
    timer = new Timer();
    this.map = returnMap();
    initialize();
    }


  public void initialize() {
    for(int i = 0; i < map.length-1; i++) {
      for(int j = 0; j < map.length-1; j++) {
        if(map[i][j] == 2) {
          this.gameObjects.add(new Key("Key",(double)i,(double)j));
        }else if(map[i][j] == 3) {
          this.gameObjects.add(new HiddenKey("HiddenKey",(double)i,(double)j));
        }else if(map[i][j]==6) {
          this.gameObjects.add(new HiddenDoor("HiddenDoor",(double)i,(double)j));
        }else if(map[i][j] == 5) {
          this.gameObjects.add(new Door("Door",i,j));
        }else if(map[i][j] == 8) {
          this.gameObjects.add(new Door("FinalDoor",i,j,true));
        }else if(map[i][j] == 7) {
          this.gameObjects.add(new RedDoor("RedDoor",i,j));
        }
      }
    }
    //initilise teleporters depending on lvl. (David)
    //specifiy coordinates of every teleporter on the level here. (David)
    switch (this.getBoard().getLvl()) {
      case LEVEL_TWO:
        createTeleporters(activeTeleporters, 13,1,14,15,8,15,11,16,20,16,21,5,21,7,21,15);
        break;
      case LEVEL_THREE:
        break;
      case LEVEL_FOUR:
        int[] telesX = new int[L4TELECOUNT];
        int[] telesY = new int[L4TELECOUNT];
        for (int i=0;i<L4TELECOUNT;i++) {
          telesX[i] = random.nextInt(20)+1;
          telesY[i] = random.nextInt(19)+1;
        }
        createTeleporters(activeTeleporters, telesX, telesY);
        break;
      default:
        break;
    }
  }

  /**
   * add telporters to list from coordinates given from method call
   * coorinates are Y,X.
   * @param teleList
   * @param i variable number of int arguments for the cordinates.
   * @author David
   */
  private void createTeleporters(List<Teleporter> teleList, int... i) {
    for (int j = 0; j < i.length ; j = j+2) {
      teleList.add(new Teleporter(i[j],i[j+1]));
    }
  }

  private void createTeleporters(List<Teleporter> teleList, int[] telesX, int[] telesY) {
    for (int i = 0; i < L4TELECOUNT ; i++) {
      teleList.add(new Teleporter(telesY[i],telesX[i]));
    }
  }

  public List<Teleporter> getTeleportList(){
    return this.activeTeleporters;
  }

  /**
   * Tells player to interact
   */
  public Interaction interact() {
    return player.interactOn(this.gameObjects, map);
  }

  public void resetMap() {
    for (GameObjects g : gameObjects) {
      if ( g instanceof Door) {
        map[(int) ((Door) g).getPosX()][(int) ((Door) g).getPosY()] = 5;
      }
    }
  }

  /**
   * checks if player is on a teleporter
   * does lots of checks and finds a teleport node that the player isnt at, and moves them therre
   * @author David
   *
   */
  public void checkTeleporter() {
    Teleporter atTeleporter=null;
    //check if player is on telporter
    if(player.isOnTeleport(activeTeleporters)) {
      //new list of teleporters that arent the current one
      List<Teleporter> otherTeleporters = new ArrayList<Teleporter>();
      //clone the active teleporter list
      for (Teleporter tp : activeTeleporters) {
        Teleporter copy = deepCopy(tp);
        otherTeleporters.add(copy);
      }
      //iterate though all teleporters and see if player is there
      for (int i = activeTeleporters.size()-1; i >= 0 ; i--) {
        if (player.playerX > activeTeleporters.get(i).getTeleporterX() && player.playerX < (activeTeleporters.get(i).getTeleporterX()+1)
            && player.playerY > activeTeleporters.get(i).getTeleporterY() && player.playerY < (activeTeleporters.get(i).getTeleporterY()+1)
            ) {
          //check all otherteleporters and choose the one needed to remove
          for (Teleporter tpp : otherTeleporters) {
            if (tpp.getTeleporterX() == activeTeleporters.get(i).getTeleporterX() &&
                tpp.getTeleporterY() == activeTeleporters.get(i).getTeleporterY()) {
              //assign the one needed to remove to outside this loop for concurecy errors
              atTeleporter = tpp;
            }
          }
        }
      }
      //remove current teleporter from the other list
      otherTeleporters.remove(atTeleporter);
      int i  = random.nextInt(activeTeleporters.size());
      //move player to random other teleporter
      player.getCam().xPos = otherTeleporters.get(i).getTeleporterX();
      player.getCam().yPos = otherTeleporters.get(i).getTeleporterY();

    }

  }

  public Teleporter deepCopy(Teleporter input) {
    Teleporter copy = new Teleporter(input.getTeleporterX(),input.getTeleporterY());
    return copy;
  }


  /**
   * sets new position when player enters teleport
   * @param r
   */
  public void setPlayerTelePos(Random r) {
    int val = r.nextInt(activeTeleporters.size());
    if(activeTeleporters.get(val).available()) {
      System.out.println(activeTeleporters.get(val).getTeleporterX());
      System.out.println(activeTeleporters.get(val).getTeleporterY());
      activeTeleporters.get(val).setActivity();
      System.out.println(activeTeleporters.get(val).available());
      this.telX = activeTeleporters.get(val).getTeleporterX();
      this.telY = activeTeleporters.get(val).getTeleporterY();
    }
  }

  /**
   * gives a new position for player when it enters a teleport
   * @return
   */
  public double getNewTeleX() {
    return this.telX;
  }
  public double getNewTeleY() {
    return this.telY;
  }

  /**
   * returns the current board
   * @return
   */
  public Board getBoard() {
    return this.board;
  }

  /**
   * sets the board - for loading
   * @param loadBoard
   */
  public void setBoard(Board loadBoard) {
    this.board = loadBoard;
  }

  /**
   * returns a map/level
   * @return
   */
  public int[][] returnMap(){
    return this.board.getMap();
  }

  /**
   * gets the player object
   * @return
   */
  public Player getPlayer() {
    return this.player;
  }

	/**
	 * Sets the timer
	 */
	public void setTimer(Timer t) {
		this.timer = t;
	}

	/**
	 * gets the timer object
	 */
	public Timer getTimer() {
		return this.timer;
	}

	/**
	 * sets the player including position and bag - for loading
	 * @param loadedPlayer
	 */
	public void setPlayer(Player loadedPlayer) {
		this.player = loadedPlayer;
	}
}
