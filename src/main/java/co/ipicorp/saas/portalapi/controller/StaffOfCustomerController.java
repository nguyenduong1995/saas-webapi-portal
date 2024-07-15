/**
 * StaffController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hazelcast.internal.ascii.rest.HttpBadRequestException;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.model.StaffOfCustomer;
import co.ipicorp.saas.core.model.dto.StaffOfCustomerSearchCondition;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.StaffOfCustomerService;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.portalapi.dto.StaffOfCustomerDto;
import co.ipicorp.saas.portalapi.form.AvatarUploadForm;
import co.ipicorp.saas.portalapi.form.StaffOfCustomerForm;
import co.ipicorp.saas.portalapi.form.search.StaffOfCustomerSearchForm;
import co.ipicorp.saas.portalapi.form.validator.EmailExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.LoginNameExistedValidator;
import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.component.SystemConfiguration;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.service.EmailService;
import grass.micro.apps.service.exception.ServiceException;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.exception.HttpNotFoundException;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * StaffController. <<< Detail note.
 * 
 * @author thuy nguyen
 * @access public
 */
@RestController
@Api(tags = "Staff Of Customer APIs", description = "all APIs relate to Staff Of Customer")
public class StaffOfCustomerController {

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private StaffOfCustomerService staffOfCustomerService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private FileStorageService fileStorageService;
	
	@Autowired
	private SystemConfiguration config;

	@GetMapping(value = ControllerAction.APP_PORTAL_STAFF_OF_CUSTOMER_ACTION + "/all")
	@ResponseBody
	@Logged
	public ResponseEntity<?> getAll(HttpServletRequest request, HttpServletResponse response) {
		AppControllerSupport support = new AppControllerListingSupport() {

			@Override
			public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response,
					Errors errors, ErrorsKeyConverter errorsProcessor) {
				CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
						.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Customer customer = sessionInfo.getCustomer();
				return StaffOfCustomerController.this.staffOfCustomerService.getAllByCustomerId(customer.getId());
			}

			@Override
			public String getAttributeName() {
				return "staffs";
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
				return DtoFetchingUtils.fetchStaffOfCustomers((List<StaffOfCustomer>) entities);
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_STAFF_OF_CUSTOMER_DETAIL_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> getStaffOfCustomerDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("id") Integer staffOfCustomerId) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				StaffOfCustomer staffOfCustomer = StaffOfCustomerController.this.staffOfCustomerService
						.get(staffOfCustomerId);
				if (staffOfCustomer == null || staffOfCustomer.isDeleted()) {
					throw new HttpNotFoundException();
				}
				getRpcResponse().addAttribute("staffOfCustomer",
						DtoFetchingUtils.fetchStaffOfCustomerInfo(staffOfCustomer));
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_STAFF_OF_CUSTOMER_ACTION + "/count")
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> countStaffOfCustomers(HttpServletRequest request, HttpServletResponse response) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				long inactiveUsers = 0;
				long activeUsers = 0;
				long pendingUsers = 0;
				List<StaffOfCustomer> staffOfCustomers = staffOfCustomerService.getAll();
				if (staffOfCustomers != null && staffOfCustomers.size() > 0) {
					inactiveUsers = staffOfCustomers.stream()
							.filter(staffOfCustomer -> staffOfCustomer.getStatus() == Status.INACTIVE).count();
					activeUsers = staffOfCustomers.stream()
							.filter(staffOfCustomer -> staffOfCustomer.getStatus() == Status.ACTIVE).count();
					pendingUsers = staffOfCustomers.stream()
							.filter(staffOfCustomer -> staffOfCustomer.getStatus() == Status.STATUS_TWO).count();
				}

				getRpcResponse().addAttribute("InactiveUsers", inactiveUsers);
				getRpcResponse().addAttribute("ActiveUsers", activeUsers);
				getRpcResponse().addAttribute("PendingUsers", pendingUsers);
				getRpcResponse().addAttribute("TotalUsers", inactiveUsers + activeUsers + pendingUsers);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_STAFF_OF_CUSTOMER_ACTION)
	@Validation(validators = { EmailExistedValidator.class, LoginNameExistedValidator.class })
	@ResponseBody
	@Logged
	public ResponseEntity<?> createStaffOfCustomer(HttpServletRequest request, HttpServletResponse response,
			@RequestBody StaffOfCustomerForm form) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
						.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Customer customer = sessionInfo.getCustomer();
				if (customer != null) {
					String rawPassword = SystemUtils.getInstance().randomPassword();
					StaffOfCustomer staffOfCustomer = StaffOfCustomerController.this.processSaveStaffOfCustomer(form,
							customer, rawPassword);
					try {
						String content = generateNewAccountEmailContent(form, rawPassword);
						StaffOfCustomerController.this.emailService.sendMail(form.getEmail(),
								"Welcome to IPI SAAS System", content);
					} catch (Exception ex) {
						// do nothing
					}
					getRpcResponse().addAttribute("message", "successful");
					getRpcResponse().addAttribute("staffOfCustomerId", staffOfCustomer.getId());
				} else {
					getRpcResponse().error(ErrorCode.APP_1000_SYSTEM_ERROR, "Session timeout");
				}

			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@PutMapping(value = ControllerAction.APP_PORTAL_STAFF_OF_CUSTOMER_ACTION)
	@Validation(validators = { EmailExistedValidator.class, LoginNameExistedValidator.class })
	@ResponseBody
	@Logged
	public ResponseEntity<?> updateStaffOfCustomer(HttpServletRequest request, HttpServletResponse response,
			@RequestBody StaffOfCustomerForm form) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
						.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Customer customer = sessionInfo.getCustomer();
				if (customer != null) {
					StaffOfCustomer staffOfCustomer = StaffOfCustomerController.this.processUpdateStaffOfCustomer(form,
							customer);
					getRpcResponse().addAttribute("message", "successful");
					getRpcResponse().addAttribute("staffOfCustomer",
							DtoFetchingUtils.fetchStaffOfCustomerInfo(staffOfCustomer));
				} else {
					getRpcResponse().error(ErrorCode.APP_1000_SYSTEM_ERROR, "Session timeout");
				}

			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_STAFF_OF_CUSTOMER_UPLOAD_AVATAR_ACTION)
	@ResponseBody
	@Logged
	public ResponseEntity<?> uploadAvatar(HttpServletRequest request, HttpServletResponse response,
			@RequestBody AvatarUploadForm form) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
						.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Customer customer = sessionInfo.getCustomer();
				if (customer != null) {
					StaffOfCustomer staffOfCustomer = StaffOfCustomerController.this.staffOfCustomerService
							.get(form.getSubjectId());
					if (staffOfCustomer == null || staffOfCustomer.isDeleted()) {
						throw new HttpNotFoundException();
					}
					staffOfCustomer = processUploadAndUpdateAvatar(form.getAvatar(), staffOfCustomer, customer.getId());
					getRpcResponse().addAttribute("message", "successful");
				} else {
					getRpcResponse().error(ErrorCode.APP_1000_SYSTEM_ERROR, "Session timeout");
				}

			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@PostMapping(value = ControllerAction.APP_CHANGE_AVATAR_ACTION)
	@ResponseBody
	@Logged
	public ResponseEntity<?> changeAvatar(HttpServletRequest request, HttpServletResponse response,
			@RequestBody AvatarUploadForm form) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
						.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Customer customer = sessionInfo.getCustomer();
				StaffOfCustomer staffOfCustomer = sessionInfo.getStaffOfCustomer();

				if (staffOfCustomer != null) {
					if (staffOfCustomer == null || staffOfCustomer.isDeleted()) {
						throw new HttpNotFoundException();
					}

					staffOfCustomer = processUploadAndUpdateAvatar(form.getAvatar(), staffOfCustomer, customer.getId());
					getRpcResponse().addAttribute("message", "successful");
				} else {
					getRpcResponse().error(ErrorCode.APP_1000_SYSTEM_ERROR, "Unknown Error");
				}
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_STAFF_OF_CUSTOMER_ACTION + "/search")
	@ResponseBody
	@Logged
	@RequiresAuthentication
	public ResponseEntity<?> search(HttpServletRequest request, HttpServletResponse response,
			@GetBody StaffOfCustomerSearchForm form) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				StaffOfCustomerSearchCondition condition = createSearchCondition(form);
				long count = staffOfCustomerService.count(condition);
				getRpcResponse().addAttribute("count", count);

				List<StaffOfCustomer> list = new LinkedList<>();

				if (count > form.getSegment()) {
					list = staffOfCustomerService.search(condition);
				}

				List<StaffOfCustomerDto> dtos = DtoFetchingUtils.fetchStaffOfCustomers(list);
				getRpcResponse().addAttribute("staffs", dtos);

			}

			private StaffOfCustomerSearchCondition createSearchCondition(StaffOfCustomerSearchForm form) {
				StaffOfCustomerSearchCondition condition = new StaffOfCustomerSearchCondition();
				CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request,
						Constants.APP_SESSION_INFO_KEY);
				Account account = info.getAccount();
				condition.setStaffId(AccountType.CUSTOMER.equals(account.getAccountType()) ? info.getCustomer().getId()
						: info.getStaffOfCustomer().getId());
				condition.setStatus(form.getStatus() != null ? form.getStatus() : Status.ACTIVE.getValue());
				condition.setOffset(form.getOffset());
				condition.setSegment(form.getSegment());
				condition.setLimitSearch(true);
				return condition;
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_STAFF_OF_CUSTOMER_RESET_PASSWORD_ACTION)
	@ResponseBody
	@Logged
	@RequiresAuthentication
	public ResponseEntity<?> resetPassword(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer staffOfCustomerId) {
		AppControllerSupport support = new AppControllerSupport() {
			
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				StaffOfCustomer staffOfCustomer = staffOfCustomerService.get(staffOfCustomerId);
				if (staffOfCustomer == null || staffOfCustomer.isDeleted()) {
					throw new HttpNotFoundException();
				}
				String rawPassword = SystemUtils.getInstance().randomPassword();
				Account account = accountService.getByEmail(staffOfCustomer.getEmail(), AccountType.STAFF_OF_CUSTOMER);
				try {
					accountService.changePassword(account.getId(), rawPassword);
					String content = generateAccountEmailContent(staffOfCustomer, rawPassword);
					String title = config.getProperty(Constants.APP_RESET_PASSWORD_EMAIL_TITLE_KEY, Constants.APP_DEFAULT_RESET_PASSWORD_EMAIL_TITLE);
					StaffOfCustomerController.this.emailService.sendMail(staffOfCustomer.getEmail(), title, content);
					getRpcResponse().addAttribute("message", "successful");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	protected String generateAccountEmailContent(StaffOfCustomer staffOfCustomer, String rawPassword) {
		String content = this.getStaffOfCustomerEmailContent();
		content = content.replace("[[FULLNAME]]", StringEscapeUtils.escapeHtml4(staffOfCustomer.getFullName()));
		content = content.replace("[[PASSWORD]]", StringEscapeUtils.escapeHtml4(rawPassword));
		content = content.replace("[[SUPPORT_EMAIL]]", StringEscapeUtils.escapeHtml4("support@ipisaas.com"));
		return content;
	}

	private String getStaffOfCustomerEmailContent() {
		String content = null;

		BufferedReader br = null;
		try {
			InputStream stream = this.getClass().getResourceAsStream("/reset_password.html");
			br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append(System.getProperty("line.separator"));
			}
			content = sb.toString();
		} catch (Exception ex) {
			throw new ServiceException("GET EMAIL CONTENT ERROR: " + ex.getMessage(), ex);
		} finally {
			try {
				br.close();
			} catch (Exception e2) {
				// do nothing
			}
		}

		return content;
	}
	

	/**
	 * @param form
	 * @param rawPassword
	 * @return
	 */
	protected String generateNewAccountEmailContent(StaffOfCustomerForm form, String rawPassword) {
		String portalUrl = "";
		try {
			portalUrl = SystemConfiguration.getInstance().getProperty(Constants.APP_PORTAL_URL_KEY);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String content = this.getNewStaffOfCustomerEmailContent();
		content = content.replace("[[FULLNAME]]", StringEscapeUtils.escapeHtml4(form.getFullName()));
		content = content.replace("[[USERNAME]]", StringEscapeUtils.escapeHtml4(form.getEmail()));
		content = content.replace("[[PASSWORD]]", StringEscapeUtils.escapeHtml4(rawPassword));
		content = content.replace("[[SUPPORT_EMAIL]]", StringEscapeUtils.escapeHtml4("support@ipisaas.com"));
		content = content.replace("[[PORTAL_URL]]", StringEscapeUtils.escapeHtml4(portalUrl));
		return content;
	}

	/**
	 * @return
	 */
	private String getNewStaffOfCustomerEmailContent() {
		String content = null;

		BufferedReader br = null;
		try {
			InputStream stream = this.getClass().getResourceAsStream("/new_account.html");
			br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append(System.getProperty("line.separator"));
			}
			content = sb.toString();
		} catch (Exception ex) {
			throw new ServiceException("GET EMAIL CONTENT ERROR: " + ex.getMessage(), ex);
		} finally {
			try {
				br.close();
			} catch (Exception e2) {
				// do nothing
			}
		}

		return content;
	}

	/**
	 * @param StaffOfCustomerForm form
	 * @param customer
	 * @return
	 */
	protected StaffOfCustomer processSaveStaffOfCustomer(StaffOfCustomerForm form, Customer customer,
			String rawPassword) {
		StaffOfCustomer staffOfCustomer = new StaffOfCustomer();
		staffOfCustomer = saveStaffOfCustomer(mappingStaffOfCustomerFromForm(staffOfCustomer, form, customer),
				rawPassword);
		// upload and update avatar
		staffOfCustomer = processUploadAndUpdateAvatar(form.getAvatar(), staffOfCustomer, customer.getId());
		return staffOfCustomer;
	}

	/**
	 * @param StaffOfCustomerForm form
	 * @param customer
	 * @return
	 */
	protected StaffOfCustomer processUpdateStaffOfCustomer(StaffOfCustomerForm form, Customer customer) {
		StaffOfCustomer staffOfCustomer = new StaffOfCustomer();
		if (form.getId() == null) {
			throw new HttpBadRequestException("Can not update without staffOfCustomerId");
		}
		staffOfCustomer = this.staffOfCustomerService.get(form.getId());
		if (staffOfCustomer == null || staffOfCustomer.isDeleted()) {
			throw new HttpNotFoundException("Can not find any staffOfCustomer with id: " + form.getId());
		}
		staffOfCustomer = updateStaffOfCustomer(mappingStaffOfCustomerFromForm(staffOfCustomer, form, customer));
		// upload and update avatar
		staffOfCustomer = processUploadAndUpdateAvatar(form.getAvatar(), staffOfCustomer, customer.getId());
		return staffOfCustomer;
	}

	/**
	 * @param avatar
	 * @param staffOfCustomer
	 * @return
	 */
	protected StaffOfCustomer processUploadAndUpdateAvatar(String avatar, StaffOfCustomer staffOfCustomer,
			Integer customerId) {

		if (staffOfCustomer.getAvatar() != null && avatar.contains(staffOfCustomer.getAvatar())) {
			return staffOfCustomer;
		}
		if (StringUtils.isNotEmpty(avatar)) {
			// delete old avatar
			String location = ResourceUrlResolver.getInstance().resolveFtpStaffPath(customerId, "");
			if (StringUtils.isNotEmpty(staffOfCustomer.getAvatar())) {
				this.fileStorageService.deleteFile(location + staffOfCustomer.getAvatar());
			}

			String[] data = avatar.split(",");
			String extension = data[0].split(";")[0].split("/")[1];
			String avatarPath = uploadAvatarToFTP(avatar, customerId, staffOfCustomer.getId());
			staffOfCustomer.setAvatar(avatarPath + "." + extension);
			staffOfCustomer = this.staffOfCustomerService.updatePartial(staffOfCustomer);
		}

		return staffOfCustomer;
	}

	/**
	 * @param avatar
	 * @return
	 */
	private String uploadAvatarToFTP(String avatar, Integer customerId, Integer staffOfCustomerId) {
		String location = ResourceUrlResolver.getInstance().resolveFtpStaffPath(customerId, "");
		String fileName = SystemUtils.getInstance().generateCode("C", customerId, "S", staffOfCustomerId);
		this.fileStorageService.storFile(avatar, location, fileName);
		return fileName;
	}

	/**
	 * @param mappingStaffOfCustomerFromForm
	 * @return
	 */
	private StaffOfCustomer saveStaffOfCustomer(StaffOfCustomer staffOfCustomer, String rawPassword) {
		Account account = createNewAccountOfCustomer(staffOfCustomer, rawPassword);
		staffOfCustomer.setAccount(account);
		this.staffOfCustomerService.saveOrUpdate(staffOfCustomer);
		return staffOfCustomer;
	}

	/**
	 * @param mappingStaffOfCustomerFromForm
	 * @return
	 */
	private StaffOfCustomer updateStaffOfCustomer(StaffOfCustomer staffOfCustomer) {
		Account account = updateAccountInfo(staffOfCustomer);
		staffOfCustomer.setAccount(account);
		this.staffOfCustomerService.updatePartial(staffOfCustomer);
		return staffOfCustomer;
	}

	/**
	 * @param staffOfCustomer
	 * @return
	 */
	private Account updateAccountInfo(StaffOfCustomer staffOfCustomer) {
		Account account = this.accountService.getActivated(staffOfCustomer.getAccount().getId());
		String email = staffOfCustomer.getEmail();
		if (account.getEmail().equals(email)) {
			account.setEmail(email);
			account.setLoginName(email);
		}
		return account;
	}

	/**
	 * @param staffOfCustomer
	 * @return
	 */
	private Account createNewAccountOfCustomer(StaffOfCustomer staffOfCustomer, String rawPassword) {
		Account account = new Account();
		account.setCustomerId(staffOfCustomer.getCustomer().getId());
		account.setAccountType(AccountType.STAFF_OF_CUSTOMER);
		account.setEmail(staffOfCustomer.getEmail());
		account.setLoginName(staffOfCustomer.getEmail());
		String salt = SystemUtils.getInstance().randomPassword() + SystemUtils.getInstance().randomPassword();
		account.setPassword(SystemUtils.getInstance().generatePassword(rawPassword, salt));
		account.setSalt(salt);
		account.setStatus(Status.ACTIVE);

		return account;
	}

	/**
	 * @param staffOfCustomer
	 * @param form
	 * @return
	 */
	private StaffOfCustomer mappingStaffOfCustomerFromForm(StaffOfCustomer staffOfCustomer, StaffOfCustomerForm form,
			Customer customer) {
		staffOfCustomer.setId(form.getId());
		staffOfCustomer.setCustomer(customer);
		staffOfCustomer
				.setEmail(StringUtils.isNotEmpty(form.getEmail()) ? form.getEmail() : staffOfCustomer.getEmail());
		staffOfCustomer.setFullName(
				StringUtils.isNotEmpty(form.getFullName()) ? form.getFullName() : staffOfCustomer.getFullName());
		staffOfCustomer
				.setMobile(StringUtils.isNotEmpty(form.getMobile()) ? form.getMobile() : staffOfCustomer.getMobile());
		staffOfCustomer.setAddress(
				StringUtils.isNotEmpty(form.getAddress()) ? form.getAddress() : staffOfCustomer.getAddress());
		staffOfCustomer
				.setTelephone(StringUtils.isNotEmpty(form.getTel()) ? form.getTel() : staffOfCustomer.getTelephone());
		staffOfCustomer.setTelephoneExt(
				StringUtils.isNotEmpty(form.getTelExt()) ? form.getTelExt() : staffOfCustomer.getTelephoneExt());
		staffOfCustomer.setStatus(staffOfCustomer.getId() == null ? Status.ACTIVE : staffOfCustomer.getStatus());
		return staffOfCustomer;
	}

}
