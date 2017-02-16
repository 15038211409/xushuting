import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class DTBFA {
	   
	   //���һ��������д��vm֮ǰ���ڵ�������id��
	
	   int  DTBFA(List<Host> hostlist,Vm vm,HostIDnum hostID,int id){
            double acmax=0,acmin=9999,eimax=0,eimin=9999,nvmax=0,nvmin=9999;
       int mips[]={3000,2500,2000,1000,500};
		   estimate power=new estimate();
		   int flag=0;//����Ƿ���Ҫ�½�����
		   //���� ac ei nv
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
			   if(power.estimatePower(hostlist.get(i), vm)<eimin)
				   eimin=power.estimatePower(hostlist.get(i), vm);
			   if(power.estimatePower(hostlist.get(i), vm)>eimax) // ei ���㲻��
				   eimax=power.estimatePower(hostlist.get(i), vm);//   
		   }
		
	      //����������������Ҫ��ʱ�����
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
//				 ei=(power.estimatePower(hostlist.get(i), vm)-eimin)/(eimax-eimin);// =min eiΪ0
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
		 //��������б�Ϊ�ջ���û������Ҫ�����������ô���½�����  ���
		 if(flag==0)
		 {    
			// hostID.ins();
			 acmax=0;acmin=9999;
			 eimax=0;eimin=9999;
			 nvmax=0;nvmin=9999;
	
			// hostlist.add(new Host(-1,2, 2933*4,2933*4,8*1024,0));
			// hostlist.add(new Host(-1,3, 3067*12,3067*12,16*1024,0));
			 Random rand = new Random();	
			 int randNum = rand.nextInt(2);
			 
			   if(randNum==0)
				   hostlist.add(new Host(-1,0, 1860*2,1860*2,4*1024,0));
			   else
				   hostlist.add(new Host(-1,1, 2660*2,2660*2,4*1024,0));
				int t=hostlist.size()-1;
				hostlist.get(t).setId(hostID.getHostID());
	    		hostlist.get(t).getVmsMigratingIn().add(vm);
	    		hostlist.get(t).setCapMips(hostlist.get(t).getCapMips()-vm.getRequestMips());
	    		vm.setHostId(hostlist.get(t).getId());
	    	
	    		hostID.ins();
			
				return  hostlist.get(t).getId();
		 }
	
  
		
		
	     // ����
			  Comparator comp = new ComparatorImpl();
			  Collections.sort(hostlist, comp);
		 //ѡ���������
	    for(int i=0;i<hostlist.size();i++)
		{
	    	
	    	
	    	double sum=0;
	    	double sum2=0;
	    	double avi=0;
	    	double ai[]=new double[5000];
	    	double uti;
	    	double nui;
	    	int k;
	    	//����ai
	    	for(int j=0;j<hostlist.get(i).getVmsMigratingIn().size();j++)
	    	{
	             sum=0;
	    	    // System.out.println("runtime"+hostlist.get(i).getVmsMigratingIn().get(j).getRuntime());
	    		 for( k=0;k<hostlist.get(i).getVmsMigratingIn().get(j).getRuntime();k++)   
	    		 { 
	    			 // System.out.println("k"+k);
	    		  sum=sum+hostlist.get(i).getVmsMigratingIn().get(j).getUt().get(k)
	    				  *mips[hostlist.get(i).getVmsMigratingIn().get(j).getType()] ;	
	    		 }
	    		// System.out.println("hostID:"+hostlist.get(i).getVmsMigratingIn().get(j).getId());
	    		
	    		
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
	    	
	    	//����avi
	    	 avi=sum2/hostlist.get(i).getVmsMigratingIn().size();
	    	//����uti
	    	uti=1-avi*(hostlist.get(i).getVmsMigratingIn().size()+1)/2/hostlist.get(i).getTotalMips();

	    	//����nui
            nui=(hostlist.get(i).getTotalMips()-hostlist.get(i).getCapMips()+vm.getRequestMips())/hostlist.get(i).getTotalMips();
           // System.out.println(avi);
            //System.out.println(nui);
           // System.out.println(uti);
           // System.out.println("*******************");
            if(nui<uti)
            {
            //System.out.println(222);
            //System.out.println("*******************");
        	hostlist.get(i).getVmsMigratingIn().add(vm);
    		//vm.setId(id);
    		vm.setHostId(hostlist.get(i).getId());
    		hostlist.get(i).setCapMips(hostlist.get(i).getCapMips()-vm.getRequestMips());
    		//hostlist.get(i).addRatio((hostlist.get(i).getCapMips()-vm.getRequestMips())/hostlist.get(i).getTotalMips());
    		// vm.insRuntime();
    		// vm.updateRequestMips();
    		for(int m=0;m<hostlist.size();m++)
    			   if(hostlist.get(m).getId()== -1)
    				  hostlist.remove(hostlist.get(m));
    		return hostlist.get(i).getId();
            }
            else
            {
            	List<Host> hostlist1=new ArrayList<Host>();
           	 acmax=0;acmin=9999;
			 eimax=0;eimin=9999;
			 nvmax=0;nvmin=9999;
			 
			 hostlist1.add(new Host(-1,0, 1860*2,1860*2,4*1024,0));
			 hostlist1.add(new Host(-1,1, 2660*2,2660*2,4*1024,0));
			// hostlist.add(new Host(-1,2, 2933*4,2933*4,8*1024,0));
			// hostlist.add(new Host(-1,3, 3067*12,3067*12,16*1024,0));
			   for(int ii=0;ii<hostlist1.size();ii++)
			   {
				   if(hostlist1.get(ii).getCapMips()<acmin)
					   acmin=hostlist1.get(ii).getCapMips();
				   if(hostlist1.get(ii).getCapMips()>acmax)
					   acmax=hostlist1.get(ii).getCapMips();
				   if(hostlist1.get(ii).getVmsMigratingIn().size()<nvmin)
					   nvmin=hostlist1.get(ii).getVmsMigratingIn().size();
				   if(hostlist1.get(ii).getVmsMigratingIn().size()>nvmax)
					   nvmax=hostlist1.get(ii).getVmsMigratingIn().size();
				   if(power.estimatePower(hostlist1.get(ii), vm)<eimin)
					   eimin=power.estimatePower(hostlist1.get(ii), vm);
				   if(power.estimatePower(hostlist1.get(ii), vm)>eimax)
					   eimax=power.estimatePower(hostlist1.get(ii), vm);
				   
			   }

				 for(int jj=0;jj<hostlist1.size();jj++)
				 {
					 if(hostlist1.get(jj).getCapMips()>vm.getRequestMips())
					   
					    {	 
						   //hostlt.add(hostlist.get(i));
						 double ac,ei,nv;
						 ac=Math.sqrt(Math.pow((hostlist1.get(jj).getCapMips()- acmin),2))/(Math.sqrt(Math.pow((hostlist1.get(jj).getCapMips()- acmin),2))+Math.sqrt(Math.pow((hostlist1.get(jj).getCapMips()- acmax),2))); 
						 if(nvmin==nvmax || hostlist1.get(jj).getVmsMigratingIn().size()==nvmin)
							    nv=1;
						else
//							    nv=(hostlist.get(i).getVmsMigratingIn().size()-nvmin)/(nvmax-nvmin);
						  nv=Math.sqrt(Math.pow((hostlist1.get(jj).getVmsMigratingIn().size()- nvmax),2))/(Math.sqrt(Math.pow((hostlist1.get(jj).getCapMips()- nvmax),2))+Math.sqrt(Math.pow((hostlist1.get(jj).getVmsMigratingIn().size()- nvmin),2))); 
//						 ei=(power.estimatePower(hostlist.get(i), vm)-eimin)/(eimax-eimin);// =min eiΪ0
//						 if(ei==0)
//							ei=1;  
						  ei=Math.sqrt(Math.pow((power.estimatePower(hostlist1.get(jj), vm)- eimax),2))/(Math.sqrt(Math.pow((power.estimatePower(hostlist1.get(jj), vm)- eimax),2))+Math.sqrt(Math.pow((power.estimatePower(hostlist1.get(jj), vm)- eimin),2))); 
						  double t=ei+ac+nv;
						 hostlist1.get(jj).setScore(t);
			
//						 System.out.println(ac); 
//						 System.out.println(nv);
//						 System.out.println(ei);
//						 System.out.println("*************");
					
					    }
					 else
						 hostlist1.get(i).setScore(0);
				 }
				 Collections.sort(hostlist1, comp);
				 hostlist.add(hostlist1.get(0));
				 hostlist.get(hostlist.size()-1).setId(hostID.getHostID());
				 hostlist.get(hostlist.size()-1).getVmsMigratingIn().add(vm);
				// hostlist.get(hostlist.size()-1).addRatio((hostlist.get(hostlist.size()-1).getCapMips()-vm.getRequestMips())/hostlist.get(hostlist.size()-1).getTotalMips());
		    		//vm.setId(id);
		    		vm.setHostId(hostlist.get(hostlist.size()-1).getId());
		    	   // vm.insRuntime();
		    	  //  vm.updateRequestMips();
		    		 hostlist.get(hostlist.size()-1).setCapMips( hostlist.get(hostlist.size()-1).getCapMips()-vm.getRequestMips());
		    		hostID.ins();
		    		return  hostlist.get(hostlist.size()-1).getId();
			    	
            	
            }
            
		}

	 
		
		 return -1;//û��ȷ����������� 
	  }
	   
	
}
class ComparatorImpl implements Comparator<Host> {
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


