/**
 * Title: ICommonService.java
 * Copyright: Loxf
 * Company: Loxf Team
 * @author: luohj
 * @version: 1.0
 * @time:  2015-9-10 下午1:05:13
 */
package com.luohj.privileges.service.service;

import com.luohj.privileges.core.model.BaseBean;
import com.luohj.privileges.core.service.IBaseService;

/**
 * @author luohj
 */
public interface ICommonService extends IBaseService {
	boolean isExistsBean(BaseBean bean);
	Long insertBean(BaseBean bean);
}
