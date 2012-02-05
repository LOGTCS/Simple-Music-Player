package temp.kr;

import java.util.ArrayList;
import temp.kr.FileInfo;

public class PlayList {
	ArrayList<FileInfo> fileList;
	private int current = -1;	//player에서 계속 NextFile을 재생하므로, 두 번째 파일부터 재생하는 문제가 있어서 
	
	public PlayList(ArrayList<FileInfo> list)	{
		this.fileList = list;
	}
	
	public PlayList()	{
		this.fileList = new ArrayList<FileInfo>();
		//this.fileList.add("/mnt/sdcard/1.mp3");
		//this.fileList.add("/mnt/sdcard/아이유 - 너랑 나.mp3");
		//this.fileList.add("/mnt/sdcard/3.mp3");
	}
	
	public String GetNextFile()	{
		FileInfo ret = new FileInfo();
		
		if(current+1 < this.fileList.size())	{
			ret = fileList.get(++current);
		}
		return ret.getPath();
	}
	
	public String GetPreviousFile()	{
		FileInfo ret = new FileInfo();
		if(current-1 >= 0)	{
			ret = fileList.get(--current);
		}
		return ret.getPath();
	}
}