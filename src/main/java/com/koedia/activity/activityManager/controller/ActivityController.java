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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.koedia.activity.activityManager.model.entity.Activity;
import com.koedia.activity.activityManager.model.entity.Activity.StepCategory;
import com.koedia.activity.activityManager.model.entity.Activity.StepDefaultPrice;
import com.koedia.activity.activityManager.model.entity.Activity.StepDescription;
import com.koedia.activity.activityManager.model.entity.Activity.StepGroupCapacity;
import com.koedia.activity.activityManager.model.entity.Activity.StepImages;
import com.koedia.activity.activityManager.model.entity.Activity.StepMeetingPoint;
import com.koedia.activity.activityManager.model.entity.Activity.StepParticipationCriterias;
import com.koedia.activity.activityManager.model.entity.Activity.StepPeriod;
import com.koedia.activity.activityManager.model.entity.Activity.StepTitle;
import com.koedia.activity.activityManager.model.entity.Image;
import com.koedia.activity.activityManager.model.entity.PaxPrice;
import com.koedia.activity.activityManager.model.entity.Schedule;
import com.koedia.activity.activityManager.model.entity.Session;
import com.koedia.activity.activityManager.model.entity.User;
import com.koedia.activity.activityManager.service.ActivityService;
import com.koedia.common.tool.StringUtil;


@Controller
@ComponentScan
@RequestMapping("activity")
@SessionAttributes(value = "activity", types={Activity.class})
public class ActivityController {

	private static String UPLOADED_FOLDER_ABSOLUTE= "/home/user/workspace_web_escurity/activityManager/src/main/resources/static/img/stored/";
	
	private static String UPLOAD_FOLDER_RELATIVE= "../img/stored/";
	
	private static ArrayList<String> validatedSteps = new ArrayList<String>();

	@Autowired
	private ActivityService activityService;
	
	@Autowired
	HttpSession httpSession;
	
	protected final static Logger logger = Logger.getLogger(ActivityController.class);
	
	
	/********************************** Init method ******************************/
//	@PostConstruct
//	private static void init() {
//       validatedSteps.clear();
//	}
	
	@InitBinder     
	public void initBinder(WebDataBinder binder) {  
	   binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));  
	   binder.registerCustomEditor(Integer.class, null, new CustomNumberEditor(Integer.class, true));
       binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/mm/yy"), true, 10)); 
	}
	
	@ModelAttribute("activity")
	public Activity populateForm() {
	    return new Activity();
	}
	
	/********************************** Redirection to activity creation form ******************************/
	@GetMapping("create")
	private ModelAndView timepickerExample(@ModelAttribute("activity") Activity activity) {
	
		ModelAndView mav = new ModelAndView("activities-creation");
		
		// TODO : Si le user souhaite reprendre son formulaire dans l'état où il l'avait laissé
		validatedSteps.clear();
		activity = new Activity();
		
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
		
		// Initialiser la première div à afficher
		int stepNumber = 0;
		int currentTab = 0;
		
		// Initialiser la liste des images
		activity.setImages(new ArrayList<Image>());
		activity.getImages().add(new Image());

		// Initialiser la liste des paxs
		activity.setPaxs(new ArrayList<PaxPrice>());
		activity.getPaxs().add(new PaxPrice());
		
		mav.addObject("activity", activity);
		mav.addObject("currentTab", currentTab);
		mav.addObject("stepNumber", stepNumber);
		mav.addObject("validatedStep", validatedSteps);
		
		mav.addObject("activity", activity);
		
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
		activity.setUserId(getUserFromSession().getId());
		
		for (Iterator<Image> itr = activity.getImages().iterator(); itr.hasNext();) {
		      if (itr.next().getPath() == null) { 
		    	  itr.remove(); 
		      }
		 }
		
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
			addUserInfosToView(mav);
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
		addUserInfosToView(mav);
		
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
		addUserInfosToView(mav);
		
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

	private void addUserInfosToView(ModelAndView mav) {
		// Retrieve user
		User user = getUserFromSession();
		
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
	
	private User getUserFromSession() {
		return (User)httpSession.getAttribute("user");
	}
	
	
	private void saveImage(Image image) {
		
		String fileName = image.getFile().getOriginalFilename();
		
		String absolutePicturePath = UPLOADED_FOLDER_ABSOLUTE + fileName;
		String relativePicturePath = UPLOAD_FOLDER_RELATIVE + fileName;

		if (image.getFile() != null && image.getFile().getSize() > 0) {
			
	    try {
	    		image.getFile().transferTo(new File(absolutePicturePath));
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    image.setPath(relativePicturePath);
	    image.setUrl("http://activity.koedia.com:8080/static/img/" + fileName);
		}

	}
	
	
	/******************* Validators ***************************/
	
	/**
	 * Title validator
	 * @param activity
	 * @param result
	 * @return mav
	 */
	@PostMapping("stepTitle")
	@Validated(Activity.StepTitle.class)
	public ModelAndView validateTitle(@ModelAttribute("activity") Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepTitle";
	    boolean isValid = false;
	    
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
	    	isValid  = true;
	    	// Got to next step
	    	mav.addObject("stepNumber", 1);
	    }
	    
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("activity", activity);
	    return mav;
	}
	
	/**
	 * Category validator
	 * @param activity
	 * @param result
	 * @return mav
	 */
	@PostMapping("stepCategory")
	@Validated(Activity.StepCategory.class)
	public ModelAndView validateCategory(@ModelAttribute("activity") Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepCategory";
	    boolean isValid = false;
	    
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
	    	isValid  = true;
	    	mav.addObject("stepNumber", 2);
	    }
	    
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("activity", activity);
	    return mav;
	}

	/**
	 * Description validator
	 * @param activity
	 * @param result
	 * @return mav
	 */
	@PostMapping("stepDescription")
	@Validated(Activity.StepDescription.class) 
	public ModelAndView validateDescription(@ModelAttribute("activity") Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepDescription";
	    boolean isValid = false;
	    
	    if (result.hasErrors()) {
	    	// Invalid
	    	mav.addObject("stepNumber", 2);
	    } else if(!validateProperty(activity, "descriptionFre", result, StepDescription.class)) {
	    	// Invalid
	    	mav.addObject("stepNumber", 2);
	    } else {
	    	// Valid
	    	isValid  = true;
	    	mav.addObject("stepNumber", 3);
	    }
	    
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("activity", activity);
	    return mav;
	}

	/**
	 * Meeting point validator
	 * @param activity
	 * @param result
	 * @return mav
	 */
	@PostMapping("stepMeetingPoint")
	@Validated(Activity.StepMeetingPoint.class) 
	public ModelAndView validateMeetingPoint(@ModelAttribute("activity") Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepMeetingPoint";
	    boolean isValid = false;
	    
	    if (result.hasErrors()) {
	    	// Invalid
	    	mav.addObject("stepNumber", 3);
	    } else if(!validateGroups(activity, result, StepMeetingPoint.class)) {
	    	// Invalid
	    	mav.addObject("stepNumber", 3);
	    } else {
	    	// Valid
	    	isValid  = true;
	    	mav.addObject("stepNumber", 4);
	    }
	    
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("activity", activity);
	    return mav;
	}
	
	/**
	 * Period validator
	 * @param activity
	 * @param result
	 * @return mav
	 */
	@PostMapping("stepPeriod")
	@Validated(Activity.StepPeriod.class) 
	public ModelAndView validatePeriod(@ModelAttribute("activity") Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepPeriod";
	    boolean isValid = false;
	    
	    if (result.hasErrors()) {
	    	// Invalid
	    	System.out.println(result.getErrorCount());
	    	mav.addObject("stepNumber", 4);
	    } else if(!validateGroups(activity, result, StepPeriod.class)) {
	    	// Invalid
	    	mav.addObject("stepNumber", 4);
	    } else {
	    	// Valid
	    	isValid = true;
	    	mav.addObject("stepNumber", 5);
	    }
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("activity", activity);
	    return mav;
	}
	
	/**
	 * Schedules validator
	 * @param activity
	 * @param result
	 * @return mav
	 */
	@PostMapping("stepSchedules")
	@Validated(Activity.StepSchedules.class) 
	public ModelAndView validateSchedules(@ModelAttribute("activity") Activity activity, BindingResult result) {
		// TODO
		ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepSchedules";
	    boolean isValid = false;
	    
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("activity", activity);
	    mav.addObject("stepNumber", 5);
	    
	    return mav;
	}
	
	@PostMapping("addExtraSession")
	public ModelAndView addExtraSession(@ModelAttribute List<Session> sessions, Errors errors) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    return mav;
	}
	
	/**
	 * Pictures validator
	 * @param activity
	 * @param results
	 * @return mav
	 */
	@PostMapping("stepImages")
	@Validated(StepImages.class) 
	private ModelAndView carouselBootstrapAdd(@ModelAttribute("activity") Activity activity, BindingResult result) {
		ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepImages";
	    boolean isValid = false;
	    mav.addObject("stepNumber", 6);
		
	    Image imageToSave = activity.getImages().get(activity.getImages().size() - 1);
	    	
	    if (!isImage(imageToSave.getFile().getOriginalFilename()) || result.hasErrors() || !validateProperty(activity, "images", result, StepImages.class)) {
		   	// Invalid
			System.out.println(result.getErrorCount());
		} else {
		   	// Valid
			saveImage(imageToSave);
		   	isValid = true;
		}
	    
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
		mav.addObject("activity", activity);
		return mav;
	}
	
	
	/**
	 * Criterias validator
	 * @param activity
	 * @param result
	 * @return mav
	 */
	@PostMapping("stepParticipationCriterias")
	@Validated(Activity.StepParticipationCriterias.class) 
	public ModelAndView validateParticipationCriterias(@ModelAttribute("activity") Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepParticipationCriterias";
	    boolean isValid = false;
	    
	    if (result.hasErrors()) {
	    	// Invalid
	    	System.out.println(result.getErrorCount());
	    	mav.addObject("stepNumber", 7);
	    } else if(!validateProperty(activity, "minimumAge", result, StepParticipationCriterias.class)) {
	    	// Invalid
	    	mav.addObject("stepNumber", 7);
	    } else {
	    	// Valid
	    	isValid = true;
	    	mav.addObject("stepNumber", 8);
	    }
	    
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("activity", activity);
	    return mav;
	}
	
	/**
	 * Default price validator
	 * @param activity
	 * @param result
	 * @return mav
	 */
	@PostMapping("stepDefaultPrice")
	@Validated(Activity.StepDefaultPrice.class) 
	public ModelAndView validateDefaultPrice(@ModelAttribute("activity") Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepDefaultPrice";
	    boolean isValid = false;
	    
	    if (result.hasErrors()) {
	    	// Invalid
	    	System.out.println(result.getErrorCount());
	    	mav.addObject("stepNumber", 8);
	    } else if(!validatePaxBean(activity.getDefaultPax(), result)) {
//	    	// Invalid
	    	mav.addObject("stepNumber", 8);
	    } else {
	    	// Valid
	    	isValid = true;
	    	mav.addObject("stepNumber", 9);
	    }
	    
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("activity", activity);
	    return mav;
	}
	
	/**
	 * Paxs list validator
	 * @param activity
	 * @param result
	 * @return
	 */
	@PostMapping("stepPaxs")
	@Validated(Activity.StepPaxs.class) 
	public ModelAndView validatePaxs(@ModelAttribute("activity") Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepPaxs";
	    boolean isValid = false;
	    
	    // TODO
	    if (result.hasErrors()) {
	    	// Invalid
	    	System.out.println(result.getErrorCount());
	    } else if (!validateProperty(activity, "paxs", result, StepDefaultPrice.class)) {
	    	// Invalid
	    } else {
	    	// Valid
	    	isValid = true;
	    	activity.getPaxs().add(new PaxPrice());
	    }
	    
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("showCreatedPax", isValid);
    	mav.addObject("stepNumber", 9);
	    return mav;
	}
	
	/**
	 * Group capacity validator
	 * @param activity
	 * @param result
	 * @return mav
	 */
	@PostMapping("stepGroupCapacity")
	@Validated(Activity.StepGroupCapacity.class) 
	public ModelAndView validateGroupCapacity(@ModelAttribute("activity") Activity activity, BindingResult result) {
	    ModelAndView mav = new ModelAndView("activities-creation");
	    String stepName = "stepGroupCapacity";
	    boolean isValid = false;
	    
	    if (result.hasErrors()) {
	    	// Invalid
	    	System.out.println(result.getErrorCount());
	    	mav.addObject("stepNumber", 10);
	    } else if (!validateProperty(activity, "maxCapacityGroup", result, StepGroupCapacity.class) || !validateProperty(activity, "minCapacityGroup", result, StepGroupCapacity.class)) {
	    	// Invalid
	    	mav.addObject("stepNumber", 10);
	    } else {
	    	// Valid
	    	isValid = true;
	    	mav.addObject("stepNumber", 11);
	    }
	    if(isValid) {
	    	if (!validatedSteps.contains(stepName)) {
	    		validatedSteps.add(stepName);
	    	}
	    } else {
	    	if (validatedSteps.contains(stepName)) {
	    		validatedSteps.remove(stepName);
	    	}
	    }
	    mav.addObject("validatedSteps", validatedSteps);
	    mav.addObject("activity", activity);
	    return mav;
	}
	
	 @PostMapping("validateActivity")
	 @Valid 
	 public ModelAndView validateActivity(@ModelAttribute("activity") Activity activity, BindingResult result) {
		 ModelAndView mav = new ModelAndView("activities-overview");
		 
		 if (result.hasErrors()) {
		    	// Invalid
		    	System.out.println(result.getErrorCount());
		    	mav.setViewName("activities-creation");
		    	mav.addObject("stepNumber", 11);
		    } else if (!validateActivityBean(activity, result)) {
		    	// Invalid
		    	mav.setViewName("activities-creation");
		    	mav.addObject("stepNumber", 11);
		    } else {
		    	// Valid
		    	mav.addObject("activity", activity);
				// Traitement & enregistrement en DB
		    	saveActivity(activity);
		    }
		 mav.addObject("activityCreated", activity);
		 mav.addObject("validatedSteps", validatedSteps);
		 
		 return mav;
	 }
	
	private static boolean validateProperty(Activity activity, String propertyToValidate, BindingResult result, Class<?>... groupes) {
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		 Set<ConstraintViolation<Activity>> constraintViolations = validator.validateProperty(activity, propertyToValidate, groupes);

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
	
	private static boolean validateGroups(Activity activity, BindingResult result, Class<?>... groupes) {
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
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
	
	
	private static boolean validateActivityBean(Activity activity, BindingResult result) {
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<Activity>> constraintViolations = validator.validate(activity);
		
		if (constraintViolations.size() > 0 ) {
			System.out.println("Unable to validate bean data : ");
			
			for (ConstraintViolation<Activity> constraints : constraintViolations) {
				System.out.println(constraints.getRootBeanClass().getSimpleName() + "." + constraints.getPropertyPath() + " " + constraints.getMessage());
				result.rejectValue(constraints.getPropertyPath().toString(), constraints.getMessageTemplate().replaceAll("[{}]", ""));
			}
			return false;
		} else {
			System.out.println("Bean data are valid");
			return true;
		}
	}
	
	private static boolean validatePaxBean(PaxPrice pax, BindingResult result) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<PaxPrice>> constraintViolations = validator.validate(pax);
		
		if (constraintViolations.size() > 0 ) {
			System.out.println("Unable to validate bean data : ");
			
			for (ConstraintViolation<PaxPrice> constraints : constraintViolations) {
				System.out.println(constraints.getRootBeanClass().getSimpleName() + "." + constraints.getPropertyPath() + " " + constraints.getMessage());
				result.rejectValue(constraints.getPropertyPath().toString(), constraints.getMessageTemplate().replaceAll("[{}]", ""));
			}
			return false;
		} else {
			System.out.println("Bean data are valid");
			return true;
		}
	}



	private boolean isImage(String filepath) {
        File f = new File(filepath);
        String mimetype= new MimetypesFileTypeMap().getContentType(f);
        String type = mimetype.split("/")[0];
        
        if(type.equals("image")) {
            System.out.println("It's an image");
            return true;
        } else { 
            System.out.println("It's NOT an image");
            return false;
    	}
	}
}