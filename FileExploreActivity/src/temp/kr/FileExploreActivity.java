package temp.kr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import temp.kr.R;
import temp.kr.FileInfo;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;



public class FileExploreActivity extends Activity {
    /** Called when the activity is first created. */
	public static final int ID_MUSIC_LIST_SAVE	= Menu.FIRST+1;
	public static final int ID_MUSIC_LIST_ADD	= Menu.FIRST+2;
	
	private ListView FileView;
	private TextView PathView;
	private ListView SelectView;
	
	private String _Path = "";
	
	private ArrayList<String> _List = new ArrayList<String>();
	private ArrayList<String> _FolderList = new ArrayList<String>();
	private ArrayList<String> _FileList = new ArrayList<String>();
	private ArrayAdapter<String> _Adapter;// = null;
	
	private ArrayList<FileInfo> m_FileList = new ArrayList<FileInfo>();
	private FileInfoAdapter m_SListAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        FileView = (ListView)findViewById(R.id.file_explore_list);
        SelectView = (ListView)findViewById(R.id.file_selected_list);
        PathView = (TextView)findViewById(R.id.file_explore_pathview);
        
        this.m_SListAdapter = new FileInfoAdapter(this, R.id.file_selected_list, this.m_FileList);
        
        this.firstInit();        
       
        
        FileView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				// TODO Auto-generated method stub
				String fileName = FileView.getItemAtPosition(position).toString();
				if (fileName.matches("<.*>")){
					MoveFolder(GetRealPathName(fileName));
				}
				else
				{
					FileSelect(_Path+fileName, fileName);
				}
			}        	
        });
        
        SelectView.setOnItemClickListener( new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
        		
        		m_FileList.remove(position);
        		
        		SelecterReflash();
        	}
        });
    }
   

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		
		// Group ID없으며, 각각 이름이 Red, Green, Blue인 아이템을 menu객체에 추가
		// 아이템은 순서대로 1, 2, 3의 아이템 ID를 부여 받았다.
		// 또 순서가 2, 1, 0 순으로 되어있어 Blue가 가장처음 Red가 가장 나중에 표시된다.
		menu.add(Menu.NONE, ID_MUSIC_LIST_SAVE, 0, "리스트 저장");
		menu.add(Menu.NONE, ID_MUSIC_LIST_ADD, 1, "지금 재생에 추가");
		return result;
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) {
    	case ID_MUSIC_LIST_SAVE:
    		Log.i("heyon", "Click Menu must save music list");
    		// 여기에 음악 리스트를 저장하는 것이 필요하다.
    		
    		break;
    		
    	case ID_MUSIC_LIST_ADD:
    		Log.i("hyeon", "ID_MUSIC_LIST_ADD");
    		Intent setting = new Intent(this, MediaPlayerService.class); 
	        setting.setAction("MusicAdd"); // 넘길 자료 셋팅 
	        setting.putParcelableArrayListExtra("MusicList", (ArrayList<FileInfo>) m_FileList);
	        PendingIntent pendingIntent = PendingIntent.getService(this, 0, setting, 0); 
	        
	        try
	        {
	        	Log.i("hyeon", "ID_MUSIC_LIST_ADD2");
	        	pendingIntent.send();
	        	Log.i("hyeon", "ID_MUSIC_LIST_ADD3");
	        }
	        catch(Exception e)
	        {
	        	e.printStackTrace();
	        	
	        }
    		
    		finish();
    		break;
    	}
    	return (super.onOptionsItemSelected(item));
    }    

    
    private void firstInit()
    {
    	Log.i("heyon", "firstInit");
    	
    	this.m_FileList.clear();
    	
    	String Path;
		try {
			Path = Environment.getExternalStorageDirectory().getCanonicalPath()  /*+ R.string.mp3root */;
			
			this.MoveFolder(Path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	FileView.setFocusable(true);
    	FileView.setFocusableInTouchMode(true); 
    	
    }


	public void FileSelect(String pPath, String pfileName)
    {	
		//선택한 파일의 경로가 나옵니다.
		Log.i("heyon", pPath);
		
		FileInfo f = new FileInfo(pPath, pfileName);
		
		this.m_FileList.add(f);
		
		this.SelecterReflash();
    }
    
	
	public void SelecterReflash()
	{
		SelectView.setAdapter(this.m_SListAdapter);
	}
	
    public void MoveFolder(String Path)
    {
    	Log.i("heyon", "MoveFolder("+Path+")");
    	if (Path.length() == 0){
    		Path = "/";
		} else {
			String lastChar = Path.substring(Path.length()-1, Path.length());
			if(lastChar.matches("/") == false)
				Path = Path + "/";
		}
		
		if ( OpenPath (Path)) {
			_Path = Path;
			UpdateAdapter();
			
			
			PathView.setText(Path);
		}
    }
    
    private boolean OpenPath(String path) {
		_FolderList.clear();
		_FileList.clear();
		
		
		Log.i("heyon", "OpenPath");
		
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) return false;
        
        for (int i=0; i<files.length; i++) {
        	if (files[i].isDirectory()) {
        		Log.i("heyon", "<" + files[i].getName() + ">");
        		_FolderList.add("<" + files[i].getName() + ">");
        	} else {
        		Log.i("heyon", files[i].getName());
        		
        		if( files[i].getName().endsWith(".mp3"))
        		_FileList.add(files[i].getName());
        	}
        }
        
        Collections.sort(_FolderList);
        Collections.sort(_FileList);
        
        _FolderList.add(0, "<..>");
        
        return true;
	}
    
    private void UpdateAdapter()
    {
    	_List.clear();
        _List.addAll(_FolderList);
        _List.addAll(_FileList);
                
        Log.i("heyon", "setAdapter");
		_Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, _List);
		
		FileView.setAdapter(_Adapter);
    }
    
 
    private String GetRealPathName(String newPath){
		String path = newPath.substring(1, newPath.length()-1);
		
		if ( path.matches("..")){
			return DeleteLastFolder(_Path);
		}else {
			return _Path + path + "/";
		}
	}
    
    private String DeleteLastFolder(String value)
    {
    	String list[] = value.split("/");

		String result = "";
		
		for (int i=0; i<list.length-1; i++) {
			result = result + list[i] + "/"; 
		}
		
		return result;
    }
    
    private class FileInfoAdapter extends ArrayAdapter<FileInfo> {
    	private ArrayList<FileInfo> items;
    	
    	public FileInfoAdapter(Context context, int textViewResourceId, ArrayList<FileInfo> items){
    		super(context, textViewResourceId, items);
    		this.items = items;
    	}
    	
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent){
    		View v = convertView;
    		
    		if ( v == null ){
    			LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    			v = vi.inflate(R.layout.fileinfo_row, null);
    		}
    		
    		FileInfo f = items.get(position);
    		
    		if ( f != null ){
    			TextView tt = (TextView) v.findViewById(R.id.toptext);
    			TextView bt = (TextView) v.findViewById(R.id.bottomtext);
    			
    			if ( tt != null ) {
    				tt.setText(f.getName());
    			}
    			
    			if ( bt != null ) {
    				bt.setText(f.getPath());
    			}
    		}
    		return v;
    	}
    }
}

