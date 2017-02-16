
import java.util.ArrayList;
import java.util.List;


public class Vm {

	/** The id. */
	private int id;
	private int type;//vm��������0��4
	/** The user id. */
	private int userId;
   

	/** The size. */
	private long size;

	/** The MIPS. */
	private double mips;//�����ܵ�mipsҪ��
	private double requestMips;


	/** The ram. */
	private double ram;
	private double capRam;

	/** The bw. */
	private double bw;

	

	/** The host. */
	private Host host;
	private int  hostid;//��Ŵ�vm��host��id����


	/** The current allocated mips. */

	private List<Integer> numHost=new ArrayList<Integer>();//�������vm��ÿ����������id
	
	//��������������
	private List<Double> Ut = new ArrayList<Double>();//
	private double maxRangeUt;//�����ʵĴ�С���䷶Χ
	private double maxRangeUse;//������Դ���þ���ֵ��ֵ
    private int lifeTime;//���������ڣ��ǲ��ǻ�Ӧ�ü�¼������ʣ��ʱ�䣬������ʱ��ʱ�䡣
    private int runTime;//vm��������ʱ��
    private double averRe;//ƽ����Դ����
    public Vm(){
    	
    	
    }

	public Vm(
			int id,
			int type,
			int userId,
			double mips,
			double ram,
			double bw,
			int lifeTime,
			int runTime//������¼vm������ʱ�䣬����ʱ��ȷ���󣬾Ϳ��԰������ʴ���Ut���У�����������£���ʱ�䶨Ϊһ���ģ�����������԰�����ĳ��������ʱ��
			) 
			{
		setId(id);
		setType(type);
		setUserId(userId);
		setMips(mips);
		
		setRam(ram);
		setBw(bw);
		setCapRam(ram);
		setlifeTime(lifeTime);//������������
		this.runTime=0;
		//updateRequestMips();

	}
	 public Vm(Vm a){
	    	setId(a.getId());
			setType(a.getType());
			setUserId(a.getUserId());
			setMips(a.getMips()) ;
			setRam(a.getRam());
			setBw(a.getBw());	
			setlifeTime(a.getlifeTime());
			setRunTime(a.getRuntime());
		}
	private void setRunTime(int runtime2) {
		// TODO Auto-generated method stub
		
	}

	public void print()
	{
		System.out.print("VmID��");
    	System.out.println(this.getId());
    	System.out.print("VmType��");
    	System.out.println(this.getType());
    	System.out.print("VmRequestMips��");
    	System.out.println(this.getRequestMips());
    	System.out.print("VmUsrID��");
    	System.out.println(this.getUserId());
    	System.out.println("******************");
	}
    public void setRequestMips()
    {
    	this.requestMips =this.mips*this.Ut.get(this.runTime);
    }
	public int getRuntime()
	{
		return this.runTime;
	}
	public void setaverRe(double a)
    {
    	this.averRe =a;
    }
	public double getaverRe()
	{
		return this.averRe;
	}
	public void insRuntime()
	{
		++runTime;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public int getType()
	{
		return this.type;
	}
	public void setmaxRangeUt(double range)
	{
		this.maxRangeUt=range;
	}
	public void setmaxRangeUse(double use)
	{
		this.maxRangeUse=use;
	}
	public double getmaxRangeUse()
	{
		return this.maxRangeUse;
	}
	public double getmaxRangeUt()
	{
		return this.maxRangeUt;
	}

	public void setUt(double Ut)
	{
		this.Ut.add(Ut);
	}
	public List<Double> getUt()
	{
		return this.Ut;
	}
	public void setlifeTime(int time)
	{
		this.lifeTime=time;
	}
	public int getlifeTime()
	{
		return lifeTime;
	}
	public void setNumHost(int num)
	{
		this.numHost.add(num);
	}
	public  List<Integer> getNumHost()
	{
		return this.numHost;
	}


	public static String getUid(int userId, int vmId) {
		return userId + "-" + vmId;
	}

	
	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public double getCapRam() {
		return capRam;
	}
	public double setCapRam(double capRam) {
		return this.capRam=capRam;
	}
	
	protected void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	
	public double getMips() {
		return mips;
	}
    
	public double getRequestMips() {
		return requestMips;
	}

	protected void setMips(double mips) {
		this.mips = mips;
	}
	public void updateRequestMips() {
		if(this.Ut.size()<runTime)
			 ;
		else
		this.requestMips = this.mips*this.Ut.get(runTime);
	};

	public double getRam() {
		return ram;
	}

	public void setRam(double ram) {
		this.ram = ram;
	}

	public double getBw() {
		return bw;
	}

	public void setBw(double l) {
		this.bw = l;
	}

	public long getSize() {
		return size;
	}

	
	public void setSize(long size) {
		this.size = size;
	}


	public Host setHost(Host host) {
		return this.host = host;
	}
	public void setHostId(int host) {
		 this.hostid = host;
	}
	public int getHostId() {
		return  this.hostid;
	}
	
	public Host getHost() {
		return host;
	}


	

}
