package me.sived.ryan.controllers;

import me.sived.ryan.service.RyanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class RyanController {

    private final RyanService ryanService;
    private final Logger logger;

    @Autowired
    public RyanController(RyanService ryanService) {
        this.ryanService = ryanService;
        this.logger = LoggerFactory.getLogger(RyanController.class);
        logger.info("RyanController class initialized");
    }

    /**
     *  @return ModelAndView for Main page
     */
    @GetMapping("/")
    public ModelAndView showMain() {
        logger.info("Showing main page");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    /**
     * Shows the page with the cheapest fares from selected airport
     * @author sived
     * @param airport an 3-digit airport code to show the cheapest fares from (for example KBP)
     * @return ModelAndView for CheapestFares page
     */
    @GetMapping("/cheapest/{airport}")
    public ModelAndView showListOfCheapestFaresFrom(@PathVariable String airport) {
        logger.info("Showing a list of cheapest fares from airport " + airport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("fareList", ryanService.getCheapestFaresFrom(airport).toList());
        modelAndView.setViewName("listOfCheapestFares");
        return modelAndView;
    }

    /**
     * Shows the page with all available routes from selected airport
     * @author sived
     * @param airport an 3-digit airport code to show all available routes from (for example KBP)
     * @return ModelAndView for Routes page
     */
    @GetMapping("/routes/{airport}")
    public ModelAndView showListOfRoutesFrom(@PathVariable String airport) {
        logger.info("Showing a list of routes from airport " + airport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("routeList", ryanService.getRoutesFrom(airport));
        modelAndView.setViewName("listOfRoutes");
        return modelAndView;
    }

    /**
     * Shows the page with all available fares for a specific route within a date range
     * @author sived
     * @param depAirport an 3-digit departure airport code of a route to show all available fares from (for example KBP)
     * @param arrAirport an 3-digit arrival airport code of a route to show all available fares to (for example TFN)
     * @param dateFrom start date of a searching date range in YYYY-MM-DD format (for example 2021-01-01)
     * @param dateTo end date of a searching date range in YYYY-MM-DD format (for example 2021-12-31)
     * @return ModelAndView for Cheapest Fares for a Route page
     * @see #showAllFaresForRoute(String, String) in case when dates are not used
     */
    @GetMapping("/fares/{depAirport}/{arrAirport}/{dateFrom}/{dateTo}")
    public ModelAndView showAllFaresForRoute(@PathVariable String depAirport, @PathVariable String arrAirport,
                                             @PathVariable String dateFrom, @PathVariable String dateTo) {
        logger.info("Showing a list of all fares from airport " + depAirport + " to " + arrAirport + " " + dateFrom + " " + dateTo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allFaresForRoute", ryanService.getAllFaresForRouteAndDates(depAirport, arrAirport, dateFrom, dateTo));
        modelAndView.setViewName("listOfAllFaresForRoute");
        return modelAndView;
    }

    /**
     * Shows the page with all available fares for a specific route for all available dates since today
     * @author sived
     * @param depAirport an 3-digit departure airport code of a route to show all available fares from (for example KBP)
     * @param arrAirport an 3-digit arrival airport code of a route to show all available fares to (for example TFN)
     * @return ModelAndView for Cheapest Fares for a Route page
     * @see #showAllFaresForRoute(String, String, String, String) in case when date range is used
     */
    @GetMapping("/fares/{depAirport}/{arrAirport}")
    public ModelAndView showAllFaresForRoute(@PathVariable String depAirport, @PathVariable String arrAirport) {
        logger.info("Showing a list of all fares from airport " + depAirport + " to " + arrAirport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allFaresForRoute", ryanService.getAllFaresForRoute(depAirport, arrAirport));
        modelAndView.setViewName("listOfAllFaresForRoute");
        return modelAndView;
    }

    /**
     * Shows the page with all available fares for a specific route (with a connecting flight) within a date range and length of stay
     * @author sived
     * @param depAirport an 3-digit departure airport code of a route (for example KBP) or 2-digit country code (for example UA)
     * @param arrAirport an 3-digit arrival airport code of a route (for example TFN) or 2-digit country code (for example ES)
     * @param dateFrom start date of a searching date range in YYYY-MM-DD format (for example 2021-01-01)
     * @param dateTo end date of a searching date range in YYYY-MM-DD format (for example 2021-12-31)
     * @param length length range of a stay separated by comma (for example '2,4' for a stay between 2 and 4 days)
     * @return ModelAndView for Teleport page
     * @see #teleport(String, String, String, String) in case if length range is not used
     * @see #teleport(String, String, String) in case if date range is not used
     * @see #teleport(String, String) in case if neither date and length ranges are used
     */
    @GetMapping("/teleport/{depAirport}/{arrAirport}/{dateFrom}/{dateTo}/{length}")
    public ModelAndView teleport(@PathVariable String depAirport, @PathVariable String arrAirport,
                                 @PathVariable String dateFrom, @PathVariable String dateTo,
                                 @PathVariable String length) {
        logger.info("Showing a return teleport feature " + depAirport + " " + arrAirport + " " + dateFrom + " " + dateTo + " " + length);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("teleportList", ryanService.teleportAndBack(depAirport, arrAirport, dateFrom, dateTo, length));
        modelAndView.setViewName("teleportAndBack");
        return modelAndView;
    }

    /**
     * Shows the page with all available fares for a specific route (with a connecting flight) within a date range
     * @author sived
     * @param depAirport an 3-digit departure airport code of a route (for example KBP) or 2-digit country code (for example UA)
     * @param arrAirport an 3-digit arrival airport code of a route (for example TFN) or 2-digit country code (for example ES)
     * @param dateFrom start date of a searching date range in YYYY-MM-DD format (for example 2021-01-01)
     * @param dateTo end date of a searching date range in YYYY-MM-DD format (for example 2021-12-31)
     * @return ModelAndView for Teleport page
     * @see #teleport(String, String, String, String, String) in case if length range is used
     * @see #teleport(String, String, String) in case if date range is not used, but length range is used
     * @see #teleport(String, String) in case if neither date and length ranges are used
     */
    @GetMapping("/teleport/{depAirport}/{arrAirport}/{dateFrom}/{dateTo}")
    public ModelAndView teleport(@PathVariable String depAirport, @PathVariable String arrAirport,
                                 @PathVariable String dateFrom, @PathVariable String dateTo) {
        logger.info("Showing a teleport feature " + depAirport + " " + arrAirport + " " + dateFrom + " " + dateTo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("teleportList", ryanService.teleport(depAirport, arrAirport, dateFrom, dateTo));
        modelAndView.setViewName("teleport");
        return modelAndView;
    }

    /**
     * Shows the page with all available fares for a specific route (with a connecting flight) with a length of stay
     * @author sived
     * @param depAirport an 3-digit departure airport code of a route (for example KBP) or 2-digit country code (for example UA)
     * @param arrAirport an 3-digit arrival airport code of a route (for example TFN) or 2-digit country code (for example ES)
     * @param length length range of a stay separated by comma (for example '2,4' for a stay between 2 and 4 days)
     * @return ModelAndView for Teleport page
     * @see #teleport(String, String, String, String, String) in case if length and date ranges are used
     * @see #teleport(String, String, String, String) in case if date range is used, but not length range
     * @see #teleport(String, String) in case if neither date and length ranges are used
     */
    @GetMapping("/teleport/{depAirport}/{arrAirport}/{length}")
    public ModelAndView teleport(@PathVariable String depAirport, @PathVariable String arrAirport,
                                 @PathVariable String length) {
        logger.info("Showing a return teleport feature " + depAirport + " " + arrAirport + " " + length);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("teleportList", ryanService.teleport(depAirport, arrAirport));
        modelAndView.setViewName("teleport");
        return modelAndView;
    }

    /**
     * Shows the page with all available fares for a specific route (with a connecting flight)
     * @author sived
     * @param depAirport an 3-digit departure airport code of a route (for example KBP) or 2-digit country code (for example UA)
     * @param arrAirport an 3-digit arrival airport code of a route (for example TFN) or 2-digit country code (for example ES)
     * @return ModelAndView for Teleport page
     * @see #teleport(String, String, String, String, String) in case if length and date ranges are used
     * @see #teleport(String, String, String, String) in case if only date range is used
     * @see #teleport(String, String, String) in case if only length range is used
     */
    @GetMapping("/teleport/{depAirport}/{arrAirport}")
    public ModelAndView teleport(@PathVariable String depAirport, @PathVariable String arrAirport) {
        logger.info("Showing a teleport feature " + depAirport + " " + arrAirport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("teleportList", ryanService.teleport(depAirport, arrAirport));
        modelAndView.setViewName("teleport");
        return modelAndView;
    }


}
