/**
 * LoadBalanceUtils.java
 * luohj - 下午3:52:37
 * 
 */
package org.loxf.registry.utils;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * 负载算法工具
 * @author luohj
 *
 */
public class LoadBalanceUtil {
	/**
	 * 随机算法
	 * @param collection
	 * @param type
	 * @return
	 * @author:luohj
	 * @see org.loxf.registry.constracts.PollingType#RANDOM
	 */
	public static <T> T getTByRandom(List<T> list, String type){
		Random r = new Random();
		int i = r.nextInt(list.size());
		return list.get(i);
	}
	/**
	 * 轮询算法
	 * @param collection
	 * @param type
	 * @return
	 * @author:luohj
	 * @see org.loxf.registry.constracts.PollingType#POLLING
	 */
	public static <T> Object getTByPolling(Collection<T> collection, String type){
		return null;
	}
	/**
	 * 加权轮询(WRR)
	 * @param collection
	 * @param type
	 * @return
	 * @author:luohj
	 * @see org.loxf.registry.constracts.PollingType#WRR
	 */
	public static <T> Object getTByWRR(Collection<T> collection, String type){
		return null;
	}
	/**
	 * 动态轮询(DYNC_POLLING)
	 * @param collection
	 * @param type
	 * @return
	 * @author:luohj
	 * @see org.loxf.registry.constracts.PollingType#DYNC_POLLING
	 */
	public static <T> Object getTByDyncPolling(Collection<T> collection, String type){
		return null;
	}
	/**
	 * 最少连接算法
	 * @param collection
	 * @param type
	 * @return
	 * @author:luohj
	 * @see org.loxf.registry.constracts.PollingType#MIN_CONNECTION
	 */
	public static <T> Object getTByMinConnection(Collection<T> collection, String type){
		return null;
	}
	/**
	 * 观察算法
	 * @param collection
	 * @param type
	 * @return
	 * @author:luohj
	 * @see org.loxf.registry.constracts.PollingType#OBSERVATION
	 */
	public static <T> Object getTByObservation(Collection<T> collection, String type){
		return null;
	}
	/**
	 * 预判算法
	 * @param collection
	 * @param type
	 * @return
	 * @author:luohj
	 * @see org.loxf.registry.constracts.PollingType#PREDICTION
	 */
	public static <T> Object getTByPreDiction(Collection<T> collection, String type){
		return null;
	}
}
