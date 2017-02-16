//用来创建host时，增加其hostid数目
public class HostIDnum {
   private int HostIDnum;
   
   HostIDnum(){
	   
	   HostIDnum=0;
	   }
    void ins(){	
    	HostIDnum++;
    }
    int getHostID(){
    	
    	return HostIDnum;
    }
}
