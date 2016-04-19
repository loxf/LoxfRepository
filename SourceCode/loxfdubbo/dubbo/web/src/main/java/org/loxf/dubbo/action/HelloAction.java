/**
 * HelloAction.java
 * luohj - 下午3:16:11
 * 
 */
package org.loxf.dubbo.action;

import org.loxf.api.demo.IHello;
import org.loxf.api.demo.IWho;

/**
 * @author luohj
 *
 */
public class HelloAction {
	private IHello hello;
	private IWho whoBoy;
	private IWho whoGirl;
	public void hello(){
		hello.sayHi();
	}
	
	public void whoBoy(){
		whoBoy.who();
	}
	
	public void whoGirl(){
		whoGirl.who();
	}
	
	public static void main(String[] args){
		
	}
}
