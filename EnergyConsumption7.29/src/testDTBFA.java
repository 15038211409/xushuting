import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class testDTBFA {

	 int  DTBFA(List<Host> hostlist,Vm vm,HostIDnum hostID){
         int mips[]={3000,2500,2000,1000,500};
		   int flag=0;//标记是否需要新建主机
	      //现有主机可以满足要求时，打分
		 for(int i=0;i<hostlist.size();i++)
		 {
			 if(hostlist.get(i).getCapMips()>vm.getRequestMips())
			    {	 
				 hostlist.get(i).getVmsMigratingIn().add(vm);
				 vm.setHostId(hostlist.get(i).getId());
				 hostlist.get(i).setCapMips( hostlist.get(i).getCapMips()-vm.getRequestMips());
				   flag=1;
				   return hostlist.get(i).getId();
			    }
			
		 }
		 //如果主机列表为空或者没有满足要求的主机，那么就新建主机  打分
		 if(flag==0)
		 {    
			 Random rand = new Random();	
			 int randNum = rand.nextInt(2);
			 
			   if(randNum==0)
				   hostlist.add(new Host(-1,0, 1860*2,1860*2,4*1024,0));
			   else
				   hostlist.add(new Host(-1,1, 2660*2,2660*2,4*1024,0));
				int t=hostlist.size()-1;
				hostlist.get(t).setId(hostID.getHostID());
	    		hostlist.get(t).getVmsMigratingIn().add(vm);	
	    		vm.setHostId(hostlist.get(t).getId());
	    		 hostlist.get(t).setCapMips( hostlist.get(t).getCapMips()-vm.getRequestMips());
	    		hostID.ins();
			
				return  hostlist.get(t).getId();
		 }
		 
		
		 return -1;//没有确定分配的主机 
	  }
}
