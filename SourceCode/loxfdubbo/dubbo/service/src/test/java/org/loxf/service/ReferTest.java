/**
 * ReferTest.java
 * luohj - 下午5:13:41
 * 
 */
package org.loxf.service;

import java.lang.reflect.InvocationTargetException;

import org.loxf.api.demo.IHello;
import org.loxf.api.demo.IWho;
import org.loxf.registry.annotation.Customer;
import org.loxf.registry.utils.BaseRefer;

/**
 * @author luohj
 *
 */
public class ReferTest extends BaseRefer {
	@Customer
	private IHello hello;
	@Customer(group = "WhoBoy")
	private IWho whoBoy;
	@Customer(group = "WhoGirl")
	private IWho whoGirl;

	public static void main(String[] args) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ReferTest test = new ReferTest();
		test.hello.sayHi();
		test.whoBoy.who();
		test.whoBoy.iam("Li lei");
		test.whoGirl.who();
		test.whoGirl.iam("Han Meimei");
	}
}
