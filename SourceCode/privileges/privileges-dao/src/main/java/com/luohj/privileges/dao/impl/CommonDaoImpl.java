package com.luohj.privileges.dao.impl;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.luohj.privileges.core.dao.impl.AbstractBaseDao;
import com.luohj.privileges.core.model.BaseBean;
import com.luohj.privileges.dao.ICommonDao;

@Repository("commonDao")
public class CommonDaoImpl extends AbstractBaseDao implements ICommonDao {
	private static Logger logger = Logger.getLogger(CommonDaoImpl.class);
	
	public CommonDaoImpl(){
		logger.debug("CommonDaoImpl..........成功加载！！！");
	}
	
	public boolean isExistsBean(BaseBean bean){
		return super.isExistsBean(bean);
	}
	
	public Long insertBean(BaseBean bean){
		return super.insertBean(bean);
	}
}
