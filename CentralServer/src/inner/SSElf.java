package inner;

import my.utils.Consts;
import server.*;

public class SSElf extends Thread {
	
	public SSElf() {
		super();
	}

	@Override
	public void run() {
		try {
			while(!interrupted()) {
				Thread.sleep(2000);
				synchronized(Main.cs) {
					for(Subserver s : Main.cs.subs) {
						if(s.helloCnt == 0) {
							if(System.currentTimeMillis() - s.lastUpdate > Consts.Y) {
								s.helloCnt++;
							}
						}
						else if(s.helloCnt == 1){
							if(System.currentTimeMillis() - s.lastUpdate > 3*(Consts.Y/2)) {
								s.helloCnt++;
							}
						}
						else {
							if(System.currentTimeMillis() - s.lastUpdate > 2*Consts.Y) {
								Main.cs.removeSubserver(s);
							}
						}
					}
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
