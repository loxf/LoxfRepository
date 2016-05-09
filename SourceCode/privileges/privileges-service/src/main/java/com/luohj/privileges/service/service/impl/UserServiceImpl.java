/**
 * Title: UserServiceImpl.java
 * Copyright: Loxf
 * Company: Loxf Team
 * @author: luohj
 * @version: 1.0
 * @time:  2015-9-10 上午11:06:49
 */
package com.luohj.privileges.service.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.luohj.privileges.core.service.impl.AbstractBaseService;
import com.luohj.privileges.core.tags.Cacheable;
import com.luohj.privileges.dao.IUserDao;
import com.luohj.privileges.model.User;
import com.luohj.privileges.service.service.IUserService;

/**
 * @author luohj
 */

@Service
public class UserServiceImpl extends AbstractBaseService implements
		IUserService {

	/** (non-Javadoc)
	 * @see com.luohj.privileges.service.service.IUserService#insertUser(com.luohj.privileges.model.User)
	 */
	@Resource
	private IUserDao userDao ;
	
	@Override
	public Long insertUser(User user) {
		return userDao.insertUser(user);
	}
	/** (non-Javadoc)
	 * @see com.luohj.privileges.service.service.IUserService#getUser(com.luohj.privileges.model.User)
	 */
	@Cacheable(key="#1.userId")
	@Override
	public User getUser(User user) {
		return userDao.getUser(user);
	}
	
}
