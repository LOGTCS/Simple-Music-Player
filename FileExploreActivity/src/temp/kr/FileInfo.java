package temp.kr;

import android.os.Parcel;
import android.os.Parcelable;

public class FileInfo implements Parcelable{
	private String Path;
	private String Name;
	
	public FileInfo()
	{
		this.Path = "";
		this.Name = "";
	}
	
	public FileInfo(Parcel in){
		Path = in.readString();
		Name = in.readString();
		
	}
	
	public FileInfo(String _Path, String _Name)
	{
		this.Path = _Path;
		this.Name = _Name;
	}
	
	public String getName()
	{
		return this.Name;
	}
	
	public String getPath()
	{
		return this.Path;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(Path);
		dest.writeString(Name);
	}
	
	 public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
	        public FileInfo createFromParcel(Parcel in) {
	             return new FileInfo(in);
	       }

	       public FileInfo[] newArray(int size) {
	            return new FileInfo[size];
	       }
	   };
}