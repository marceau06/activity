package com.koedia.activity.activityManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class ActivityManagerApplication extends SpringBootServletInitializer {
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(ActivityManagerApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(ActivityManagerApplication.class, args);
	}
}


// NOTES POUR DOCS

// @Repository : Cette annotation existe depuis Spring 2.0 et sert à identifier un bean de type DAO.
// @Service : Celle-ci identifie le bean comme un service
// @Controller : Utile uniquement si l’on utilise SpringMVC, cette annotation indique un contrôleur Spring MVC.
// @Component : Cette dernière est l’annotation générique pouvant fonctionner pour n’importe quel bean

// Retrieve user
//Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//User user = userService.findUserByEmail(auth.getName());

// Session object 
// Les sessions permettent de transmettre une information entre les requêtes HTTP
//@Autowired
//HttpSession httpSession;
//httpSession.setAttribute(String, Object);
