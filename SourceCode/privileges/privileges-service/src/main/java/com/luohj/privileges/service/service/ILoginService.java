package com.luohj.privileges.service.service;

import com.luohj.privileges.core.service.IBaseService;
import com.luohj.privileges.model.User;

/**
 * @author luohj
 */
public interface ILoginService extends IBaseService {
	User login(User user);
}
