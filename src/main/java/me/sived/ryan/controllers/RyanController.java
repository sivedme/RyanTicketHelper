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

    /* Main page */
    @GetMapping("/")
    public ModelAndView showMain() {
        logger.info("Showing main page");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }


    /* Returns cheapest fares from selected airport */
    @GetMapping("/cheapest/{airport}")
    public ModelAndView showListOfCheapestFaresFrom(@PathVariable String airport) {
        logger.info("Showing a list of cheapest fares from airport " + airport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("fareList", ryanService.getCheapestFaresFrom(airport).toList());
        modelAndView.setViewName("listOfCheapestFares");
        return modelAndView;
    }

    /* Returns all routes from selected airport */
    @GetMapping("/routes/{airport}")
    public ModelAndView showListOfRoutesFrom(@PathVariable String airport) {
        logger.info("Showing a list of routes from airport " + airport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("routeList", ryanService.getRoutesFrom(airport));
        modelAndView.setViewName("listOfRoutes");
        return modelAndView;
    }

    /* Returns all fares for selected route */
    @GetMapping("/fares/{depAirport}/{arrAirport}/{dateFrom}/{dateTo}")
    public ModelAndView showAllFaresForRoute(@PathVariable String depAirport, @PathVariable String arrAirport,
                                             @PathVariable String dateFrom, @PathVariable String dateTo) {
        logger.info("Showing a list of all fares from airport " + depAirport + " to " + arrAirport + " " + dateFrom + " " + dateTo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allFaresForRoute", ryanService.getAllFaresForRouteAndDates(depAirport, arrAirport, dateFrom, dateTo));
        modelAndView.setViewName("listOfAllFaresForRoute");
        return modelAndView;
    }

    /* Returns all fares for selected route */
    @GetMapping("/fares/{depAirport}/{arrAirport}")
    public ModelAndView showAllFaresForRoute(@PathVariable String depAirport, @PathVariable String arrAirport) {
        logger.info("Showing a list of all fares from airport " + depAirport + " to " + arrAirport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allFaresForRoute", ryanService.getAllFaresForRoute(depAirport, arrAirport));
        modelAndView.setViewName("listOfAllFaresForRoute");
        return modelAndView;
    }

    /* Returns all fares for all routes from selected airport */
    @GetMapping("/fares/{airport}")
    public ModelAndView showAllFaresFrom(@PathVariable String airport) {
        logger.info("Showing a list of all fares from airport " + airport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allFaresList", ryanService.getAllFaresFrom(airport));
        modelAndView.setViewName("listOfAllFares");
        return modelAndView;
    }

    /* Returns all fares for all routes from selected airport */
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

    /* Returns all fares for all routes from selected airport */
    @GetMapping("/teleport/{depAirport}/{arrAirport}/{dateFrom}/{dateTo}")
    public ModelAndView teleport(@PathVariable String depAirport, @PathVariable String arrAirport,
                                 @PathVariable String dateFrom, @PathVariable String dateTo) {
        logger.info("Showing a teleport feature " + depAirport + " " + arrAirport + " " + dateFrom + " " + dateTo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("teleportList", ryanService.teleport(depAirport, arrAirport, dateFrom, dateTo));
        modelAndView.setViewName("teleport");
        return modelAndView;
    }

    /* Returns all fares for all routes from selected airport */
    /* Not supported yet */
    @GetMapping("/teleport/{depAirport}/{arrAirport}/{length}")
    public ModelAndView teleport(@PathVariable String depAirport, @PathVariable String arrAirport,
                                 @PathVariable String length) {
        logger.info("Showing a return teleport feature " + depAirport + " " + arrAirport + " " + length);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("teleportList", ryanService.teleport(depAirport, arrAirport));
        modelAndView.setViewName("teleport");
        return modelAndView;
    }

    /* Returns all fares for all routes from selected airport */
    @GetMapping("/teleport/{depAirport}/{arrAirport}")
    public ModelAndView teleport(@PathVariable String depAirport, @PathVariable String arrAirport) {
        logger.info("Showing a teleport feature " + depAirport + " " + arrAirport);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("teleportList", ryanService.teleport(depAirport, arrAirport));
        modelAndView.setViewName("teleport");
        return modelAndView;
    }


}
