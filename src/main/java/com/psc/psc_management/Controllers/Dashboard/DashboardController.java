package com.psc.psc_management.Controllers.Dashboard;

import java.util.Optional;

import com.psc.psc_management.Models.Branches;
import com.psc.psc_management.Models.BuyPaddy;
import com.psc.psc_management.Models.Employees;
import com.psc.psc_management.Models.Farmers;
import com.psc.psc_management.Models.Prices;
import com.psc.psc_management.Services.Authentication.MyUserDetails;
import com.psc.psc_management.Services.Interfaces.BuyPaddyService;
import com.psc.psc_management.Services.Interfaces.EmployeeService;
import com.psc.psc_management.Services.Interfaces.FarmerService;
import com.psc.psc_management.Services.Interfaces.PriceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private FarmerService farmerService;

    @Autowired
    private PriceService priceService;

    @Autowired
    private BuyPaddyService buyPaddyService;

    @GetMapping("/")
    public String index(@AuthenticationPrincipal MyUserDetails employee, Model model) {
        Employees getEmployee = employeeService.getEmployeesByEmployeeName(employee.getUsername());
        String role = getEmployee.getRole().getRoleName();

        if (role.equals("Admin")) {
            return "/Views/Admin/Dashboard/index";
        }

        else if (role.equals("Clerk")) {
            String loggedUserName = employee.getUsername();
            Employees employees = employeeService.getEmployeesByEmployeeName(loggedUserName);
            Branches branch = employees.getBranch();

            Iterable<Farmers> listFarmers = farmerService.findAllByBranch(branch);
            model.addAttribute("listFarmers", listFarmers);
            return "/Views/Clerk/Farmers/index";
        }

        else if (role.equals("Manager")) {
            // Get Paddy Buy and Sell Details
            Optional<Prices> prices = priceService.findById(1);
            Prices price = prices.get();
            Float PaddyBuyPrice = price.getBuying();
            Float PaddySellPrice = price.getSelling();

            model.addAttribute("paddyBuyPrice", PaddyBuyPrice);
            model.addAttribute("paddySellPrice", PaddySellPrice);

            // Get Used Budget

            String loggedUserName = employee.getUsername();
            Employees employees = employeeService.getEmployeesByEmployeeName(loggedUserName);
            Branches branch = employees.getBranch();

            Float branchBudget = branch.getBudget();
            Float usedBranchBudget = branch.getUsedBudget() == null ? 0 : branch.getUsedBudget();
            Float generatedIncome = branch.getIncome() == null ? 0 : branch.getIncome();

            float budgetLeft = branchBudget - usedBranchBudget + generatedIncome;
            model.addAttribute("budgetLeft", budgetLeft);

            // //Get Generated Income
            Float branchIncome = branch.getIncome();
            model.addAttribute("income", branchIncome);

            // //Branch Employee Count
            long EmployeeCount = employeeService.countByBranch(branch);
            model.addAttribute("EmployeeCount", EmployeeCount);

            // //Branch Used Capacity
            model.addAttribute("usedCapacity", branch.getUsedCapacity());

            // //Branch Left Capacity
            float branchLeftCapacity = branch.getCapacity() - branch.getUsedCapacity();
            model.addAttribute("leftCapacity", branchLeftCapacity);

            // //Branch Crop Limit
            model.addAttribute("farmerLimit", branch.getCropLimit());

            return "Views/Manager/Dashboard/index";
        }

        else if (role.equals("Collection Officer")) {
            String loggedUserName = employee.getUsername();
            Employees employees = employeeService.getEmployeesByEmployeeName(loggedUserName);
            Branches branch = employees.getBranch();

            Iterable<BuyPaddy> buyPaddyList = buyPaddyService.findAllByBranch(branch);
            model.addAttribute("buyPaddyList", buyPaddyList);

            return "/Views/Collection/BuyPaddy/index";
        }

        else if (role.equals("Finance Officer")) {
            Iterable<BuyPaddy> buyPaddies = buyPaddyService.findAll();
            model.addAttribute("buyPaddies", buyPaddies);
            return "/Views/Finance/index";
        }

        return "";

    }
}
