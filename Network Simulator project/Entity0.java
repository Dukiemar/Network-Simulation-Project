public class Entity0 extends Entity
{    
    //Declaration an array of integers to hold the the nodes that are neighbours to entity0
    protected int[] directNeighbours= new int [4];
    //Declaration an array to hold the minimum cost from entity0 to the other entities
    protected int[] minCost= new int[4];
    // Perform any necessary initialization in the constructor
    public Entity0()
    {
        //Initialize distance table to infinity
    	for (int i = 0; i < 4; i++)
    	{
    		for (int j = 0; j < 4; j++)
    		{
    			distanceTable[i][j] = 999;
    			//System.out.println("Distance[" + i +"]["+j+"]=" + distanceTable[i][j]);
    		}
    	}
    	
        //Assignment of the respective neighbouring nodes to the indexes in the directNeighbour array
        directNeighbours[0]=1;
        directNeighbours[1]=2;
        directNeighbours[2]=3;
        
        //this is the distance table containing all the values for the distances to immediate neighbours
        this.distanceTable[0][0] = 0;
        this.distanceTable[1][0] = 1;
        this.distanceTable[2][0] = 3;
        this.distanceTable[3][0] = 7;
        //values assigned the the minCost array which repersents the minimum cost to each node
        for (int i = 0; i < 4; i++)
    	{
    		int currentMin = 999;
    		for (int j = 0; j < 4; j++)
    		{
    			if (distanceTable[i][j] < currentMin)
    			{
    				currentMin = distanceTable[i][j];
    			}
    		}
    		minCost[i] = currentMin;
    		System.out.println("Initial Mincost Entity0= "+minCost[i]);
    	}
    	System.out.println();
        
        /* The final minimum Cost values for this entity should be:
         * minCost[0] = 0;
         * minCost[1] = 1;
         * minCost[2] = 2;
         * minCost[3] = 4;
        */
       
        //assign the packets to be sent to the neighbours 
        Packet toNeighbour_1 = new Packet(0, 1, minCost);
        Packet toNeighbour_2 = new Packet(0, 2, minCost);
        Packet toNeighbour_3 = new Packet(0, 3, minCost);
        //call the toLayer2 function to send each packet
        NetworkSimulator.toLayer2(toNeighbour_1);
        NetworkSimulator.toLayer2(toNeighbour_2);
        NetworkSimulator.toLayer2(toNeighbour_3);
        
        
    }
    
    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    
    //This function ensures that the packets are coming from neighbours and the simulator is not sendin wrong packets
    public boolean isNeighbour(int x)
    {
        for (int i=0 ;i <= directNeighbours.length;i++)
        {
            if(x==directNeighbours[i])
            {
                return true;
            }
        }
        return false;
    }
    //This function updates the min costs in the diistance table when packets are received from other nodes
    public void update(Packet p)
    {        
        int p_src=p.getSource();
        int p_dest= p.getDest();
        boolean isUpdated=false;
        
        if((p_dest==0) && isNeighbour(p_src)==true)//account for the issue in the simulator were packets are sent to non neighbours.
        {
            for(int x = 0; x < minCost.length; x++)
            {
    		   //Update the distance table if the node cost changes
    		    if(distanceTable[x][p_src] > p.getMincost(x) + minCost[p_src])
    		    {
    			   this.distanceTable[x][p_src] = p.getMincost(x) + minCost[p_src];
    			   
    			   //Check to see if this new cost is now the minimum cost
    			   if ((p.getMincost(x) + minCost[p_src]) < minCost[x])
    			   {
    				 minCost[x] = p.getMincost(x) + minCost[p_src];
    				 isUpdated = true;
    				 //This loop updates and print the min costs table
            		 for (int i = 0; i < 4; i++)
            			{
            			    int currentMin = 999;
            			    for (int j = 0; j < 4; j++)
            			    {
            			       if (distanceTable[i][j] < currentMin)
            			       {
            			          currentMin = distanceTable[i][j];
            			       }
            			    }
            			    minCost[i] = currentMin;
            			    System.out.println("Updated Mincost Entity0= "+ minCost[i]);
                       }
                       
    				}
   
               }
            }
        }
        else
        {
            System.out.println("emulator error detected");
        }
        //if packets are updates then they are sent to the directly connected neighbours.
        if(isUpdated==true)
            {
                //create the packets to be sent with the source destination and mincost
                Packet toNeighbour_1 = new Packet(0, 1, minCost);
                Packet toNeighbour_2 = new Packet(0, 2, minCost);
                Packet toNeighbour_3 = new Packet(0, 3, minCost);
                //call the toLayer2 function to send the packets.
                NetworkSimulator.toLayer2(toNeighbour_1);
                NetworkSimulator.toLayer2(toNeighbour_2);
                NetworkSimulator.toLayer2(toNeighbour_3);
      
            }
            System.out.println();
      }
    
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }
    
    public void printDT()
    {
        System.out.println();
        System.out.println("           via");
        System.out.println(" D0 |   1   2   3");
        System.out.println("----+------------");
        for (int i = 1; i < NetworkSimulator.NUMENTITIES; i++)
        {
            System.out.print("   " + i + "|");
            for (int j = 1; j < NetworkSimulator.NUMENTITIES; j++)
            {
                if (distanceTable[i][j] < 10)
                {    
                    System.out.print("   ");
                }
                else if (distanceTable[i][j] < 100)
                {
                    System.out.print("  ");
                }
                else 
                {
                    System.out.print(" ");
                }
                
                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}
