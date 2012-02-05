package temp.kr;

import java.util.ArrayList;
import temp.kr.FileInfo;

public class PlayList {
	ArrayList<FileInfo> fileList;
	private int current = -1;	//player���� ��� NextFile�� ����ϹǷ�, �� ��° ���Ϻ��� ����ϴ� ������ �־ 
	
	public PlayList(ArrayList<FileInfo> list)	{
		this.fileList = list;
	}
	
	public PlayList()	{
		this.fileList = new ArrayList<FileInfo>();
		//this.fileList.add("/mnt/sdcard/1.mp3");
		//this.fileList.add("/mnt/sdcard/������ - �ʶ� ��.mp3");
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