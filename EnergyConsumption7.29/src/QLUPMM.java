import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class QLUPMM {
   
	public int QLUPMM(List<Host> hostlist,PDM pdm)
	{
		  int mips[]={3000,2500,2000,1000,500};
		for(int i=0;i<hostlist.size();i++)
			{  

		    	double sum=0;
		    	double sum2=0;
		    	double avi=0;
		    	double ai[]=new double[5000];
		    	double uti;
		    	double nui;
		    	int k;
		    	//计算ai
		    	for(int j=0;j<hostlist.get(i).getVmsMigratingIn().size();j++)
		    	{
		             sum=0;
		    	
		    		 for( k=0;k<hostlist.get(i).getVmsMigratingIn().get(j).getRuntime()-1;k++)   
		    		 { 
		    			
		    		  sum=sum+hostlist.get(i).getVmsMigratingIn().get(j).getUt().get(k)
		    				  *mips[hostlist.get(i).getVmsMigratingIn().get(j).getType()] ;	
		    		 }
		    		 double temp=0;
		    		 if(k-1==0)
		    		 temp=	hostlist.get(i).getVmsMigratingIn().get(j).getUt().get(k)*mips[hostlist.get(i).getVmsMigratingIn().get(j).getType()]*0.1 ;
		    		 else if(k==0)
		    			 temp=0;
		    		 else
		    		 temp=sum/(k-1)-hostlist.get(i).getVmsMigratingIn().get(j).getUt().get(k)*mips[hostlist.get(i).getVmsMigratingIn().get(j).getType()];
		    		 ai[j]=temp>0?temp:-temp;
		    		 sum2+=ai[j];
		    	}
		    	
		    	//计算avi
		    	avi=sum2/hostlist.get(i).getVmsMigratingIn().size();
		    	//计算uti
		    	uti=1-avi*(hostlist.get(i).getVmsMigratingIn().size()+1)/2/hostlist.get(i).getTotalMips();
                nui=1-(hostlist.get(i).getCapMips()/hostlist.get(i).getTotalMips());
                hostlist.get(i).setDu(uti-nui);
		    	//计算nui
	            //nui=(hostlist.get(i).getTotalMips()-hostlist.get(i).getCapMips()+vm.getRequestMips())/hostlist.get(i).getTotalMips();
		
			}
		Comparator comp = new ComparatorVm();
		Collections.sort(hostlist, comp);
		NDTBFA dt=new NDTBFA();
		int flag=0;
		int j;
		int flagsign=0;
	    List<Integer> idlist= new ArrayList<Integer>();
		for(int i=0;i<hostlist.size();i++)
		{
			flagsign=0;
			Host host=new Host(hostlist.get(i));
			idlist.clear();
			//System.out.println("1111");
			for(j=0;j<hostlist.get(i).getVmsMigratingIn().size();j++)
			{	
				host.getVmsMigratingIn().add(hostlist.get(i).getVmsMigratingIn().get(j));
				//System.out.println("zzzzz"+host.getVmsMigratingIn().get(j).getRuntime());
			}   
	        hostlist.remove(hostlist.get(i));
	        for(j=0;j<host.getVmsMigratingIn().size();j++)
	        {
	        	if(dt.DTBFA(hostlist, host.getVmsMigratingIn().get(j),idlist)== -1)
	        	{
	        		//System.out.println("222");
	        		hostlist.add(i, host);
	        	    break;
	        	}
	        	
	        	
	        }
	        
			if(j==host.getVmsMigratingIn().size())
			{
			   flag=1;
	         for(int i1=0;i1<host.getVmsMigratingIn().size();i1++)
	         {
	        	 for(int i2=0;i2<idlist.size();i2++)
	        	 {
	        		 for(int i3=0;i3<hostlist.size();i3++)
	        		 {
	        			 if(hostlist.get(i3).getId()==idlist.get(i2))
	        			 {
	        				 flagsign=1;
	        				 pdm.add(host.getVmsMigratingIn().get(i1).getRequestMips()*0.1/host.getVmsMigratingIn().get(i1).getMips());
	        				 pdm.ins();
	        				 hostlist.get(i3).getVmsMigratingIn().add(host.getVmsMigratingIn().get(i1));
	        			     break;
	        			 }
	        			 
	        		 }
	        		 if(flagsign==1)
	        			 break;
	        	 }
	        	 
	         }
				
			}
		}
		
		
		if(flag==0)
		   return 0;
		else
		   {
		//	System.out.println("3333");
			return 1;
		   }
		
	}
	
}
class ComparatorVm implements Comparator<Host> {
	public int compare(Host e1, Host e2) {
		double score1 = e1.getDu();
		double score2 = e2.getDu();
		if (score1 > score2) {
			return -1;
		} else if (score1 < score2) {
			return 1;
		} else {
			return 0;
		}
	}
}