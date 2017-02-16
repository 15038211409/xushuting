import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class NDTBFA {
	   
	   //最后一个参数是写明vm之前所在的主机的id号
	
	   int  DTBFA(List<Host> hostlist,Vm vm,List<Integer> a){
            double acmax=0,acmin=9999,eimax=0,eimin=9999,nvmax=0,nvmin=9999;
       int mips[]={3000,2500,2000,1000,500};
		   estimate power=new estimate();
		   int flag=0;//标记是否需要新建主机
		   //计算 ac ei nv
		 //  System.out.print(hostID.getHostID());
		  
		   for(int i=0;i<hostlist.size();i++)
		   {
			  
			   if(hostlist.get(i).getCapMips()<acmin)
				   acmin=hostlist.get(i).getCapMips();
			   if(hostlist.get(i).getCapMips()>acmax)
				   acmax=hostlist.get(i).getCapMips();
			   if(hostlist.get(i).getVmsMigratingIn().size()<nvmin)
				   nvmin=hostlist.get(i).getVmsMigratingIn().size();
			   if(hostlist.get(i).getVmsMigratingIn().size()>nvmax)
				   nvmax=hostlist.get(i).getVmsMigratingIn().size();
//			   if(power.estimatePower(hostlist.get(i), vm)<eimin)
//				   eimin=power.estimatePower(hostlist.get(i), vm);
//			   if(power.estimatePower(hostlist.get(i), vm)>eimax) // ei 计算不对
//				   eimax=power.estimatePower(hostlist.get(i), vm);//   
		   }
		
	      //现有主机可以满足要求时，打分
		 for(int i=0;i<hostlist.size();i++)
		 {
			 if(hostlist.get(i).getCapMips()>vm.getRequestMips())
			   
			    {	 
				   //hostlt.add(hostlist.get(i));
				 double ac,ei,nv;
				 //ac=(hostlist.get(i).getCapMips()-acmin)/(acmax-acmin);
				 if(acmin==acmax)
					 ac=1;
				 else
				 ac=Math.sqrt(Math.pow((hostlist.get(i).getCapMips()- acmin),2))/(Math.sqrt(Math.pow((hostlist.get(i).getCapMips()- acmin),2))+Math.sqrt(Math.pow((hostlist.get(i).getCapMips()- acmax),2))); 
				 if(nvmin==nvmax || hostlist.get(i).getVmsMigratingIn().size()==nvmin)
					    nv=1;
				else
//					    nv=(hostlist.get(i).getVmsMigratingIn().size()-nvmin)/(nvmax-nvmin);
				  nv=Math.sqrt(Math.pow((hostlist.get(i).getVmsMigratingIn().size()- nvmax),2))/(Math.sqrt(Math.pow((hostlist.get(i).getCapMips()- nvmax),2))+Math.sqrt(Math.pow((hostlist.get(i).getVmsMigratingIn().size()- nvmin),2))); 
//				 ei=(power.estimatePower(hostlist.get(i), vm)-eimin)/(eimax-eimin);// =min ei为0
//				 if(ei==0)
//					ei=1;  
				 if(eimax==eimin)
					ei=1;
				 else
				    ei=Math.sqrt(Math.pow((power.estimatePower(hostlist.get(i), vm)- eimax),2))/(Math.sqrt(Math.pow((power.estimatePower(hostlist.get(i), vm)- eimax),2))+Math.sqrt(Math.pow((power.estimatePower(hostlist.get(i), vm)- eimin),2))); 
				  double t=ei+ac+nv;
				   hostlist.get(i).setScore(t); 
//				 System.out.println(ac); 
//				 System.out.println(nv);
//				 System.out.println(ei);
//				 System.out.println("*************");
				   flag=1;
			    }
			 else
				 hostlist.get(i).setScore(0);
		 }
		 
		 //如果主机列表为空或者没有满足要求的主机，那么就新建主机  打分
		 if(flag==0)
	        return -1;	
	     // 排序
			  Comparator comp = new ComparatorImpll();
			  Collections.sort(hostlist, comp);
		 //选择分配主机
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
	    	//  System.out.println("runtimeruntime"+hostlist.get(i).getVmsMigratingIn().get(j).getRuntime());
	    		 for( k=0;k<hostlist.get(i).getVmsMigratingIn().get(j).getRuntime();k++)   
	    		 { 
	    			sum+=hostlist.get(i).getVmsMigratingIn().get(j).getRequestMips();
	    		//	 System.out.println("kk"+k);
	    			 sum=sum+hostlist.get(i).getVmsMigratingIn().get(j).getUt().get(k)
	    				  *mips[hostlist.get(i).getVmsMigratingIn().get(j).getType()] ;	
	    		 }
	    		// System.out.println("ID:"+hostlist.get(i).getVmsMigratingIn().get(j).getId());
	    		
	    		
	    		// System.out.println("runtime:"+hostlist.get(i).getVmsMigratingIn().get(j).getRuntime());
	    		// System.out.println("k="+k);
	    		 double temp=0;
	    		 if(k-1==0)
	    		    temp=	hostlist.get(i).getVmsMigratingIn().get(j).getUt().get(k)*mips[hostlist.get(i).getVmsMigratingIn().get(j).getType()]*0.1 ;
	    		 else if(k==0)
	    			    temp=	0 ;
	    		      else
	    		    temp=sum/(k-1)-hostlist.get(i).getVmsMigratingIn().get(j).getUt().get(k)*mips[hostlist.get(i).getVmsMigratingIn().get(j).getType()];
	    		 ai[j]=temp>0?temp:-temp;
	    		 sum2+=ai[j];
	    	}
	    	
	    	//计算avi
	    	 avi=sum2/hostlist.get(i).getVmsMigratingIn().size();
	    	//计算uti
	    	uti=1-avi*(hostlist.get(i).getVmsMigratingIn().size()+1)/2/hostlist.get(i).getTotalMips();

	    	//计算nui
            nui=(hostlist.get(i).getTotalMips()-hostlist.get(i).getCapMips()+vm.getRequestMips())/hostlist.get(i).getTotalMips();
           // System.out.println(avi);
            //System.out.println(nui);
           // System.out.println(uti);
           // System.out.println("*******************");
            if(nui<uti)
            {
               a.add(hostlist.get(i).getId());
            //hostlist.get(i).getVmsMigratingIn().add(vm);
    		//vm.setHostId(hostlist.get(i).getId());
    		//hostlist.get(i).setCapMips(hostlist.get(i).getCapMips()-vm.getRequestMips());
    
    		return 1;
            
            }
            
		}
            
	 
		
		 return -1;//没有确定分配的主机 
	  }
	   
	
}
class ComparatorImpll implements Comparator<Host> {
	public int compare(Host e1, Host e2) {
		double score1 = e1.getScore();
		double score2 = e2.getScore();
		if (score1 > score2) {
			return -1;
		} else if (score1 < score2) {
			return 1;
		} else {
			return 0;
		}
	}
}


