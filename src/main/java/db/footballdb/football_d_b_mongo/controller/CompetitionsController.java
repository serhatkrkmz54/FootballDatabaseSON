package db.footballdb.football_d_b_mongo.controller;

import db.footballdb.football_d_b_mongo.model.CompetitionsDTO;
import db.footballdb.football_d_b_mongo.service.CompetitionsService;
import db.footballdb.football_d_b_mongo.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/competitionss")
public class CompetitionsController {

    private final CompetitionsService competitionsService;

    public CompetitionsController(final CompetitionsService competitionsService) {
        this.competitionsService = competitionsService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("competitionses", competitionsService.findAll());
        return "competitions/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("competitions") final CompetitionsDTO competitionsDTO) {
        return "competitions/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("competitions") @Valid final CompetitionsDTO competitionsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "competitions/add";
        }
        competitionsService.create(competitionsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("competitions.create.success"));
        return "redirect:/competitionss";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("competitions", competitionsService.get(id));
        return "competitions/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("competitions") @Valid final CompetitionsDTO competitionsDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "competitions/edit";
        }
        competitionsService.update(id, competitionsDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("competitions.update.success"));
        return "redirect:/competitionss";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = competitionsService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            competitionsService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("competitions.delete.success"));
        }
        return "redirect:/competitionss";
    }

}
