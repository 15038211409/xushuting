
//estimatePowerֻ�ǹ�������������û�ж���������Դ����ɾ����
public class estimate {
  public double estimatePower(Host host,Vm vm)
	{
		//�������һ��vm ���䵽host�ϵ���������	private double capMips;//�������������ʣ�ദ����������ʼ��Ϊ��������ĺ�������70%��private double requestMips;
		// unitEnergy     unitEnergy[0][0][0]= 0;
		double energyUnit[][][];//=new double[4][11][2];
		unitEnergy energy=new unitEnergy();
		energy.InitUnit();
	    int type=3;
		double power;
		double k;//k��the fraction of power consumed by the idle server(70%)
		double u;//u��the CPU utilization
        double hostRequest=0;
		//ע�����������е�vm ��ÿһʱ�̵������ʶ��ڱ䣬����������һ��vm���������ϵ��ܺ�ʱ��Ҫ���¼����������ϵ�vm���ܺ�ÿʱÿ�̵�vm��
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
				   k=energyUnit[type][0][1]/energyUnit[type][10][1];//�����ܺ�ռ���ܺĵı���
	
				   //��ҪHost��������ʱ��ʵʱ���¼������������ʣ���Ϊ����vm�����ڸı�
//				   for(int j=0;j<host.getVmsMigratingIn().size();j++)
//				   {
//					   /////////���������ⱨ��
//					   hostRequest=+host.getVmsMigratingIn().get(j).getRequestMips();
//				   }
	//			   host.setCapMips(host.getTotalMips() - hostRequest);
				   u=(vm.getRequestMips() + (host.getTotalMips()-host.getCapMips()))/host.getTotalMips();
         
				   power= k * energy.unitEnergy[type][10][1] +(1- k) * energy.unitEnergy[type][10][1] * u;
 
		   
		//���power����-1����˵�������Ų������VM
		return power;
	}

}
