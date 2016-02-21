public class Entity3 extends Entity
{    
    //Declaration on an array of integers to hold the the nodes that are neighbours to entity0
    protected int[] directNeighbours= new int [4];
    //Declaration on an array to hold the minimum cost from entity0 to the other entities
    protected int[] minCost= new int[NetworkSimulator.NUMENTITIES];
    // Perform any necessary initialization in the constructor
    public Entity3()
    {
         //Initialize distance table to infinity
    	for (int i = 0; i < 4; i++)
    	{
    		for (int j = 0; j < 4; j++)
    		{
    			distanceTable[i][j] = 999;
    		}
    	}
    	
    	//Assignment of the respective neighbouring nodes to the indexes in the directNeighbour array
        directNeighbours[0]=0;
        directNeighbours[1]=2;
        
        this.distanceTable[0][3] = 7;
        this.distanceTable[2][3] = 2;
        this.distanceTable[3][3] = 0;
        
        //values assigned the the minCost array which repersents the minimum cost to each node
        for (int i = 0; i < 4; i++)
    	{
    		int infinity = 999;
    		for (int j = 0; j < 4; j++)
    		{
    			if (distanceTable[i][j] < infinity)
    			{
    				infinity = distanceTable[i][j];
    			}
    		}
    		minCost[i] = infinity;
    		System.out.println("Initial Mincost Entity3= "+minCost[i]);
    	}
        
    	/*The final minimum Cost values for this entity should be:
         * minCost[0] = 4;
         * minCost[1] = 3;
         * minCost[2] = 2;
         * minCost[3] = 0;
        */
       //create the packets to be sent with the source destination and mincost
        Packet toNeighbour_0 = new Packet(3,0,minCost);
        Packet toNeighbour_2 = new Packet(3,2,minCost);
        //call the toLayer2 function to send the packets.
        NetworkSimulator.toLayer2(toNeighbour_0);
        NetworkSimulator.toLayer2(toNeighbour_2);
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
    
    public void update(Packet p)
    {
        
        int p_src=p.getSource();
        int p_dest= p.getDest();
        //int newMinCst[]=new int[NetworkSimulator.NUMENTITIES];
        boolean isUpdated=false;
       
        if((p_dest==3) && isNeighbour(p_src)==true)//account for the issue in the simulator were packets are sent to non neighbours.
        {
            for(int x = 0; x < minCost.length; x++)
            {
    		   //Update the distance table if the node cost changes
    		    if(distanceTable[x][p_src] > p.getMincost(x) + minCost[p_src])
    		    {
    			   this.distanceTable[x][p_src] = p.getMincost(x) + minCost[p_src];
    			   //Check to see if this new cost is now the minimum cost
    			   if (p.getMincost(x) + minCost[p_src] < minCost[x])
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
                		System.out.println("Updated Mincost Entity3= "+minCost[i]);
                     }
    				 //System.out.println("Initial Mincost Entity3= "+minCost[x]);
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
                Packet toNeighbour_0 = new Packet(3,0,minCost);
                Packet toNeighbour_2 = new Packet(3,2,minCost);
                //call the toLayer2 function to send the packets.  
                NetworkSimulator.toLayer2(toNeighbour_0);
                NetworkSimulator.toLayer2(toNeighbour_2);
            }
        
    }
    
    public void linkCostChangeHandler(int whichLink, int newCost)
    {
    }
    
    public void printDT()
    {
        System.out.println("         via");
        System.out.println(" D3 |   0   2");
        System.out.println("----+--------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++)
        {
            if (i == 3)
            {
                continue;
            }
            
            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j += 2)
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
