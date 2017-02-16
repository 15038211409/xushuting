
//estimatePower只是估算了能量，并没有对主机的资源进行删减。
public class estimate {
  public double estimatePower(Host host,Vm vm)
	{
		//计算出讲一个vm 分配到host上的能量增长	private double capMips;//代表这个主机的剩余处理能力，初始化为这个主机的赫兹数的70%；private double requestMips;
		// unitEnergy     unitEnergy[0][0][0]= 0;
		double energyUnit[][][];//=new double[4][11][2];
		unitEnergy energy=new unitEnergy();
		energy.InitUnit();
	    int type=3;
		double power;
		double k;//k是the fraction of power consumed by the idle server(70%)
		double u;//u是the CPU utilization
        double hostRequest=0;
		//注意主机上已有的vm 的每一时刻的利用率都在变，所以评估下一个vm放置在其上的能耗时，要重新计算已在其上的vm的能耗每时每刻的vm的
			switch(host.getType())
			{
			case 0:type=0;break;
			case 1:type=1;break;
			case 2:type=2;break;
			case 3:type=3;break;
			}
		           power=88888;
		         //  vm.getRequestMips();
		         //  host.getTotalMips();
		         //  host.getCapMips();
		           energyUnit=energy.getUnitEnergy();
				   k=energyUnit[type][0][1]/energyUnit[type][10][1];//空闲能耗占总能耗的比例
	
				   //需要Host的利用率时，实时更新计算主机利用率，因为其上vm不断在改变
//				   for(int j=0;j<host.getVmsMigratingIn().size();j++)
//				   {
//					   /////////这里有问题报错
//					   hostRequest=+host.getVmsMigratingIn().get(j).getRequestMips();
//				   }
	//			   host.setCapMips(host.getTotalMips() - hostRequest);
				   u=(vm.getRequestMips() + (host.getTotalMips()-host.getCapMips()))/host.getTotalMips();
         
				   power= k * energy.unitEnergy[type][10][1] +(1- k) * energy.unitEnergy[type][10][1] * u;
 
		   
		//如果power等于-1，则说明主机放不下这个VM
		return power;
	}

}
