import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MRM {
	public int MRM(Host host,List<Vm> vmlist)
	{
		int mips[]={3000,2500,2000,1000,500};
		double ai[]=new double[5000];//这个ai是用来计算上界的vm资源变化的，
		double ar[]=new double[5000];//这个ar是用来计算vm的平均资源请求的
		double sum2=0;
		double avi;
		double uti;
		for(int i=0;i<host.getVmsMigratingIn().size();i++)
    	{
             double sum=0;
    	     int j;
    	    // System.out.println("runtime:"+host.getVmsMigratingIn().get(i).getRuntime());
    		 for( j=0;j<host.getVmsMigratingIn().get(i).getRuntime()-1;j++)   
    		 { 
    		//	 System.out.println("j:"+j);
    		  sum=sum+host.getVmsMigratingIn().get(i).getUt().get(j)
    				  *mips[host.getVmsMigratingIn().get(i).getType()] ;	
    		 }
    		 double temp=0;
    		 if(j-1==0)
    		 temp=host.getVmsMigratingIn().get(i).getUt().get(j)*mips[host.getVmsMigratingIn().get(i).getType()]*0.1 ;
     		 else if(j==0)
     			 temp=0;
     		 else
    		 temp=sum/(j-1)-host.getVmsMigratingIn().get(i).getUt().get(j)*mips[host.getVmsMigratingIn().get(i).getType()];
    		 ai[i]=temp>0?temp:-temp;
    		 sum2+=ai[i];
    		 
    	
    		 sum=0;
    		 for( j=0;j<host.getVmsMigratingIn().get(i).getRuntime();j++)   
    		 { 
    			
    		  sum=sum+host.getVmsMigratingIn().get(i).getUt().get(j)
    				  *mips[host.getVmsMigratingIn().get(i).getType()] ;	
    		 }
    		 ar[i]=sum/j;
    		 host.getVmsMigratingIn().get(i).setaverRe(ar[i]);
    	
    	}
		avi=sum2/host.getVmsMigratingIn().size();
		uti=1-avi*(host.getVmsMigratingIn().size()+1)/2/host.getTotalMips();
		Comparator compp = new ComparatorVm();
		Collections.sort(host.getVmsMigratingIn(), compp); 
	//	System.out.println("uti"+uti);
		for(int i=0;i<host.getVmsMigratingIn().size();i++)
		{
			//Vm vm=new Vm(host.getVmsMigratingIn().get(i));
		    vmlist.add(host.getVmsMigratingIn().get(i));
			host.getVmsMigratingIn().remove(i);
			if(SeondExponentialSmoothing(host)<uti)
			{
				//vmlist.add(vm);
				return 1;
			}
			else
			{ 
				host.getVmsMigratingIn().add(i, vmlist.get(vmlist.size()-1));
				 vmlist.remove(vmlist.size()-1);
			}
			if(i==host.getVmsMigratingIn().size()-1)
			{
				vmlist.add(host.getVmsMigratingIn().get(0));
				host.getVmsMigratingIn().remove(0);
				i=0;
			}
			
		}
	
		
		return 0;//这里为什么要返回数值

	}
	class ComparatorVm implements Comparator<Vm> {
		public int compare(Vm e1, Vm e2) {
			double score1 = e1.getaverRe();
			double score2 = e2.getaverRe();
			if (score1 > score2) {
				return -1;
			} else if (score1 < score2) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	double SeondExponentialSmoothing(Host host)//二次指数平滑法     依次去掉vm的host然后看变化 
	{
		double exSmooth;//计算出的平滑值
		double ratio=0;//host的利用率
		double a,a0=0,b,b0=1,y=0.618,x1,x2;//y表示拉姆，精度为多少
		double Q1,Q2,middle=0;//用来记录MSE函数的计算值
		
		List<Double> d= new ArrayList<Double>();//d为一系列预测出来的n及n以前的所有时刻的预测值
		int n;//用来记录host中利用率次数
		
		//这里可以不用计算平滑初值	
		a=a0;b=b0;
		x1=a+y*(b-a);
		x2=b-y*(b-a);
		
	    foreCast(d,x1,host); //采用一个d进行计算，每次new的时候需要考虑。是不是会发生指向错误    
	    //System.out.println(d.size());
	//    System.out.println("d size is");
	    for(int i=2;i<host.getRatio().size();i++)
		{
			middle+=Math.pow((host.getRatio().get(i)-d.get(i-2)),2);
		}
		Q1=middle/(host.getRatio().size()-2);//q为MSE的计算结果
		
		foreCast(d,x2,host);	
		middle=0;//注意一些应用值的初始化处理
		
		for(int i=2;i<host.getRatio().size();i++)
			{
				middle+=Math.pow((host.getRatio().get(i)-d.get(i-2)),2);
			}
		Q2=middle/(host.getRatio().size()-2 );	
		middle=0;
		
		if(Q1<Q2)
		{
			b=x2;
			x2=x1;
			x1=b-y*(b-a);
			Q2=Q1;
			foreCast(d,x1,host); 
			 for(int i=2;i<host.getRatio().size();i++)
				{
					middle+=Math.pow((host.getRatio().get(i)-d.get(i-2)),2);
				}
				Q1=middle/(host.getRatio().size()-2);//q为MSE的计算结果
				middle=0;
		}
		else
		{
			a=x2;
			x1=x2;
			x2=a+y*(b-a);
			Q1=Q2;
			foreCast(d,x2,host); 
			 for(int i=2;i<host.getRatio().size();i++)
				{
					middle+=Math.pow((host.getRatio().get(i)-d.get(i-2)),2);
				}
				Q2=middle/(host.getRatio().size()-2);//q为MSE的计算结果
				middle=0;
		}
		
		
		while(!(b-a<=0.1 || a-b<=0.1))
		{
		//对这个进行判断	
		if(Q1>Q2)
		{
			a=x1;
			x1=x2;
			x2=a+y*(b-a);
			Q1=Q2;
			
			foreCast(d,x2,host);//d只是一个计算MSE的中间值
			
			for(int i=2;i<host.getRatio().size();i++)
				{
					middle+=Math.pow((host.getRatio().get(i)-d.get(i-2)),2);
				}
			Q2=middle/(host.getRatio().size()-2);
			middle=0;
		}
		else
		{
			b=x2;
			x2=x1;
			x1=b-y*(b-a);
			Q2=Q1;
			foreCast(d,x1,host);//需要重新新建d吗
		//	System.out.println("the else second d is"+d.size());
			for(int i=2;i<host.getRatio().size();i++)
				{
					middle+=Math.pow((host.getRatio().get(i)-d.get(i-2)),2);
				}
			Q1=middle/(host.getRatio().size()-2);
			middle=0;
		}
		}
	   y=(a+b)/2;
	   foreCast(d,y,host);
	   
	   
	//在做预测的时候，是只做到n，用已有的N的数据进行预测处理，但是在真是计算时，计算到n+1
	//需要将结果改过来吗，还是只需要二次指数平滑结果值即可。
	//这句出问题
	    //System.out.println("d size is"+d.size());
	    if(d.size()==0)
	    	exSmooth=0;
	    else
        exSmooth=d.get(d.size()-1);//越界问题,二次平滑越界问题***************************************************
		//平滑参数
      //  System.out.println("exSmooth"+exSmooth);
		return  exSmooth;//d是一系列值，而最终要求的结果只是其中一个 
		
	}
	
	void foreCast(List<Double> d,double y,Host host)//返回一系列从0时刻到n-1时刻的所有时刻的预测值，因为每一个预测值都是累积与之前的相关,应该是计算到n时刻的值才可以。计算的是到什么时候的预测值
	{
		//List <Double> d=new ArrayList<Double> ();//用来记录预测值
		List <Double> S1=new ArrayList<Double> ();//用来记录平滑初值，一次指数平滑
		List <Double> S2=new ArrayList<Double> ();//用来记录平滑初值，二次指数平滑
		double initial,ratio=0,middle;//S为最初始序列的算术平均值,middle只是一个中间值v
		for(int i=0;i<host.getRatio().size();i++)
		{
			ratio+=host.getRatio().get(i);//主机的利用率需要一直更新
		}	
	//	System.out.println(host.getRatio().size());
		initial=ratio/host.getRatio().size();//平滑初值，即为公式中的S0.		
		//一次指数平滑
		for(int j=1;j<host.getRatio().size();j++)//第一次的数值只是作为开始的值，不参与平时的计算
		{
			middle=0;//每次都对中间值进行初赋值
			for(int k=1;k<=j;k++)
			{
				middle+=y*Math.pow((1-y),(j-k))*host.getRatio().get(k);
			}
			middle+=Math.pow((1-y),j)*initial;
			S1.add(middle);//给每个S1的依次平滑指数进行赋初值，平滑初值从1开始
		}
		//System.out.println(S1.size());
		//下面进行二次指数平滑值确定
		for(int i=0;i<host.getRatio().size()-1;i++)
		{
			middle=0;
			if(i==0)
			{
				middle=y*S1.get(i)+(1-y)*initial;
			}
			else
			{
				middle=y*S1.get(i)+(1-y)*S2.get(i-1);
			}
			S2.add(middle);//这样S1和S2都是从1开始的
			//上面是每轮的二次指数平滑值，不是预测值，下面实现每个每轮的预测	
			//做MSE的计算,从i=1开始做。		
		}
	//	System.out.println(S2.size());
		//根据一个二次指数平滑，计算出下每个下一轮的预测值
//	     d.add(host.getRatio().get(0));//将初值赋值为与真实一样的值，注意前面的n 既然已经从1开始了，所以预测应该从2的值开始。
		for(int i=0;i<host.getRatio().size()-1;i++)//注意不同list的起始的下标不一样的，这里面的ratio.size()只是用来计数，而不是用来取利用率的。
		{
		   d.add((2*S1.get(i)-S2.get(i))+(y/(1-y))*(S1.get(i)-S2.get(i)));//d直接从2开始存
		}
		//上面将所有的预测值都做完后，进行MSE的计算。
//		middle=0;
//		for(int i=1;i<host.getRatio().size();i++)                                            
//		{
//			middle+=Math.pow((host.getRatio().get(i)-d.get(i)),2);
//		}
//		MSE=middle/(host.getRatio().size()-1);
//		System.out.println("d的长度"+d.size());
//		System.out.println("d de "+d.size());	
		//return d;
	}
}

