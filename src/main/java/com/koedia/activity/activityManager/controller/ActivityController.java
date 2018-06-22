package com.koedia.activity.activityManager.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.koedia.activity.activityManager.model.entity.Activity;
import com.koedia.activity.activityManager.model.entity.Activity.StepCategory;
import com.koedia.activity.activityManager.model.entity.Activity.StepDescription;
import com.koedia.activity.activityManager.model.entity.Activity.StepMeetingPoint;
import com.koedia.activity.activityManager.model.entity.Activity.StepPeriod;
import com.koedia.activity.activityManager.model.entity.Activity.StepSchedules;
import com.koedia.activity.activityManager.model.entity.Activity.StepTitle;
import com.koedia.activity.activityManager.model.entity.Schedule;
import com.koedia.activity.activityManager.model.entity.Session;
import com.koedia.activity.activityManager.model.entity.User;
import com.koedia.activity.activityManager.service.ActivityService;
import com.koedia.common.tool.StringUtil;


@Controller
@ComponentScan
@RequestMapping("activity")
public class ActivityController {

	private static String UPLOADED_FOLDER_ABSOLUTE= "/home/user/workspace_web_escurity/activityManager/src/main/resources/static/img/stored/";
	
	private static String UPLOAD_FOLDER_RELATIVE= "../img/stored/";
	
	private final static Map<String, Integer> inputStepNumber = new HashMap<String, Integer>();
	private static ArrayList<Integer> validatedSteps = new ArrayList<Integer>();

	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	HttpSession httpSession;
	
	protected final static Logger logger = Logger.getLogger(ActivityController.class);
	
	
	/********************************** Init method ******************************/
	@PostConstruct
	private static void init() {
       validatedSteps.clear();
	   inputStepNumber.put("title", 0);
	   inputStepNumber.put("category", 1);
	   inputStepNumber.put("descriptionFre", 2);
	   inputStepNumber.put("descriptionEng", 2);
	   inputStepNumber.put("descriptionEsp", 2);
	   inputStepNumber.put("meetingPoint", 3);
	   inputStepNumber.put("beginDate", 4);
	   inputStepNumber.put("endDate", 5);
	   inputStepNumber.put("images", 6);
	   inputStepNumber.put("participationCriterias", 7);
	   inputStepNumber.put("price", 8);
	   inputStepNumber.put("paxType", 9);
	   inputStepNumber.put("groupCapacity", 10);
	}
	
	@InitBinder     
	public void initBinder(WebDataBinder binder) {  
		// Converts empty strings into null when a form is submitted 
	   binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));  
	   // Converts input text to date before validation
//       CustomDateEditor editor = new CustomDateEditor(new SimpleDateFormat("dd/mm/yy"), true);
//       binder.registerCustomEditor(Date.class, editor);
       
       binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/mm/yy"), true, 10)); 
	}
	
	/********************************** Redirection to activity creation form ******************************/
	
	@GetMapping("create")
	public ModelAndView goToCreationActivity(Activity activity) {
		
		// Initialiser la liste des horaires en créant un horaire pour chacun des jours de la semaine
		List<Schedule> usualSchedules = new ArrayList<Schedule>();
		for (int i = 0; i < 7; i++) {
			Schedule s = new Schedule(i);
			for (int j = 0; j < 9; j++) {
				s.addSession(new Session(j));
			}
			usualSchedules.add(s);
		}
		activity.setUsualSchedules(usualSchedules);
		
		ModelAndView mav = new ModelAndView("activities-creation");

		return mav;
	}
	
	
	/********************************** Redirection to activity update form *******************************/
	
	@GetMapping("modification")
	public ModelAndView goToUpdateActivity(Activity activity) {
		ModelAndView mav = new ModelAndView("activities-update");
		return mav;
	}
	
	/********************************** Redirection activities list ***************************************/
	
	@GetMapping("list")
	public ModelAndView goToListActivity(Activity activity) {
		ModelAndView mav = new ModelAndView("activities-list");
		
		// Récupérer les activités créées par l'utilisateur
		List<Activity> allActivitiesForUser = activityService.findAllByUserId(8);
		
		mav.addObject("activitiesList", allActivitiesForUser);
		
		System.out.println(allActivitiesForUser.size());
		
		return mav;
	}
	
	
	/********************************** Enregistrer une activité *******************************************/

	public void saveActivity(Activity activity) {
		activity.setCreationDate(new Date(Calendar.getInstance().getTime().getTime()));
		try{
			activityService.saveActivity(activity);
		} catch(Exception ex) {
		}
	}

	/********************************** Mettre à jour une activité ***********************************************/

	@PostMapping("update")
	@ModelAttribute("activity")
	public ModelAndView updateActivity(@ModelAttribute("activity") Activity activity) {
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("activities-overview");
		
		// Traitement & enregistrement en DB
		if (activity != null) {
			activityService.saveActivity(activity);
		}
		return mav;
	}
	
	
	/********************************** Activer / Désactiver une activité ****************************************/
	
	@PostMapping("toggle")
	public ModelAndView toggleActivity(@RequestParam String activityId) {	
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("redirect:/account");
		
		if (StringUtil.isPresent(activityId)) {
		
			// Récupérer l'activité à activer/désactiver
			Activity activityToUpdate = activityService.findById(Integer.valueOf(activityId));
			
			if (activityToUpdate != null) {
				
				// Modifier la colonne "active" pour activer/desactiver l'activite
				activityToUpdate.setActive(!activityToUpdate.isActive());
	
				// Traitement & enregistrement en DB
				activityService.saveActivity(activityToUpdate);
			}
			// Retrieve user's informations
			retrieveUserInfos(mav);
		}
		
		return mav;
	}
	
	/********************************** Supprimer une activité **********************************************************/
	@PostMapping("remove")
	public ModelAndView removeActivity(@RequestParam String activityId) {
		
		if (StringUtil.isPresent(activityId)) {
				// Traitement & enregistrement en DB
				activityService.deleteById(Integer.valueOf(activityId));
			}
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("account");
		
		// Retrieve user's informations
		retrieveUserInfos(mav);
		
		return mav;
	}
	
	@GetMapping("remove")
	public ModelAndView returnToAccountAfterRemove(Activity activity) {
		ModelAndView mav = new ModelAndView("activities-update");
		return mav;
	}
	
	
	/********************************** Exporter une activité **********************************************************/
	@PostMapping("export")
	public ModelAndView exportActivity(@RequestParam String activityId) {
		
	 String CSV_SEPARATOR = ",";
	 
	 if (StringUtil.isPresent(activityId)) {
			
			// Récupérer l'activité à activer/désactiver
			Activity activityToexport = activityService.findById(Integer.valueOf(activityId));

			 try
				{
				    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("activity.csv"), "UTF-8"));
		
				    StringBuffer oneLine = new StringBuffer();
				    oneLine.append(activityToexport.getId() <=0 ? "" : activityToexport.getId());
				    oneLine.append(CSV_SEPARATOR);
				    oneLine.append(activityToexport.getTitle().trim().length() == 0? "" : activityToexport.getTitle());
				    oneLine.append(CSV_SEPARATOR);
		            bw.write(oneLine.toString());
				    bw.newLine();
				    bw.flush();
				    bw.close();
				}
			 	catch (IOException e){}
			 }
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("account");
		
		// Retrieve user's informations
		retrieveUserInfos(mav);
		
		return mav;
	}
	
	/********************************** Modifier une activité **********************************************************/
	@GetMapping("edit")
	public ModelAndView editActivity(@RequestParam String activityId) {
		
		if (StringUtil.isPresent(activityId)) {
				// Mise a jour de l'activité
			}
		
		// Redirection vers page compte 
		ModelAndView mav = new ModelAndView("account");
		
		return mav;
	}
	
	
	/********************************** Méthodes utiles **********************************************************/

	private void retrieveUserInfos(ModelAndView mav) {
		// Retrieve user
		User user = (User)httpSession.getAttribute("user");
		
		// Retrieve activities created by user
		List<Activity> allActivitiesForUser = activityService.findAllByUserId((Integer)httpSession.getAttribute("userId"));
		
		// Add users informations in account view
		mav.addObject("activitiesList", allActivitiesForUser);
		mav.addObject("numberActivities", allActivitiesForUser.size());
		mav.addObject("userId", user.getId());
		mav.addObject("email", user.getEmail());
		mav.addObject("address", user.getAddress());
		mav.addObject("websiteAddress", user.getWebsiteAddress());
		mav.addObject("phone", user.getPhone());
		
		// TODO Add all activities in the model to update them
		for (int i= 0; i < allActivitiesForUser.size(); i++) {
			mav.addObject(allActivitiesForUser.get(i));
		}
	}
	
	
	private void saveImage(Activity activity) {
		
		String picFileEndName = "mainpic" + Math.round(Math.random()*101);
		
		Integer userId = (Integer)httpSession.getAttribute("userId");
		
		if (userId != null) {
			activity.setUserId(userId);
			picFileEndName += "_user" + userId.toString();
		}
		
		String absolutePicturePath = UPLOADED_FOLDER_ABSOLUTE + picFileEndName + ".jpg";
		String relativePicturePath = UPLOAD_FOLDER_RELATIVE + picFileEndName + ".jpg";

		MultipartFile mainPictureFile = activity.getMainPictureFile();
				 
		if (mainPictureFile != null && mainPictureFile.getSize() > 0) {
			
	        if (!mainPictureFile.getOriginalFilename().equals("")) {
	        	activity.setMainPicture(relativePicturePath);
	            try {
					 mainPictureFile.transferTo(new File(absolutePicturePath));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
		}

	}
	
	
	/********************************** Page de TEST **************************************************************/
	@GetMapping("test")
	private ModelAndView timepickerExample(@ModelAttribute("activity") Activity activity, Integer stepNumber) {
	
		// Initialiser la liste des horaires en créant un horaire pour chacun des jours de la semaine
		List<Schedule> usualSchedules = new ArrayList<Schedule>();
		for (int i = 0; i < 7; i++) {
			Schedule s = new Schedule(i);
			s.setWeekdayName("weekDay N°"+i);
			for (int j = 0; j < 9; j++) {
				s.addSession(new Session(j));
			}
			usualSchedules.add(s);
		}
		activity.setUsualSchedules(usualSchedules);
		
		stepNumber = 0;
		
		ModelAndView mav = new ModelAndView("test");
//		mav.addObject("TestJS", new String("test javascript"));
		mav.addObject("stepNumber", stepNumber);
		return mav;
	}

	/******************* Validators ***************************/
	
	
	@PostMapping("stepTitle")
	public ModelAndView validateTitle(@Validated(Activity.StepTitle.class) Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("test");
	    
	    // Spring annotations validation
	    if (result.hasErrors()) {
	    	// Invalid
	    	mav.addObject("stepNumber", 0);
	    } else if(!validateProperty(activity, "title", result, StepTitle.class)) {
	    	// JSR 303 bean validation
	    	// Invalid
	    	mav.addObject("stepNumber", 0);
	    } else {
	    	// Valid
	    	mav.addObject("stepNumber", 1);
	    }
	    
	    mav.addObject("activity", activity);
	    return mav;
	}
	
	@PostMapping("stepCategory")
	public ModelAndView validateCategory(@Validated(Activity.StepCategory.class) Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("test");
	    
	    // Spring annotations validation
	    if (result.hasErrors()) {
	    	// Invalid
	    	mav.addObject("stepNumber", 1);
	    } else if(!validateProperty(activity, "category", result, StepCategory.class)) {
	    	// JSR 303 bean validation
	    	// Invalid
	    	mav.addObject("stepNumber", 1);
	    } else {
	    	// Valid
	    	mav.addObject("stepNumber", 2);
	    }
	    
	    mav.addObject("activity", activity);
	    return mav;
	}

	@PostMapping("stepDescription")
	public ModelAndView validateDescription(@Validated(Activity.StepDescription.class) Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("test");
	    
	    if(!validateProperty(activity, "descriptionFre", result, StepDescription.class)) {
	    	// Invalid
	    	mav.addObject("stepNumber", 2);
	    } else {
	    	// Valid
	    	mav.addObject("stepNumber", 3);
	    }
	    
	    return mav;
	}

	@PostMapping("stepMeetingPoint")
	public ModelAndView validateMeetingPoint(@Validated(Activity.StepMeetingPoint.class) Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("test");
	    
	    if (result.hasErrors()) {
	    	// Invalid
	    	mav.addObject("stepNumber", 3);
	    } else if(!validateProperty(activity, "meetingPoint", result, StepMeetingPoint.class)) {
	    	// Invalid
	    	mav.addObject("stepNumber", 3);
	    } else {
	    	// Valid
	    	mav.addObject("stepNumber", 4);
	    }
	    
	    return mav;
	}
	@PostMapping("stepPeriod")
	public ModelAndView validatePeriod(@Validated(Activity.StepPeriod.class) Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("test");
	    
	    if (result.hasErrors()) {
	    	// Invalid
	    	System.out.println(result.getErrorCount());
	    	mav.addObject("stepNumber", 4);
	    } else if(!validateProperty(activity, "meetingPoint", result, StepPeriod.class)) {
	    	// Invalid
	    	mav.addObject("stepNumber", 4);
	    } else {
	    	// Valid
	    	mav.addObject("stepNumber", 5);
	    }
	    
	    return mav;
	}
	
	
	@PostMapping("stepSchedules")
	public ModelAndView validateSchedules(@Validated(Activity.StepSchedules.class) Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("test");
	    mav.addObject("activity", activity);
	    mav.addObject("stepNumber", 5);
//	    if (result.hasErrors()) {
//	    	// Invalid
//	    	System.out.println(result.getErrorCount());
//	    	mav.addObject("stepNumber", 4);
//	    } else if(!validateProperty(activity, "meetingPoint", result, StepSchedules.class)) {
//	    	// Invalid
//	    	mav.addObject("stepNumber", 4);
//	    } else {
//	    	// Valid
//	    	mav.addObject("stepNumber", 5);
//	    }
	    
	    return mav;
	}
	
	@PostMapping("stepImages")
	public ModelAndView validateImages(@Validated(Activity.StepImages.class) Activity activity, Errors errors) {
	    ModelAndView mav = new ModelAndView("test");
	    return mav;
	}
	
	@PostMapping("stepParticipationCriterias")
	public ModelAndView validateParticipationCriterias(@Validated(Activity.StepParticipationCriterias.class) Activity activity, Errors errors) {
	    ModelAndView mav = new ModelAndView("test");
	    return mav;
	}
	
	@PostMapping("stepPaxs")
	public ModelAndView validatePaxs(@Validated(Activity.StepPaxs.class) Activity activity, Errors errors) {
	    ModelAndView mav = new ModelAndView("test");
	    return mav;
	}
	
	@PostMapping("stepGroupCapacity")
	public ModelAndView validateGroupCapacity(@Validated(Activity.StepGroupCapacity.class) Activity activity, Errors errors) {
	    ModelAndView mav = new ModelAndView("test");
	    return mav;
	}
	
	private static boolean validateProperty(Activity activity, String propertyToValidate, BindingResult result, Class<?>... groupes) {
		 
		 Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//		 Set<ConstraintViolation<Activity>> constraintViolations = validator.validateProperty(activity, propertyToValidate);
		 
		 Set<ConstraintViolation<Activity>> constraintViolations = validator.validate(activity, groupes);

		 
		 if (constraintViolations.size() > 0) {
			 System.out.println("Unable to validate bean data : ");
			 
			 for (ConstraintViolation<Activity> constraints : constraintViolations) {
				 
				 System.out.println("  " + constraints.getRootBeanClass().getSimpleName() + "." + constraints.getPropertyPath() + " " + constraints.getMessage());
				 result.rejectValue(constraints.getPropertyPath().toString(), constraints.getMessageTemplate().replaceAll("[{}]", ""));
			 }
			 return false;
		 } else {
			 System.out.println("Bean data are valid");
			 return true;
		 }
	}

}

	
	
	

