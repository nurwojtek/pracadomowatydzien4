package pl.com.nur.pracadomowatydzien4.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.com.nur.pracadomowatydzien4.model.Car;
import pl.com.nur.pracadomowatydzien4.service.VehicleService;


import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

//@RestController
@Controller
@RequestMapping("/vehicles")
public class VehicleApi {

    private VehicleService vehicleList;
    private String search;

    public VehicleApi(VehicleService vehicleList) {
        this.vehicleList = vehicleList;
    }


/*
    @GetMapping()
         //   produces = {
         //   MediaType.APPLICATION_XML_VALUE,
         //   MediaType.APPLICATION_JSON_VALUE})    // dodaje XML
    public ResponseEntity<VehicleService> getVehicles(){
        return new ResponseEntity(vehicleList.getVehicleList(), HttpStatus.OK);
    }
 */

    @GetMapping
    public String getVehicles(Model model){
        model.addAttribute("cars",   vehicleList.getVehicleList());
        model.addAttribute("newCar", new Car());
        model.addAttribute("delCar", new Car());
        model.addAttribute("modCar", new Car());
        model.addAttribute("searchCar", new Car());

      //  model.addAttribute("deleteCar", vehicleList.toString());
        return "vehicle";
    }




    @GetMapping("/searchcar/")
    public String getVehiclesById(@RequestParam Long id,
                                  HttpServletResponse response,
                                  Model model1){
        Car carserch = new Car();
        Optional<Car> first = vehicleList.getVehicleList().stream()
                .filter(vehicleList -> vehicleList.getId()==id).findFirst();
        if(first.isPresent()){
            carserch.setId(first.get().getId());
            carserch.setModel(first.get().getModel());
            carserch.setMark(first.get().getMark());
            carserch.setColor(first.get().getColor());
            model1.addAttribute("searchcar",  carserch); //vehicleList.getVehicleList().get(1));
            response.setStatus(201);
            return "searchcars";
        }
        response.setStatus(404);
        return "searchcars";
    }



    @GetMapping("/query")
    //    http://localhost:8080/vehicles/query?color=Czerwony
    public String getVehicles(@RequestParam String color){
        return "redirect:/vehicles";
    }


    @PostMapping
    public String addVehicle(@ModelAttribute Car car,
                               HttpServletResponse response){
       boolean add = vehicleList.addVehicle(car);
       if(add){
           response.setStatus(201);
           return "redirect:/vehicles";
       }
       response.setStatus(400);
       return "redirect:/vehicles";
    }
/*
    @PutMapping
    public ResponseEntity modVehicle(@RequestBody Car newCar){
        Optional<Car> first = vehicleList.getVehicleList().stream()
                .filter(vehicleList -> vehicleList.getId()==newCar.getId()).findFirst();
        if(first.isPresent()){
            vehicleList.getVehicleList().remove(first.get());
            vehicleList.getVehicleList().add(newCar);
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
*/
    @PostMapping("/modification")
    public String modElementVehicle(@ModelAttribute Car car,
                                            HttpServletResponse response){
        Optional<Car> first = vehicleList.getVehicleList().stream()
                .filter(vehicleList -> vehicleList.getId()==car.getId()).findFirst();

        if(first.isPresent()) {
            if (car.getMark() != "") {
                first.get().setMark(car.getMark());
            }
            if (car.getModel() != "") {
                first.get().setModel(car.getModel());
            }
            if (car.getColor() != "") {
                first.get().setColor(car.getColor());
            }
            response.setStatus(200);
            return "redirect:/vehicles";
        }

        response.setStatus(400);
        return "redirect:/vehicles";
    }


    //@DeleteMapping("/{id}?")
    @GetMapping("/delete/{id}")
    public String removeVehicle(@PathVariable Long id,
                                HttpServletResponse response){
        Optional<Car> first = vehicleList.getVehicleList().stream()
                .filter(vehicleList -> vehicleList.getId()==id).findFirst();
        if(first.isPresent()){
            vehicleList.getVehicleList().remove(first.get());
            response.setStatus(200);
            return "redirect:/vehicles";
        }
        response.setStatus(404);
        return "redirect:/vehicles";
    }





}
