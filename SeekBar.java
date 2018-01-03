import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SeekBar extends JProgressBar{

	private int updatedValue = 0;

	public void updateSeekBar(long progress, float totalVal){
		BackgroundExecutor.get().execute(new UpdatingTask(progress, totalVal));
		setValue(updatedValue);
	}

	private class UpdatingTask implements Runnable{

		long progress;
		float totalVal;

		public UpdatingTask(long progress, float totalVal){
			this.progress = progress;
			this.totalVal = totalVal;
		}

		@Override
		public void run(){
			int lp = (int) (progress/1000);
			int seekLength = getMaximum();
			int n = (int) ((lp/(totalVal+1000))*seekLength);
			updatedValue = updatedValue+n;
		}
	}

	public SeekBar(){
		super();
		setMaximum(1000);
		addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e){}

			@Override
			public void mousePressed(MouseEvent e){
				float val = ((float)e.getX()/getWidth())*getMaximum();
				returnValueToPlayer(val);
				setValue((int)val);
				log("SeekBar pressed: "+val+" x: "+e.getX());
			}

			@Override
			public void mouseExited(MouseEvent e){}

			@Override
			public void mouseEntered(MouseEvent e){}

			@Override
			public void mouseClicked(MouseEvent e){}

		});
	}

	private void returnValueToPlayer(float val){}
	private void log(String s){
		System.out.println("\n\nSeekbar : \n\n"+s+"\n\n");
	}
}