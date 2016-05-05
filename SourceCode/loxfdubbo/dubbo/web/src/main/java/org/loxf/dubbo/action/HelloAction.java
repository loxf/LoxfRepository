/**
 * HelloAction.java
 * luohj - 下午3:16:11
 * 
 */
package org.loxf.dubbo.action;

import org.loxf.api.demo.IHello;
import org.loxf.api.demo.IWho;
import org.loxf.registry.annotation.Customer;
import org.loxf.registry.utils.BaseRefer;

/**
 * @author luohj
 *
 */
public class HelloAction extends BaseRefer {
	public HelloAction(){
		super.instancesBean(this);
	}
	
	@Customer(interfaces = IHello.class)
	public IHello hello;
	@Customer(interfaces = IWho.class, group = "WhoBoy")
	public IWho whoBoy;
	@Customer(interfaces = IWho.class, group = "WhoGirl")
	public IWho whoGirl;
	
	public void hello(){
		System.out.println(hello.sayHi());
	}
	
	public void whoBoy(){
		System.out.println(whoBoy.who());
	}
	
	public void whoGirl(){
		System.out.println(whoGirl.who());
	}
	
	public void iam(String name){
		System.out.println(whoBoy.iam(name));
	}
	
	public static void main(String[] args){
		HelloAction helleAction = new HelloAction();
		helleAction.hello();
		helleAction.whoBoy();
		helleAction.iam("Li lei");
		helleAction.whoGirl();
		helleAction.iam("Han Meimei");
	}
}
