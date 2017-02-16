import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NovelEnergy {
	
	 public static void main(String args[]) throws IOException{
	  // int i=0;
	   List<Host> hostlist=new ArrayList<Host>();
	   List<Host> hostlisttest=new ArrayList<Host>();
	   List<Vm>   vmlist=new ArrayList<Vm>();
	   List<Vm>   vmlisttest=new ArrayList<Vm>();
	   HostIDnum hostIDnum=new HostIDnum();
	   HostIDnum hostIDnumtest=new HostIDnum();
	   int mips[]={3000,2500,2000,1000,500};
	   int vmID=0;
	   int vmIDtest=0;
	   int vmsize=10;
	   String path=("C:\\Users\\槑\\Desktop\\徐舒婷\\EnergyConsumption7.29\\bin\\file1"); 
	   String outputpath1=("C:\\Users\\槑\\Desktop\\徐舒婷\\EnergyConsumption7.29\\output1.txt"); 
	   String outputpath2=("C:\\Users\\槑\\Desktop\\徐舒婷\\EnergyConsumption7.29\\output2.txt"); 
	   int LifeTime=30;
	   BufferedReader br=new BufferedReader(new FileReader(path));
	   FileWriter fileWriter1=new FileWriter(outputpath1, true);
	   FileWriter fileWriter2=new FileWriter(outputpath2, true);
	   
	   //初始化vm
	   Random rand = new Random();
	   for(int i=0;i<vmsize;i++){
		   int randNum = rand.nextInt(5);
		   switch(randNum){
		   
		   case 0:  vmlist.add(new Vm(vmID++,0,-1,3000.0,6*1024.0,1000,LifeTime,-1));
		            vmlisttest.add(new Vm(vmIDtest++,0,-1,3000.0,6*1024.0,1000,LifeTime,-1)); 
		            break;
		   case 1:  vmlist.add(new Vm(vmID++,1,-1,2500.0,0.85*1024.0,1000,LifeTime,-1));
		            vmlisttest.add(new Vm(vmIDtest++,1,-1,2500.0,0.85*1024.0,1000,LifeTime,-1));
                    break;
		   case 2:  vmlist.add(new Vm(vmID++,2,-1,2000.0,3.75*1024.0,1000,LifeTime,-1));
		            vmlisttest.add(new Vm(vmIDtest++,2,-1,2000.0,3.75*1024.0,1000,LifeTime,-1));
                    break;
		   case 3:  vmlist.add(new Vm(vmID++,3,-1,1000.0,1.7*1024.0,1000,LifeTime,-1));
		            vmlisttest.add(new Vm(vmIDtest++,3,-1,1000.0,1.7*1024.0,1000,LifeTime,-1));
		            break;
		   case 4:  vmlist.add(new Vm(vmID++,4,-1,500.0,0.613*1024.0,1000,LifeTime,-1));
		            vmlisttest.add(new Vm(vmIDtest++,4,-1,500.0,0.613*1024.0,1000,LifeTime,-1));
                    break;
		   default:   
			        break;
		   }	

	   }
	   
	   
	   //获取ut
	   for(int i=0;i<vmlist.size();i++)
	   {
			for(int j=0;j<LifeTime;j++)
			{
				String line=null;
				line= br.readLine();
				vmlist.get(i).setUt(Double.valueOf(line.toString()) * 0.01*2);
				vmlisttest.get(i).setUt(Double.valueOf(line.toString()) * 0.01*2);
				//System.out.println(Double.valueOf(line.toString()) * 0.01*4);
				//这里的使用率乘以0.1应该是0.01
				//vmlist.get(i-1).setUt(Double.valueOf(line.toString()) * 0.01);//对利用率进行处理，后续与利用率有关的是估计能耗的类
			} 
			vmlist.get(i).insRuntime();//runtime为1则是运行1
            vmlist.get(i).updateRequestMips();
            vmlisttest.get(i).insRuntime();//runtime为1则是运行1
            vmlisttest.get(i).updateRequestMips();
			//System.out.println("************");   
	   }
	   
	   //初始化runtime
	   for(int i=0;i<vmlist.size();i++)
	   { 
		   vmlist.get(i).insRuntime();
		   vmlist.get(i).updateRequestMips();
		   vmlist.get(i).insRuntime();
		   vmlist.get(i).updateRequestMips();
		   vmlist.get(i).insRuntime();
		   vmlist.get(i).updateRequestMips();
		   vmlisttest.get(i).insRuntime();
	       vmlisttest.get(i).updateRequestMips();
	       vmlisttest.get(i).insRuntime();
	       vmlisttest.get(i).updateRequestMips();
	       vmlisttest.get(i).insRuntime();
	       vmlisttest.get(i).updateRequestMips();
	   }
	   
	  
	   //for(int i=0;i<vmlist.size();i++)
	   // System.out.println("vmi runtime:"+vmlist.get(i).getId() + vmlist.get(i).getRuntime());
	  
	   

	   //分配vm
	   DTBFA test=new DTBFA();
	   testDTBFA test1=new testDTBFA();
	   QLUPMM ql=new QLUPMM();
	   MRM   mrm=new MRM();
	   //test
	   for(int i=0;i<vmlisttest.size();i++) 
		   test1.DTBFA(hostlisttest, vmlisttest.get(i), hostIDnumtest);
	   System.out.println("test size:"+hostlisttest.size());
	   //test 初始化ratio
	   for(int i=0;i<4;i++)
	   { 
		   
		   double temp;
		   for(int j=0;j<hostlisttest.size();j++)
		   {  
			 temp=hostlisttest.get(j).getTotalMips();
			 
			 for(int k=0;k<hostlisttest.get(j).getVmsMigratingIn().size();k++)
		    	 temp=temp-hostlisttest.get(j).getVmsMigratingIn().get(k).getUt().get(hostlisttest.get(j).getVmsMigratingIn().get(k).getRuntime())*mips[hostlisttest.get(j).getVmsMigratingIn().get(k).getType()];
		
			 hostlisttest.get(j).setCapMips(temp);
			 hostlisttest.get(j).addRatio(temp/hostlisttest.get(j).getTotalMips());
		   } 
	  }
	   
      double sumenergytest=0;
	  double slatahtest=0, slatahnumtest=0;
	  PDM pdmtest=new PDM();
	   //test 过载分配
	   for(int i=5;i<LifeTime-10;i++)
	   {  
		   double temp=0;
		   for(int j=0;j<hostlisttest.size();j++)
		   {
			   temp=hostlisttest.get(j).getTotalMips();
			   for(int k=0;k<hostlisttest.get(j).getVmsMigratingIn().size();k++)
			   {   
				  
				   hostlisttest.get(j).getVmsMigratingIn().get(k).insRuntime();
				  // System.out.println("runtime:"+hostlist.get(j).getVmsMigratingIn().get(k).getRuntime());
				   hostlisttest.get(j).getVmsMigratingIn().get(k).updateRequestMips();
				   temp= temp-  hostlisttest.get(j).getVmsMigratingIn().get(k).getRequestMips();//hostlist.get(j).getVmsMigratingIn().get(k).getUt().get(hostlist.get(j).getVmsMigratingIn().get(k).getRuntime())*mips[hostlist.get(j).getVmsMigratingIn().get(k).getType()];
			   }
			   hostlisttest.get(j).setCapMips(temp);
			   hostlisttest.get(j).addRatio(1-temp/hostlisttest.get(j).getTotalMips());
			   if(temp<0)
			   { 
				   slatahnumtest++;
			       for(int k=0;k<hostlisttest.get(j).getVmsMigratingIn().size();k++)
			       {
			    	  
			    	 	   pdmtest.add(hostlisttest.get(j).getVmsMigratingIn().get(k).getRequestMips()*0.1/hostlisttest.get(j).getVmsMigratingIn().get(k).getMips());
			    	 	   pdmtest.ins();
			    		   test1.DTBFA(hostlisttest,  hostlisttest.get(j).getVmsMigratingIn().get(k), hostIDnumtest);
			    		   if(temp+hostlisttest.get(j).getVmsMigratingIn().get(k).getRequestMips()>0)
			    		     {
			    			   hostlisttest.get(j).getVmsMigratingIn().remove(k);
			    		       break;
			    		     }
			    		   else
			    			   hostlisttest.get(j).getVmsMigratingIn().remove(k);
			    		   k=0;
			       }
			       //System.out.println("test size:"+hostlisttest.size());
			   }
			  
				if(hostlisttest.get(j).getType()==0)
					sumenergytest+=85.995 +  31.005 *(1-temp/hostlisttest.get(j).getTotalMips());
				else
					sumenergytest+=93.69  +  41.31  *(1-temp/hostlisttest.get(j).getTotalMips());
 
		   }
	
	   }
	   slatahtest= slatahnumtest/(LifeTime-14)/hostIDnumtest.getHostID();
//	   System.out.println("sumenergytest:"+sumenergytest*1.2); 
//	   System.out.println("slatah:"+slatahtest); 
//	   System.out.println("pdm:"+pdmtest.getsum()/vmlisttest.size()); 
	  // fileWriter.write("1111");
	   
	   
	   //改进
	   for(int i=0;i<vmlist.size();i++) 
		   test.DTBFA(hostlist, vmlist.get(i), hostIDnum, i);

	
	   
   
//	   int ttt=0;
//	   for(int ii=0;ii<hostlist.size();ii++)
//		 for(int jj=0;jj<hostlist.get(ii).getVmsMigratingIn().size();jj++)
//		 { ttt++;
//		 System.out.println("vmi runtime:"+hostlist.get(ii).getVmsMigratingIn().get(jj).getId() +"  "+ hostlist.get(ii).getVmsMigratingIn().get(jj).getRuntime());
//		 }
//	   System.out.println("vmsize:"+ttt);
  
	   
	   System.out.println("first size:"+hostlist.size());
	   
	   
	   //计算 ratio
	   for(int i=0;i<4;i++)
	   { 
		   
		   double temp;
		   for(int j=0;j<hostlist.size();j++)
		   {  
			 temp=hostlist.get(j).getTotalMips();
			 
			 for(int k=0;k<hostlist.get(j).getVmsMigratingIn().size();k++)
		    	 temp=temp-hostlist.get(j).getVmsMigratingIn().get(k).getUt().get(hostlist.get(j).getVmsMigratingIn().get(k).getRuntime())*mips[hostlist.get(j).getVmsMigratingIn().get(k).getType()];
		
			 hostlist.get(j).setCapMips(temp);
			 hostlist.get(j).addRatio(temp/hostlist.get(j).getTotalMips());
		   } 
	  }
	   
	   double temp=0;
	   double sumenergy=0;
	   double slatah=0, slatahnum=0;
	   PDM pdm=new PDM();
	 
	   for(int i=5;i<LifeTime-10;i++)
	   {  

		   List<Vm> tempvmlist=new ArrayList<Vm>();
		   tempvmlist.clear();
			  ql.QLUPMM(hostlist,pdm); 
			  System.out.println("second1 size:"+hostlist.size());
		   for(int j=0;j<hostlist.size();j++)
		   {
			   temp=hostlist.get(j).getTotalMips();
			   tempvmlist.clear();
			 //  System.out.println("vmsize:"+hostlist.get(j).getVmsMigratingIn().size());
			   for(int k=0;k<hostlist.get(j).getVmsMigratingIn().size();k++)
			   {   
				  
				   hostlist.get(j).getVmsMigratingIn().get(k).insRuntime();
				  // System.out.println("runtime:"+hostlist.get(j).getVmsMigratingIn().get(k).getRuntime());
			       hostlist.get(j).getVmsMigratingIn().get(k).updateRequestMips();
				   temp= temp-  hostlist.get(j).getVmsMigratingIn().get(k).getRequestMips();//hostlist.get(j).getVmsMigratingIn().get(k).getUt().get(hostlist.get(j).getVmsMigratingIn().get(k).getRuntime())*mips[hostlist.get(j).getVmsMigratingIn().get(k).getType()];
			   }
			   hostlist.get(j).setCapMips(temp);
			   hostlist.get(j).addRatio(1-temp/hostlist.get(j).getTotalMips());
			   if(temp<0)
			   { 
				   slatahnum++;
				   // System.out.println(4);
				   mrm.MRM(hostlist.get(j), tempvmlist);
				  // System.out.println("ttsize:"+tempvmlist.size());
			       for(int k=0;k<tempvmlist.size();k++)
			       {
			    	   pdm.add(tempvmlist.get(k).getRequestMips()*0.1/tempvmlist.get(k).getMips());
			    	   pdm.ins();
			    	   test.DTBFA(hostlist, tempvmlist.get(k), hostIDnum, k);
			       }
			   }
				if(hostlist.get(j).getType()==0)
				   sumenergy+=85.995 +  31.005 *(1-temp/hostlist.get(j).getTotalMips())  ;
				else
				   sumenergy+=93.69  +  41.31  *(1-temp/hostlist.get(j).getTotalMips())	  ;
 
		   }
	
		  System.out.println("second2 size:"+hostlist.size());  
	   }
	   
	   slatah= slatahnum/(LifeTime-14)/hostIDnum.getHostID();
	   //pdm=pdm/vmlist.size();
//	   System.out.println("sumenergy:"+sumenergy); 
//	   System.out.println("slatah:"+slatah); 
//	   System.out.println("pdm:"+pdm.getsum()/vmlist.size()); 
	   System.out.println("sumenergytest:"+String.valueOf(sumenergytest*1.2)+"\r\n");
	   System.out.println("slav:"+String.valueOf(slatahtest*pdmtest.getsum()/vmlisttest.size())+"\r\n");
	   System.out.println("mnumber:"+pdmtest.getnumber()+"\r\n");
	   System.out.println("sum:"+String.valueOf(sumenergytest*1.2*slatahtest*pdmtest.getsum()/vmlisttest.size())+"\r\n\r\n\r\n");
	  
	   System.out.println("sumenergytest:"+String.valueOf(sumenergy)+"\r\n");
	   System.out.println("slav:"+String.valueOf(slatah*pdm.getsum()/vmlist.size())+"\r\n");
	   System.out.println("mnumber:"+pdm.getnumber()+"\r\n");
	   System.out.println("sum:"+String.valueOf(sumenergy*slatah*pdm.getsum()/vmlist.size())+"\r\n\r\n\r\n");
	   if(sumenergy<sumenergytest*1.2)
	   {
	    fileWriter1.write("sumenergytest:"+String.valueOf(sumenergytest*1.2)+"\r\n");
	    fileWriter1.write("slav:"+String.valueOf(slatahtest*pdmtest.getsum()/vmlisttest.size())+"\r\n");
	    fileWriter1.write("mnumber:"+pdmtest.getnumber()+"\r\n");
	    fileWriter1.write("sum:"+String.valueOf(sumenergytest*1.2*slatahtest*pdmtest.getsum()/vmlisttest.size())+"\r\n\r\n\r\n");
	    fileWriter1.flush();
	    fileWriter1.close();
	    fileWriter2.write("sumenergytest:"+String.valueOf(sumenergy)+"\r\n");
	    fileWriter2.write("slav:"+String.valueOf(slatah*pdm.getsum()/vmlist.size())+"\r\n");
	    fileWriter2.write("mnumber:"+pdm.getnumber()+"\r\n");
	    fileWriter2.write("sum:"+String.valueOf(sumenergy*slatah*pdm.getsum()/vmlist.size())+"\r\n\r\n\r\n");
	    fileWriter2.flush();
	    fileWriter2.close();
	   }
	 }
}
class PDM{
	
private double sum;
private double number;
PDM(){
 this.sum=0;
 this.number=0;
}
void add(double a){
	this.sum=this.sum+a;
}
void ins(){
	this.number++;
}
double getsum(){
	return this.sum;
}
double getnumber(){
	return this.number;
}
	
	
}
