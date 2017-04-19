/**
 * Class Ressource
 * Modelize the ressources in the game
 **/

import java.io.Serializable;

public class Ressource implements Serializable
{
    // Attributes
    // private
    private final int type;                   // Type of ressource
    private int totalAmount;                  // Amount of items of this ressource
    private final int amountForVictory;       // Amount of collected ressource needed to win for this kind of ressource
    static final long serialVersionUID = 42;  // id for rmi use

    // Constructor
    public Ressource(int type0, int amount0, int amountForV)
    {
      this.type = type0;
      this.totalAmount = amount0;
      this.amountForVictory = amountForV;
    }

    // copy
    public Ressource copy()
    {
      Ressource copy0 = new Ressource(this.type, this.totalAmount, this.amountForVictory);
      return copy0;
    }

    // Methods

    /**
     * Method : getType
     * Param : void
     * Desc : Return the type of the current ressource
     * Return : int, type of the ressource
     **/
    public int getType()
    {
      return type;
    }


    /**
     * Method : getAmount
     * Param : void
     * Desc : Return the current amount of the current ressource
     * Return : int, amount of the ressource
     **/
    synchronized int getAmount()
    {
      return this.totalAmount;
    }


    /**
     * Method : addRessource
     * Param : int, amountAdded - amount to add to the current amount of the ressource
     * Desc : Add the specified amount to the total amount of the ressource
     * Return : boolean, true if the totalAmount == amountForVictory, else false
                used for player to know if he reached the objectif
     **/
    synchronized boolean addRessource(int amountAdded)
    {
      this.totalAmount += amountAdded;
      if(this.totalAmount >= this.amountForVictory)
      {
        return true;
      }
      return false;
    }


    /**
     * Method : RmvAmount
     * Param : int, amountTaken - amount to take from the totalAmount of the ressource
     * Desc : Remove the specified amount from the totalAmount of the ressource
     * Return : int, the amount really taken from the total Amount;
     **/
    synchronized int rmvRessource(int amountTaken)
    {
      int realAmountTaken = this.totalAmount;

      this.totalAmount -= amountTaken;
      if(this.totalAmount <= 0) // If we try to take more ressources than available
      {
        this.totalAmount = 0;
      }
      else
      {
        realAmountTaken = amountTaken;
      }

      return realAmountTaken; // Return the real amount that is taken from the totalAmount
    }


    /**
     * Method : amountForVictoryIsReached
     * Param : void
     * Desc : Test if the totalAmount is equal to the amount to reach for this
              ressource - used by players
     * Return : boolean true if the amount is reached, else false
     **/
     public boolean amountForVictoryIsReached()
     {
       return (this.totalAmount >= this.amountForVictory);
     }

}
