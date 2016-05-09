package com.luohj.privileges.dao;


import com.luohj.privileges.core.dao.IBaseDao;
import com.luohj.privileges.core.model.BaseBean;

public interface ICommonDao extends IBaseDao {
	boolean isExistsBean(BaseBean bean);
	Long insertBean(BaseBean bean);
}
