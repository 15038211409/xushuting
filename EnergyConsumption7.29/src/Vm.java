
import java.util.ArrayList;
import java.util.List;


public class Vm {

	/** The id. */
	private int id;
	private int type;//vm的类型有0到4
	/** The user id. */
	private int userId;
   

	/** The size. */
	private long size;

	/** The MIPS. */
	private double mips;//这是总得mips要求
	private double requestMips;


	/** The ram. */
	private double ram;
	private double capRam;

	/** The bw. */
	private double bw;

	

	/** The host. */
	private Host host;
	private int  hostid;//存放此vm的host的id号码


	/** The current allocated mips. */

	private List<Integer> numHost=new ArrayList<Integer>();//用来存放vm的每个宿主机的id
	
	//增加两项利用率
	private List<Double> Ut = new ArrayList<Double>();//
	private double maxRangeUt;//利用率的大小区间范围
	private double maxRangeUse;//最大的资源利用具体值差值
    private int lifeTime;//有生命周期，是不是还应该记录生命的剩余时间，或者是时钟时间。
    private int runTime;//vm的已运行时间
    private double averRe;//平均资源需求
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
			int runTime//用来记录vm的生存时间，生存时间确定后，就可以把利用率存入Ut当中，普适性情况下，把时间定为一样的，后续试验可以把这个改成随机生成时间
			) 
			{
		setId(id);
		setType(type);
		setUserId(userId);
		setMips(mips);
		
		setRam(ram);
		setBw(bw);
		setCapRam(ram);
		setlifeTime(lifeTime);//设置生命周期
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
		System.out.print("VmID：");
    	System.out.println(this.getId());
    	System.out.print("VmType：");
    	System.out.println(this.getType());
    	System.out.print("VmRequestMips：");
    	System.out.println(this.getRequestMips());
    	System.out.print("VmUsrID：");
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
