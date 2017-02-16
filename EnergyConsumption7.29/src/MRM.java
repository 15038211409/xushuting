import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MRM {
	public int MRM(Host host,List<Vm> vmlist)
	{
		int mips[]={3000,2500,2000,1000,500};
		double ai[]=new double[5000];//���ai�����������Ͻ��vm��Դ�仯�ģ�
		double ar[]=new double[5000];//���ar����������vm��ƽ����Դ�����
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
	
		
		return 0;//����ΪʲôҪ������ֵ

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
	double SeondExponentialSmoothing(Host host)//����ָ��ƽ����     ����ȥ��vm��hostȻ�󿴱仯 
	{
		double exSmooth;//�������ƽ��ֵ
		double ratio=0;//host��������
		double a,a0=0,b,b0=1,y=0.618,x1,x2;//y��ʾ��ķ������Ϊ����
		double Q1,Q2,middle=0;//������¼MSE�����ļ���ֵ
		
		List<Double> d= new ArrayList<Double>();//dΪһϵ��Ԥ�������n��n��ǰ������ʱ�̵�Ԥ��ֵ
		int n;//������¼host�������ʴ���
		
		//������Բ��ü���ƽ����ֵ	
		a=a0;b=b0;
		x1=a+y*(b-a);
		x2=b-y*(b-a);
		
	    foreCast(d,x1,host); //����һ��d���м��㣬ÿ��new��ʱ����Ҫ���ǡ��ǲ��ǻᷢ��ָ�����    
	    //System.out.println(d.size());
	//    System.out.println("d size is");
	    for(int i=2;i<host.getRatio().size();i++)
		{
			middle+=Math.pow((host.getRatio().get(i)-d.get(i-2)),2);
		}
		Q1=middle/(host.getRatio().size()-2);//qΪMSE�ļ�����
		
		foreCast(d,x2,host);	
		middle=0;//ע��һЩӦ��ֵ�ĳ�ʼ������
		
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
				Q1=middle/(host.getRatio().size()-2);//qΪMSE�ļ�����
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
				Q2=middle/(host.getRatio().size()-2);//qΪMSE�ļ�����
				middle=0;
		}
		
		
		while(!(b-a<=0.1 || a-b<=0.1))
		{
		//����������ж�	
		if(Q1>Q2)
		{
			a=x1;
			x1=x2;
			x2=a+y*(b-a);
			Q1=Q2;
			
			foreCast(d,x2,host);//dֻ��һ������MSE���м�ֵ
			
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
			foreCast(d,x1,host);//��Ҫ�����½�d��
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
	   
	   
	//����Ԥ���ʱ����ֻ����n�������е�N�����ݽ���Ԥ�⴦�����������Ǽ���ʱ�����㵽n+1
	//��Ҫ������Ĺ����𣬻���ֻ��Ҫ����ָ��ƽ�����ֵ���ɡ�
	//��������
	    //System.out.println("d size is"+d.size());
	    if(d.size()==0)
	    	exSmooth=0;
	    else
        exSmooth=d.get(d.size()-1);//Խ������,����ƽ��Խ������***************************************************
		//ƽ������
      //  System.out.println("exSmooth"+exSmooth);
		return  exSmooth;//d��һϵ��ֵ��������Ҫ��Ľ��ֻ������һ�� 
		
	}
	
	void foreCast(List<Double> d,double y,Host host)//����һϵ�д�0ʱ�̵�n-1ʱ�̵�����ʱ�̵�Ԥ��ֵ����Ϊÿһ��Ԥ��ֵ�����ۻ���֮ǰ�����,Ӧ���Ǽ��㵽nʱ�̵�ֵ�ſ��ԡ�������ǵ�ʲôʱ���Ԥ��ֵ
	{
		//List <Double> d=new ArrayList<Double> ();//������¼Ԥ��ֵ
		List <Double> S1=new ArrayList<Double> ();//������¼ƽ����ֵ��һ��ָ��ƽ��
		List <Double> S2=new ArrayList<Double> ();//������¼ƽ����ֵ������ָ��ƽ��
		double initial,ratio=0,middle;//SΪ���ʼ���е�����ƽ��ֵ,middleֻ��һ���м�ֵv
		for(int i=0;i<host.getRatio().size();i++)
		{
			ratio+=host.getRatio().get(i);//��������������Ҫһֱ����
		}	
	//	System.out.println(host.getRatio().size());
		initial=ratio/host.getRatio().size();//ƽ����ֵ����Ϊ��ʽ�е�S0.		
		//һ��ָ��ƽ��
		for(int j=1;j<host.getRatio().size();j++)//��һ�ε���ֵֻ����Ϊ��ʼ��ֵ��������ƽʱ�ļ���
		{
			middle=0;//ÿ�ζ����м�ֵ���г���ֵ
			for(int k=1;k<=j;k++)
			{
				middle+=y*Math.pow((1-y),(j-k))*host.getRatio().get(k);
			}
			middle+=Math.pow((1-y),j)*initial;
			S1.add(middle);//��ÿ��S1������ƽ��ָ�����и���ֵ��ƽ����ֵ��1��ʼ
		}
		//System.out.println(S1.size());
		//������ж���ָ��ƽ��ֵȷ��
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
			S2.add(middle);//����S1��S2���Ǵ�1��ʼ��
			//������ÿ�ֵĶ���ָ��ƽ��ֵ������Ԥ��ֵ������ʵ��ÿ��ÿ�ֵ�Ԥ��	
			//��MSE�ļ���,��i=1��ʼ����		
		}
	//	System.out.println(S2.size());
		//����һ������ָ��ƽ�����������ÿ����һ�ֵ�Ԥ��ֵ
//	     d.add(host.getRatio().get(0));//����ֵ��ֵΪ����ʵһ����ֵ��ע��ǰ���n ��Ȼ�Ѿ���1��ʼ�ˣ�����Ԥ��Ӧ�ô�2��ֵ��ʼ��
		for(int i=0;i<host.getRatio().size()-1;i++)//ע�ⲻͬlist����ʼ���±겻һ���ģ��������ratio.size()ֻ����������������������ȡ�����ʵġ�
		{
		   d.add((2*S1.get(i)-S2.get(i))+(y/(1-y))*(S1.get(i)-S2.get(i)));//dֱ�Ӵ�2��ʼ��
		}
		//���潫���е�Ԥ��ֵ������󣬽���MSE�ļ��㡣
//		middle=0;
//		for(int i=1;i<host.getRatio().size();i++)                                            
//		{
//			middle+=Math.pow((host.getRatio().get(i)-d.get(i)),2);
//		}
//		MSE=middle/(host.getRatio().size()-1);
//		System.out.println("d�ĳ���"+d.size());
//		System.out.println("d de "+d.size());	
		//return d;
	}
}

