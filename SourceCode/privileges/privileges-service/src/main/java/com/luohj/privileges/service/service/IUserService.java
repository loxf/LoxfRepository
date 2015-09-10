/**
 * Title: IUserService.java
 * Copyright: Loxf
 * Company: Loxf Team
 * @author: luohj
 * @version: 1.0
 * @time:  2015-9-10 上午11:02:34
 */
package com.luohj.privileges.service.service;

import com.luohj.privileges.core.service.IBaseService;
import com.luohj.privileges.model.User;

/**
 * @author luohj
 */
public interface IUserService extends IBaseService {
	Long insertUser(User user);
	User getUser(User user);
}
