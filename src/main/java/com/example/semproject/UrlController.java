package com.example.semproject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UrlController {

//    @Autowired
//    private ShrinkService service;
//
//    @PostMapping(value = "shrink")
//    public ResponseEntity<?> shortenUrl(@RequestBody ShrinkRequest request) {
//        try {
//            ShrinkResponse response = service.shrinkUrl(request);
//            HttpStatus status = (response.isValidUrl() && StringUtils.isBlank(response.getErrorDesc())) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
//            return new ResponseEntity<ShrinkResponse>(response, status);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//
//    }
//
//    @GetMapping(value = "{shrinkKey}")
//    public ResponseEntity<?> getUrlAndRedirect(@PathVariable String shrinkKey) {
//        try {
//            String url = service.getFullUrl(shrinkKey);
//
//            String response = StringUtils.isBlank(url) ? "URL_NOT_FOUND" : url;
//            HttpStatus status = StringUtils.isBlank(url) ? HttpStatus.NOT_FOUND : HttpStatus.OK;
//
//            if (status.is2xxSuccessful())
//                return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(response)).build();
//            else
//                return new ResponseEntity<String>(response, status);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }

    @GetMapping("/register")
    public ModelAndView showRegisterPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register.html");
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView showHomePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home.html");
        return modelAndView;
    }
}

