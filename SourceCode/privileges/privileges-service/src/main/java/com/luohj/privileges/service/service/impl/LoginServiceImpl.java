package com.luohj.privileges.service.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.luohj.privileges.core.service.impl.AbstractBaseService;
import com.luohj.privileges.core.tags.CacheRefresh;
import com.luohj.privileges.dao.IUserDao;
import com.luohj.privileges.model.User;
import com.luohj.privileges.service.service.ILoginService;

@Service
public class LoginServiceImpl extends AbstractBaseService implements
		ILoginService {
	
	@Resource
	private IUserDao userDao;

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.luohj.privileges.service.service.ILoginService#login(com.luohj.privileges.model.User)
	 */
	@Override
	public User login(User user) {
		return userDao.login(user);
	}

}
