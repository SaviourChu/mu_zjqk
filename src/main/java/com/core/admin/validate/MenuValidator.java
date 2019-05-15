/*
 *  Copyright 2014-2015 snakerflow.com
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.core.admin.validate;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.core.admin.model.Menu;

/**
 * MenuValidator.
 */
public class MenuValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("menu.name", "nameMsg", "请输入菜单名称!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Menu.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/security/menu/save"))
			controller.render("menuAdd.jsp");
		else if (actionKey.equals("/security/menu/update"))
			controller.render("menuEdit.jsp");
	}
}
