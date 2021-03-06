package com.hrm.steps;

import java.security.InvalidParameterException;

import com.hrm.datagen.EmployeeDatagen;
import com.hrm.entity.Employee;
import com.hrm.enums.UserRole;
import com.hrm.page.HomePage;
import com.hrm.page.LoginPage;
import com.hrm.page.pim.AddEmployeePage;
import com.hrm.utils.ConfigUtils;

public class LoginSteps {
	LoginPage loginPage;
	HomePage homePage;
	AddEmployeePage addEmployeePage;

	public void loginAsUser(UserRole userRole) {
		String username = "", password = "";
		switch (userRole) {
		case Admin:
			username = ConfigUtils.getAdminUsername();
			password = ConfigUtils.getAdminUserPassword();
			break;
		case ESS:
			username = ConfigUtils.getEssUsername();
			password = ConfigUtils.getEssUserPassword();
			break;
		default:
			throw new InvalidParameterException();
		}
		loginPage.enterLoginCredentials(username, password);
		if (loginPage.isCredentialError() && userRole == UserRole.ESS) {
			loginAsUser(UserRole.Admin);
			addEmployeePage.navigateToPage();
			Employee employee = new EmployeeDatagen().createEmployeeWithUser(new Employee());
			employee.getUser().setUsername(username);
			employee.getUser().setPassword(password);
			addEmployeePage.addNewEmployeeWithLoginDetails(employee);
			addEmployeePage.logout();
			loginPage.enterLoginCredentials(username, password);
		}
		homePage.shouldBeDisplayed();
	}

	public void openLoginPage() {
		loginPage.open();
	}

	public void checkMenusInHomePage(UserRole userRole) {
		homePage.checkForMenusDisplayed(userRole);
	}

}
