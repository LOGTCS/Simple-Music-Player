package temp.kr;

import java.util.ArrayList;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import temp.kr.FileExploreActivity;

//파일 하나를 입력받아 해당 파일을 재생하는 클래스.
//PlayList에서 받은 파일을 재생한다.
public class MediaPlayerService extends Service {	
	final String TAG = "debug"; 
	//public static boolean ServiceOn = false;
	MediaPlayer mMediaPlayer;
	int status = 0;	//0 : Nothing, 1 : on Play, 2:Pause
	float vol = 1.0f;
	//Context context = getApplicationContext();
	
	//NotificationManager 관련 변수 및 상수 설정
	NotificationManager NM;
	public static int SMP_NOTIFINUMBER = 2;
	
	PlayList playList = new PlayList();
	
	serviceBinder sB = new serviceBinder();
	
	@Override
    public void onCreate() {
		try	{
			this.createPackageContext("temp.kr", CONTEXT_IGNORE_SECURITY);
		} catch(Exception e)	{
			Log.e("debug", e.toString());
		}
		
        // TODO Auto-generated method stub
        super.onCreate();
        Log.d("debug", "onCreate");
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer mp) {
				try	{
					mp.reset();
					status = 0;
					MediaPlayerService.this.Play();
				} catch(Exception e){
					Log.e(TAG, "onCompletion"+e.toString());
				}
			}
		});
        Widget.ServiceOn = true;
    }
	
	@Override
    public void onStart(Intent intent, int startId) {
        String action = intent.getAction();
        Log.d("debug", "service onStart " + action);
        if(action != null)	{
	        if(action.equals("play"))	{
	        	//status에 따라 play인지 pause인지 resume인지 결정
	        	//0 : nothing, 1 : 플레이중, 2:pause
	        	//Log.d("debug", "service onStart " + status);
	        	switch(status){
	        	case 0 :
	        		Play();	        		
	        		break;
	        	case 1 :
	        		pause();
	        		break;
	        	case 2 :
	        		resume();
	        		break;
	        	}	        	
	        }
	        else if(action.equals("prev"))	{
	        	previous();
	        }
	        else if(action.equals("next"))	{
	        	next();
	        }
	        else if(action.equals("MusicAdd")){
	        	Bundle bd = intent.getExtras();
				ArrayList<FileInfo> fi = bd.getParcelableArrayList("MusicList");
				for(int i = 0; i < fi.size(); i++)	{
					Log.i("hyeon", "path : "+fi.get(i).getPath());
					Log.i("hyeon", "name : " + fi.get(i).getName());
				}
				
				playList = new PlayList(fi);
				
	        }
	        	
        }
        super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.d("debug", "onBind");
		return sB;
	}
	
	public void Stop()	{
		status = 0;
		mMediaPlayer.stop();
	}
	
	public int Play()	{
		Log.i("debug", "play");
		int ret = -1;
		try{
			String mediaFile = playList.GetNextFile();
			if(mediaFile != "")	{
				Log.i(TAG, mediaFile);
				if(mediaFile != "")	{					
					mMediaPlayer.setDataSource(mediaFile);
					mMediaPlayer.prepare();
					mMediaPlayer.start();
					
					/*
					NM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);//
	                Notification noti = new Notification(R.drawable.ic_launcher, "노티피케이션 테스트입니다?", System.currentTimeMillis());
	                Intent i = new Intent(context, FileExploreActivity.class);
	                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
	                noti.setLatestEventInfo(context, "SMP", mediaFile, pi);
	                NM.notify(SMP_NOTIFINUMBER, noti);
					*/
					ret = this.status = 1;
				}
				else	{
					Log.i(TAG, "all music played");
				}
			}
			else {
				//파일 열기 창을 연다.
				Intent intent = new Intent(getApplicationContext(), FileExploreActivity.class);
				
				//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				
				PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
				Log.i("hyeon", "before Activity");
				try
				{
					pendingIntent.send(getApplicationContext(), 1111, intent);
				}
				catch(CanceledException e)
				{
					e.printStackTrace();
				}
				
				
				
				//startActivity(intent);
				Log.i("hyeon", "after Activity");
				
				
			}
		}
		catch (Exception e){
			Log.e(TAG, "exception "+e.toString());
		}
		return ret;
	}
	
	public int pause()	{
		mMediaPlayer.pause();
		return status = 2;
	}
	
	public int resume()	{
		mMediaPlayer.start();
		return status = 1;
	}
	
	public int next()	{
		int ret = 0;
		mMediaPlayer.reset();
		try	{
			String mediaFile = playList.GetNextFile();
			if(mediaFile != "")	{
				mMediaPlayer.setDataSource(mediaFile);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
				ret = this.status = 1;
			} 
		}catch(Exception e){
			Log.e(TAG, "next : "+e.toString());
		}
		return ret;
	}
	
	public int previous()	{
		int ret = 0;
		mMediaPlayer.reset();
		try	{
			String mediaFile = playList.GetPreviousFile();
			if(mediaFile != "")	{
				mMediaPlayer.setDataSource(mediaFile);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
				ret = this.status = 1;
			} 
		}catch(Exception e){
			Log.e(TAG, "next : "+e.toString());
		}
		return ret;
	}
		
	class serviceBinder extends Binder	{
		public MediaPlayerService getService()	{
			return MediaPlayerService.this;
		}
	}
	
	@Override
	public void onDestroy()	{
		Widget.ServiceOn = false;
		super.onDestroy();
	}
}