
import java.util.ArrayList;
import java.util.List;

public class Host {

	/** The id. */
	private int id;//id是唯一标识主机的符号
	private int type;//给主机增加类型，说明是4类主机中的哪一种
	
	/**自加一个mips的处理能力；
	 */
	private double totalMips;//代表这个主机的所有处理能力，
	private double capMips;//代表这个主机的剩余处理能力，
	private double capRam;
	private double score;
	private double du;//uti-nui

	/** The storage. */
	private long storage;//没用
	
	/** The vm list. */
	private final List<? extends Vm> vmList = new ArrayList<Vm>();	//没用



	/** The vms migrating in. */
	private final List<Vm> vmsMigratingIn = new ArrayList<Vm>();//自带的列表
    
	/** The datacenter where the host is placed. */
	private double upperThreshold;//主机利用率上界
	private List<Double> ratio =new ArrayList<Double>();//host的每个时刻的利用率

	public Host(){
		
		setId(-1);
	}
	public Host(
			int id,
			int type,
			double totalMips,
			double capMips,
			double capRam,//可用的内存大小	
			double score
		//long storage,
			   ) 
	{
		setId(id);
		setType(type);
		setTotalMips(totalMips);
		setCapMips(capMips) ;
		setcapRam(capRam);
		setStorage(storage);	
		setScore(score);
	}
    public Host(Host a){
    	setId(a.getId());
		setType(a.getType());
		setTotalMips(a.getTotalMips());
		setCapMips(a.getCapMips()) ;
		setcapRam(a.getCapRam());
		setStorage(a.getStorage());	
		setScore(a.getScore());
		
	}
	public void printHost(){
    	System.out.print("HostID：");
    	System.out.println(this.getId());
    	System.out.print("HostType：");
    	System.out.println(this.getType());
    	System.out.print("HostTotalMips：");
    	System.out.println(this.getTotalMips());
    	System.out.print("HostCapMips：");
    	System.out.println(this.getCapMips());
    	System.out.print("HostCapRam：");
    	System.out.println(this.getCapRam());
    	System.out.print("HostScore：");
    	System.out.println(this.getScore());
    	System.out.print("HostVmSize：");
    	System.out.println(this.getVmsMigratingIn().size());	
    	System.out.println("********************************");
    	
    }
	public void addRatio(double ratio)//添加host的利用率
	{
	//	this.capRam=capRam;
		this.ratio.add(ratio);
	}
//	public void setRatio(int i,double ratio)//ratio中的利用率不用修改，所有的都是历史数据，新添的都是直接的
//	{
//	//	this.capRam=capRam;
//		this.ratio.set(i,ratio);
//	}
	public void setScore(double score)//添加host的利用率
	{
	//	this.capRam=capRam;
		this.score=score;
	}
	public void setDu(double d)//添加host的利用率
	{
	
		this.du=d;
	}
	public double getDu()//添加host的利用率
	{

	return this.du;
	}
	public List<Double> getRatio()//返回相应时刻的host的利用率
	{
	//	this.capRam=capRam;
		return this.ratio;
	}
	public void setcapRam(double capRam)
	{
		this.capRam=capRam;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public void setUpperThreshold(double threshold)
	{
		this.upperThreshold=threshold;
	}
	public double getUpperThreshold()
	{
		return this.upperThreshold;
	}
	
	public void setTotalMips(double totalMips)
	{
		this.totalMips=totalMips;
	}
	
	
	public Vm getVm(int vmId, int userId) {
		for (Vm vm : getVmList()) {
			if (vm.getId() == vmId && vm.getUserId() == userId) {
				return vm;
			}
		}
		return null;
	}

	public int getType() {
		//	return PeList.getTotalMips(getPeList());
			return type;
		}
	
	public double getTotalMips() {
	//	return PeList.getTotalMips(getPeList());
		return totalMips;
	}
	public void setVmsMigratingIn(Vm vm)
	{
		vmsMigratingIn.add(vm);
	}
	public List<Vm> getVmsMigratingIn() {
		return vmsMigratingIn;
	}
	//capMips;
	public double getCapMips() {
		return  capMips;
		
	}
	public double getScore() {
		return  score;
		
	}
	//这个剩余容量还是关于70%的剩余。
	public double setCapMips(double capMips) {  
		return  this.capMips=capMips;
		
	}
	

	
	public double getCapRam() {
		return capRam;
	}
	public void setCapRam(double capRam) {
		 this.capRam=capRam;
	}
	
	

	public long getStorage() {
		return storage;
	}


	public int getId() {
		return id;
	}

//	protected void setId(int id)
	public void setId(int id){
		this.id = id;
	}

	
	@SuppressWarnings("unchecked")
	public <T extends Vm> List<T> getVmList() {
		return (List<T>) vmList;
	}

	protected void setStorage(long storage) {
		this.storage = storage;
	}


}
