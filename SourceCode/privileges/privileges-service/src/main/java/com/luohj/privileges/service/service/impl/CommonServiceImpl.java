/**
 * Title: CommonServiceImpl.java
 * Copyright: Loxf
 * Company: Loxf Team
 * @author: luohj
 * @version: 1.0
 * @time:  2015-9-10 下午1:07:29
 */
package com.luohj.privileges.service.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.luohj.privileges.core.model.BaseBean;
import com.luohj.privileges.core.service.impl.AbstractBaseService;
import com.luohj.privileges.dao.ICommonDao;
import com.luohj.privileges.service.service.ICommonService;

/**
 * @author luohj
 */
@Service("commonService")
public class CommonServiceImpl extends AbstractBaseService implements ICommonService {

	@Resource
	private ICommonDao commonDao;
	/** (non-Javadoc)
	 * @see com.luohj.privileges.service.service.ICommonService#isExistsBean(com.luohj.privileges.core.model.BaseBean)
	 */
	@Override
	public boolean isExistsBean(BaseBean bean) {
		// TODO Auto-generated method stub
		return commonDao.isExistsBean(bean);
	}

	/** (non-Javadoc)
	 * @see com.luohj.privileges.service.service.ICommonService#insertBean(com.luohj.privileges.core.model.BaseBean)
	 */
	@Override
	public Long insertBean(BaseBean bean) {
		// TODO Auto-generated method stub
		return commonDao.insertBean(bean);
	}

}
