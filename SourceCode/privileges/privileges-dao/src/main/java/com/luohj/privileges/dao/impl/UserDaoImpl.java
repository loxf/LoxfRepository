package com.luohj.privileges.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.luohj.privileges.core.dao.impl.AbstractBaseDao;
import com.luohj.privileges.dao.IUserDao;
import com.luohj.privileges.model.User;

@Repository("userDao")
public class UserDaoImpl extends AbstractBaseDao implements IUserDao {
	private static Logger logger = Logger.getLogger(UserDaoImpl.class);

	public UserDaoImpl(){
		logger.debug("UserDaoImpl..........成功加载！！！");
	}
	
	@Override
	public User getUser(User ur) {
		User user = (User)getSqlClient().queryForObject("systemMgr.getUser",ur.getUserId());
		return user;
	}

	/** (non-Javadoc)
	 * @see com.luohj.privileges.dao.IUserDao#insertUser(com.luohj.privileges.model.User)
	 */
	@Override
	public Long insertUser(User user) {
		return insertBean(user);
	}

	@Override
	public User login(User ur){
		User user = (User)getSqlClient().queryForObject("systemMgr.login",ur);
		return user;
	}

}
